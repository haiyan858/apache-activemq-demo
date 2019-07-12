package com.atguigu.activemq.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @Author cuihaiyan
 * @Create_Time 2019-07-11 21:33
 */

@Service
public class SpringMQ_Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        SpringMQ_Producer springMQ_producer = (SpringMQ_Producer) ctx.getBean("springMQ_Producer");

       /* springMQ_producer.jmsTemplate.send(new MessageCreator()
        {
            @Override
            public Message createMessage(Session session) throws JMSException {

                TextMessage textMessage = session.createTextMessage("*****spring+ActiveMQ 整合*********");
                return textMessage;
            }
        });*/

       //lambda 表达式写法
        springMQ_producer.jmsTemplate.send((Session session) -> {
            TextMessage textMessage = session.createTextMessage("*****spring+ActiveMQ 整合 messageListener 3333*********");
            return textMessage;
        });

        System.out.println("********** send task over ************");
    }
}
