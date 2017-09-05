/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.mq;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * @author taosj
 * @version Consumer.java, v0.1 2017年3月7日 上午9:29:49
 */
public abstract class Consumer {
    protected DefaultMQPushConsumer defaultMQPushConsumer;
    @Value("rocketMQ.namesrvAddr")
    private String namesrvAddr;

    @PostConstruct
    public void init() throws MQClientException {
        defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        init(defaultMQPushConsumer);
    }

    public abstract void init(DefaultMQPushConsumer defaultMQPushConsumer) throws MQClientException;
}
