/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.mq;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * @author taosj
 * @version Producer.java, v0.1 2017年3月7日 上午9:30:13
 */
public abstract class Producer {
    private DefaultMQProducer defaultMQProducer;
    @Value("${rocketMQ.namesrvAddr}")
    protected String namesrvAddr;
    private static Logger log = LoggerFactory.getLogger(Producer.class);
    @PostConstruct
    public void init() throws MQClientException {
        defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        init(defaultMQProducer);
    }

    public abstract void init(DefaultMQProducer defaultMQProducer) throws MQClientException;

    public boolean send(Message message)
            throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        log.info("MA mq address----->{}",namesrvAddr);
        SendResult sendResult = defaultMQProducer.send(message);
        log.info("MQ return msgID:" + sendResult.getMsgId());
        if (sendResult != null && sendResult.getSendStatus() == SendStatus.SEND_OK) {
            return true;
        } else {
            return false;
        }
    }

    public DefaultMQProducer getDefaultMQProducer() throws MQClientException {
        return defaultMQProducer;
    }

    public static class MassageContent {
        public final String topic;
        public final String tag;
        public final String keys;
        public final byte[] body;

        public MassageContent(String topic, String tag, String keys, byte[] body) {
            this.topic = topic;
            this.tag = tag;
            this.keys = keys;
            this.body = body;
        }
    }
}
