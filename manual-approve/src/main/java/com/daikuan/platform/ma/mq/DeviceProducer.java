/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.daikuan.platform.ma.exception.MQException;

/**
 * 
 * @author taosj
 * @version DeviceProducer.java, v0.1 2017年3月7日 上午9:30:04
 */
@Component
public class DeviceProducer extends Producer {
    private static final Logger logger = LoggerFactory.getLogger(DeviceProducer.class);

    @Override
    public void init(DefaultMQProducer defaultMQProducer) throws MQClientException {
        defaultMQProducer.setProducerGroup("CustomerServiceAuditResult");
        defaultMQProducer.setInstanceName(String.valueOf(System.currentTimeMillis()));
        defaultMQProducer.start();
        logger.info("===producer===");
    }

    public boolean messageSend(MassageContent messageContent) throws MQException {
        try {
            logger.info("DeviceProducerMA mq address----->{}",namesrvAddr);
            Message message = new Message(messageContent.topic, messageContent.tag, messageContent.body);
            return send(message);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            throw new MQException("messageSend error", e);
        }
    }
}
