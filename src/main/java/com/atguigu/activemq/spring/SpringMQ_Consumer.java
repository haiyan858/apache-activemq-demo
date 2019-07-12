package com.atguigu.activemq.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author cuihaiyan
 * @Create_Time 2019-07-12 07:44
 */

@Service
public class SpringMQ_Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        SpringMQ_Consumer springMQ_Consumer = (SpringMQ_Consumer) ctx.getBean("springMQ_Consumer");

        String retValue = (String) springMQ_Consumer.jmsTemplate.receiveAndConvert();

        System.out.println("********** 消费者收到的消息************:"+retValue);
    }
}
