package com.atguigu.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Author cuihaiyan
 * @Create_Time 2019-07-10 22:43
 */
public class JmsProducer_Topic_Persist {

    public static final String BROKER_URL = "tcp://localhost:61616";
    public static final String TOPIC_NAME = "topic-persist-01";

    public static void main(String[] args) throws JMSException {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        //connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        MessageProducer messageProducer = session.createProducer(topic);
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT); //持久化

        connection.start();

        for (int i = 1; i <= 3; i++) {
            TextMessage textMessage = session.createTextMessage("msg-persist-" + i);
            messageProducer.send(textMessage);
        }

        //7，关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("*************消息发布到 MQ 完成*************");
    }
}
