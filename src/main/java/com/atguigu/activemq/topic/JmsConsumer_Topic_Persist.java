package com.atguigu.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @Author cuihaiyan
 * @Create_Time 2019-07-10 22:43
 */
public class JmsConsumer_Topic_Persist {


    public static final String BROKER_URL = "tcp://localhost:61616";
    //public static final String QUEUE_NAME = "topic-test-01";
    public static final String TOPIC_NAME = "topic-persist-01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是z3 消费者-Persist");

        //1，创建连接工厂，按照给定的URL地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        //2，通过连接工厂，获取连接并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        //connection.start();
        connection.setClientID("z3");
        //3，创建会话session
        //args1 事务, args2 签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4，创建目的地(queue or topic)
        Topic topic = session.createTopic(TOPIC_NAME);
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "remark.......");
        connection.start();

        Message message = topicSubscriber.receive();
        while (null != message){
            TextMessage textMessage = (TextMessage) message;
            System.out.println("*****收到的持久化消息："+textMessage.getText()+"********");
            message = topicSubscriber.receive();
        }

        //7，关闭资源
        session.close();
        connection.close();

        System.out.println("*************消息消费【" + TOPIC_NAME + "】消息 完成*************");
    }
}
