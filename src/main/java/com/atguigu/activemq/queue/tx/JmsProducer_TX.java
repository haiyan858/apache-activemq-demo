package com.atguigu.activemq.queue.tx;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息提供者-事务
 * <p>
 * session.commit(); //事务为true ，要记得提交
 *
 * @Author cuihaiyan
 * @Create_Time 2019-07-10 13:02
 */
public class JmsProducer_TX {

    public static final String BROKER_URL = "tcp://localhost:61616";
    public static final String QUEUE_NAME = "queue-test-01";

    public static void main(String[] args) throws JMSException {

        //1，创建连接工厂，按照给定的URL地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

        //2，通过连接工厂，获取连接并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3，创建会话session
        //args1 事务
        //args2 签收
        //Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE); //需要commit

        //4，创建目的地(queue or topic)
        Queue queue = session.createQueue(QUEUE_NAME);

        //5，创建消息的生产者
        MessageProducer messageProducer = session.createProducer(queue);

        //6，通过使用messageProducer生产3条数据发送到 MQ的队列里面
        for (int i = 1; i <= 3; i++) {
            //6.1，创建消息
            TextMessage textMessage = session.createTextMessage("tx---msg----" + i);
            //6.2，通过 messageProducer发送给MQ
            messageProducer.send(textMessage);
        }

        //7，关闭资源
        messageProducer.close();
        session.commit(); //事务为true ，要记得提交

        session.close();
        connection.close();

        System.out.println("*************消息发布到 MQ 完成*************");

        /*
        session 的提交、回滚、关闭
        try {
            // session ok
            session.commit();
        }catch (Exception e){
            // session error
            session.rollback(); //回滚容错
        }finally {
            if (null != session){
                session.close();
            }
        }
        */

    }
}
