package com.wsq.common.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    /**
     * 同步发送mq
     * @param msg
     * @param topic
     * @param tag
     * @param key
     * @throws Exception
     */
    public void sendMsg(String msg, String topic, String tag, String key) throws Exception {
        Message message = new Message(topic, tag, key, msg.getBytes());
        defaultMQProducer.send(message);
    }

    /**
     * 异步发送mq
     * @param msg
     * @param topic
     * @param tag
     * @param key
     * @throws Exception
     */
    public void sendAsyncMsg(String msg, String topic, String tag, String key) throws Exception {
        Message message = new Message(topic, tag, key, msg.getBytes());
        defaultMQProducer.send(message, new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }
}
