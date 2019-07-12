package com.atguigu.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.Message;

import javax.jms.*;
import java.io.IOException;

/**
 * @Author cuihaiyan
 * @Create_Time 2019-07-10 22:43
 */
public class JmsConsumer_Topic {


    public static final String BROKER_URL = "tcp://localhost:61616";
    //public static final String QUEUE_NAME = "topic-test-01";
    public static final String TOPIC_NAME = "topic-test-01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是1号消费者");

        //1，创建连接工厂，按照给定的URL地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

        //2，通过连接工厂，获取连接并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3，创建会话session
        //args1 事务
        //args2 签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4，创建目的地(queue or topic)
        //Queue queue = session.createQueue(QUEUE_NAME);
        Topic topic = session.createTopic(TOPIC_NAME);

        //5，创建消息的消费者
        //MessageConsumer messageConsumer = session.createConsumer(queue);
        MessageConsumer messageConsumer = session.createConsumer(topic);

        /*
        同步阻塞:
        while (true) {
            //6，消费
            //TextMessage textMessage = (TextMessage) messageConsumer.receive();
            // 设置超时时间
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L);
            if (null != textMessage) {
                System.out.println("*************消费者接收到消息:" + textMessage.getText());
            } else {
                break;
            }
        }*/


        /*
        //消费者监听
        messageConsumer.setMessageListener(new MessageListener()
        {
            @Override
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("*************消费者接收到消息:" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

        //消费者监听
        //lambda 表达式写法
        messageConsumer.setMessageListener((message) -> {
            if (null != message && message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("*************消费者接收到"+TOPIC_NAME+"消息:" + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });


        System.in.read();

        //7，关闭资源
        messageConsumer.close();
        session.close();
        connection.close();

        System.out.println("*************消息消费【" + TOPIC_NAME + "】消息 完成*************");
    }
}
