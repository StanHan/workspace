package demo.mq.rocket;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 消费过程要做到幂等。在编程中一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同。
 * 幂等函数，或幂等方法，是指可以使用相同参数重复执行，并能获得相同结果的函数。这些函数不会影响系统状态，也不用担心重复执行会对系统造成改变。
 *
 * RocketMQ目前无法避免消息重复，所以如果业务对消费重复非常敏感，务必要在业务层面去重，有以下几种去重方式：
 * 
 * <li>将消息的唯一键，可以是msgId，也可以是消息内容中的唯一标识字段，例如订单Id等，消费之前判断是否在Db或Tair(全局KV存储)中存在，如果不存在则插入，并消费，否则跳过。（实际过程要考虑原子性问题，判断是否存在可以尝试插入，如果报主键冲突，则插入失败，直接跳过）。msgId一定是全局唯一标识符，但是可能会存在同样的消息有两个不同msgId的情况（有多种原因），这种情况可能会使业务上重复消费，建议最好使用消息内容中的唯一标识字段去重。
 * <li>使用业务层面的状态机去重。
 * 
 * 提高消费并行度。绝大部分消息消费行为属于IO密集型，即可能是操作数据库，或者调用RPC，这类消费行为的消费速度在于后端数据库或者外系统的吞吐量，通过增加消费并行度，可以提高总的消费吞吐量，但是并行度增加到一定程度，反而会下降。
 * 
 * 修改消费并行度方法如下所示：
 * <li>同一个ConsumerGroup下，通过增加Consumer实例数量来提高并行度，超过订阅队列数的Consumer实例无效。可以通过加机器，或者在已有机器启动多个进程的方式。
 * <li>提高单个Consumer的消费并行线程，通过修改以下参数.consumer.setConsumeThreadMin();consumer.setConsumeThreadMax()
 * 
 * 消息批量消费。某些业务流程如果支持批量方式消费，则可以很大程度上提高消费吞吐量，例如订单扣款类应用，一次处理一个订单耗时1秒钟，一次处理10个订单可能也只耗时2秒钟，这样即可大幅度提高消费的吞吐量。
 * 通过设置consumer的consumeMessageBatchMaxSize这个参数，默认是1，即一次只消费一条消息，例如设置为N，那么每次消费的消息数小于等于N。
 * 
 * 跳过非重要消息。发生消息堆积时，如果消费速度一直追不上发送速度，可以选择丢弃不重要的消息
 * 
 * 优化每条消息消费过程
 */
public class PushConsumerDemo {

    /**
     * 通过注册监听的方式来消费信息。 当前例子是PushConsumer用法，使用方式给用户感觉是消息从RocketMQ服务器推到了应用客户端。
     * 但是实际PushConsumer内部是使用长轮询Pull方式从Broker拉消息，然后再回调用户Listener方法<br>
     * 
     * @throws MQClientException
     */
    public static void main(String[] args) throws InterruptedException, MQClientException {
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例。 注意：ConsumerGroupName需要由应用来保证唯一
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("testmerchantLeagueConsumerGroup");
        consumer.setNamesrvAddr("172.16.30.162:9876");

        /** 提高单个Consumer的消费并行线程 */
        consumer.setConsumeThreadMin(20);
        consumer.setConsumeThreadMax(64);
        /** 一次只消费一条消息 */
        consumer.setConsumeMessageBatchMaxSize(1);

        /** 订阅指定topic下tags分别等于TagA或TagB */
        consumer.subscribe("broker-a", "TagB || TagA");
        /**
         * 订阅指定topic下所有消息。 注意：一个consumer对象可以订阅多个topic
         */
        consumer.subscribe("TopicTest2", "*");
        consumer.subscribe("TopicTest3", "*");

        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.registerMessageListener(new MessageListenerConcurrently() {

            /**
             * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
             */
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                long offset = msgs.get(0).getQueueOffset();
                String maxOffset = msgs.get(0).getProperty(MessageConst.PROPERTY_MAX_OFFSET);
                long diff = Long.parseLong(maxOffset) - offset;
                /** 当某个队列的消息数堆积到100000条以上，则尝试丢弃部分或全部消息，这样就可以快速追上发送消息的速度。 */
                if (diff > 10_0000) {
                    // TODO 消息堆积情况的特殊处理
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                // TODO 正常消费过程
                System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);

                MessageExt msg = msgs.get(0);

                if (msg.getTopic().equals("broker-a")) {
                    // 执行TopicTest1的消费逻辑
                    if (msg.getTags() != null && msg.getTags().equals("TagA")) {
                        // 执行TagA的消费
                        String message = new String(msg.getBody());
                        System.out.println(message);
                    } else if (msg.getTags() != null && msg.getTags().equals("TagB")) {
                        // 执行TagB的消费
                        String message = new String(msg.getBody());
                        System.out.println(message);
                    }

                } else if (msg.getTopic().equals("TopicTest2")) {
                    // 执行TopicTest2的消费逻辑
                    System.out.println("TopicTest2");
                }
                // 消费者向mq服务器返回消费成功的消息
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        /**
         * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         */
        consumer.start();

        System.out.println("Consumer Started.");
    }
}