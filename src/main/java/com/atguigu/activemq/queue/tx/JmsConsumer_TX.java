package com.atguigu.activemq.queue.tx;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息消费者
 *
 * @Author cuihaiyan
 * @Create_Time 2019-07-10 14:06
 */
public class JmsConsumer_TX {


    public static final String BROKER_URL = "tcp://localhost:61616";
    public static final String QUEUE_NAME = "queue-test-01";

    public static void main(String[] args) throws JMSException {
        System.out.println("我是1号消费者");

        //1，创建连接工厂，按照给定的URL地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

        //2，通过连接工厂，获取连接并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3，创建会话session
        //args1 事务
        //args2 签收
        //Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //Session session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        //4，创建目的地(queue or topic)
        Queue queue = session.createQueue(QUEUE_NAME);

        //5，创建消息的消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);


        //同步阻塞:
        while (true) {
            //6，消费
            //TextMessage textMessage = (TextMessage) messageConsumer.receive();
            // 设置超时时间
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L);
            if (null != textMessage) {
                System.out.println("*************消费者接收到消息:" + textMessage.getText());
                textMessage.acknowledge(); //手动签收
            } else {
                break;
            }
        }

        //7，关闭资源
        messageConsumer.close();
        //session.commit(); //事务为 true的情况下，先不执行commit ，后果：消息会被重复消费
        //session.commit(); //开启commit之后，消息只被消费一次
        session.close();
        connection.close();

        System.out.println("*************消息消费【"+QUEUE_NAME+"】消息 完成*************");
    }
}
