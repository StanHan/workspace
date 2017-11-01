package demo.mq.rocket;

import java.util.Set;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.MessageQueueListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;

public class PullConsumerDemo {

    /**
     * 通过拉去的方式来消费消息
     * 
     * @throws MQClientException
     */
    public static void main(String[] args) throws InterruptedException, MQClientException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer();
        consumer.setNamesrvAddr("100.66.154.81:9876");
        consumer.setConsumerGroup("broker");
        try {
            consumer.start();
            Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues("PushTopic");

            for (MessageQueue messageQueue : messageQueues) {

                System.out.println(messageQueue.getTopic());
            }

            // 消息队列的监听
            consumer.registerMessageQueueListener("", new MessageQueueListener() {

                @Override
                // 消息队列有改变，就会触发
                public void messageQueueChanged(String topic, Set<MessageQueue> mqAll, Set<MessageQueue> mqDivided) {
                    // TODO Auto-generated method stub

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}