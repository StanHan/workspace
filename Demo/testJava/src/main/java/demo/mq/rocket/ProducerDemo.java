package demo.mq.rocket;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * MQ 消息生产者 发送消息注意事项:
 * <li>一个应用尽可能用一个Topic，消息子类型用tags来标识，tags可以由应用自由设置。只有发送消息设置了tags，消费方在订阅消息时，才可以利用tags在broker做消息过滤。
 * <li>每个消息在业务层面的唯一标识码，要设置到keys字段，方便将来定位消息丢失问题。服务器会为每个消息创建索引（哈希索引），应用可以通过topic，key来查询这条消息内容，以及消息被谁消费。
 * <li>消息发送成功或者失败，要打印消息日志，务必要打印sendresult和key字段。
 * <li>对于消息不可丢失应用，务必要有消息重发机制，例如如果消息发送失败，存储到数据库，能有定时程序尝试重发，或者人工触发重发。
 * 
 * 消息发送失败如何处理？ Producer的send方法本身支持内部重试，重试逻辑如下：
 * <li>至多重试3次。
 * <li>如果发送失败，则轮转到下一个Broker。
 * <li>这个方法的总耗时时间不超过sendMsgTimeout设置的值，默认10s。所以，如果本身向broker发送消息产生超时异常，就不会再做重试。
 * 
 * 以上策略仍然不能保证消息一定发送成功，为保证消息一定成功，建议应用这样做：如果调用send同步方法发送失败，则尝试将消息存储到db，由后台线程定时重试，保证消息一定到达Broker。
 */
public class ProducerDemo {
    public static void main(String[] args) throws MQClientException {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键， 因为服务器会回查这个Group下的任意一个Producer
         */
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupTest");

        producer.setNamesrvAddr("172.16.30.162:9876");

        producer.setInstanceName(String.valueOf(System.currentTimeMillis()));

        producer.setVipChannelEnabled(false);
        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        producer.start();

        /**
         * 下面这段代码表明一个Producer对象可以发送多个topic，多个tag的消息。 注意：send方法是同步调用，只要不抛异常就标识成功。
         * 但是发送成功也可会有多种状态，例如消息写入Master成功，但是Slave不成功，这种情况消息属于成功，但是对于个别应用如果对消息可靠性要求极高，需要对这种情况做处理。
         * 另外，消息可能会存在发送失败的情况，失败重试由应用来处理。
         * <li>SEND_OK，消息发送成功。
         * <li>FLUSH_DISK_TIMEOUT，消息发送成功，但是服务器刷盘超时，消息已经进入服务器队列，只有此时服务器宕机，消息才会丢失。
         * <li>FLUSH_SLAVE_TIMEOUT，消息发送成功，但是服务器同步到Slave时超时，消息已经进入服务器队列，只有此时服务器宕机，消息才会丢失。
         * <li>SLAVE_NOT_AVAILABLE，消息发送成功，但是此时slave不可用，消息已经进入服务器队列，只有此时服务器宕机，消息才会丢失。
         */
        try {
            {
                Message msg = new Message("broker-a", // topic
                        "TagB", // tag
                        "OrderID002", // key
                        ("Hello MetaQ2").getBytes());// body
                SendResult sendResult = producer.send(msg);
                System.out.println(sendResult);
            }
            {
                Message msg = new Message("TopicTest2", // topic
                        "TagB", // tag
                        "OrderID0034", // key
                        ("Hello MetaQ").getBytes());// body
                SendResult sendResult = producer.send(msg);
                System.out.println(sendResult);
            }

            {
                Message msg = new Message("TopicTest3", // topic
                        "TagC", // tag
                        "OrderID061", // key
                        ("Hello MetaQ").getBytes());// body
                /**
                 * 选择oneway形式发送。一个RPC调用，通常是这样一个过程：
                 * <li>客户端发送请求到服务器
                 * <li>服务器处理该请求
                 * <li>服务器向客户端返回应答
                 * 
                 * 所以一个RPC的耗时时间是上述三个步骤的总和，而某些场景要求耗时非常短，但是对可靠性要求并不高，例如日志收集类应用，此类应用可以采用oneway形式调用。
                 * oneway形式只发送请求不等待应答，而发送请求在客户端实现层面仅仅是一个os系统调用的开销，即将数据写入客户端的socket缓冲区，此过程耗时通常在微秒级。
                 */
                producer.sendOneway(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
         */
        producer.shutdown();
    }
}