## 消息中间件之ActiveMQ

### 1、ActiveMQ 下载、安装、启动

1. 下载 http://activemq.apache.org/components/classic/download/

   > ActiveMQ 解压之后,目录如下

   ```bash
   -rw-r--r--@  1 cuihaiyan  staff     40580  3 15 20:04 LICENSE
   -rw-r--r--@  1 cuihaiyan  staff      3334  3 15 20:04 NOTICE
   -rw-r--r--@  1 cuihaiyan  staff      2610  3 15 20:04 README.txt
   -rwxr-xr-x@  1 cuihaiyan  staff  18162140  3 15 20:02 activemq-all-5.15.9.jar
   drwxr-xr-x  10 cuihaiyan  staff       320  6 20 10:30 bin
   drwxr-xr-x@ 20 cuihaiyan  staff       640  7  9 16:58 conf
   drwxr-xr-x@  8 cuihaiyan  staff       256  7  9 16:59 data
   drwxr-xr-x@  5 cuihaiyan  staff       160  7  9 17:41 docs
   drwxr-xr-x@  7 cuihaiyan  staff       224  3 15 20:04 examples
   drwxr-xr-x@ 22 cuihaiyan  staff       704  3 15 20:04 lib
   drwxr-xr-x   4 cuihaiyan  staff       128  7 10 14:02 tmp
   drwxr-xr-x@  8 cuihaiyan  staff       256  3 15 20:04 webapps
   drwxr-xr-x   3 cuihaiyan  staff        96  6 20 10:29 webapps-demo
   ```

   > doc目录有具体的Getting started 文档: **ActiveMQ安装目录/docs/user-guide.html**

2. API 文档 http://activemq.apache.org/maven/apidocs/



### 2、JMS开发的基本步骤

1. 创建一个连接工厂 connection factory

2. 通过 connection factory 来创建 JMS connection

3. 启动 JMS connection

4. 通过 connection 创建 JMS session

5. 创建 JMS Destination

6. 创建 JMS producer 或者创建 JMS message并设置 destination

7. 创建 JMS consumer 或者注册一个 JMS message listener

8. 发送或者接收 JMS message

9. 关闭所有的 JMS 资源

   connection 、session、producer、consumer 等



### 3、JMS 组成的四大元素

1. JMS Provider 实现JMS接口和规范的消息中间件,也就是我们的MQ服务器

2. JMS producer 消息生产者

3. JMS consumer 消息消费者

4. JMS message 消息

   - 消息头

     > JMS Destination
     >
     > JMS DeliveryMode
     >
     > JMS Expiration
     >
     > JMS Priority
     >
     > JMS MessageID

   - 消息体

     > 5种消息体格式:
     >
     > 1. TextMessage
     > 2. MapMessage
     > 3. BytesMessage
     > 4. StreamMessage
     > 5. ObjectMessage

   - 消息属性



### 4、JMS可靠性

1. 持久 persistent
2. 事务 transaction
3. 签收 acknowledge



队列默认持久化(ActiveMQ服务器宕机重启后,未被消费的消息依然存在,不会丢失)

是什么是事务,事务的隔离级别,事务的特性



```java
Session createSession(boolean transacted, int acknowledgeMode)
```

自动签收、手动签收

**事务偏生产者/签收偏消费者**

签收-非事务

> acknowledgeMode 的情况: 
>
> 自动签收-默认  Session.AUTO_ACKNOWLEDGE
>
> 手动签收  Session.CLIENT_ACKNOWLEDGE (调方法 textMessage.acknowledge(); //手动签收 )
>
> 允许重复消息 Session.DUPS_OK_ACKNOWLEDGE

签收-事务

> acknowledgeMode 应答模式 的情况: 
>
> 自动签收-默认  Session.AUTO_ACKNOWLEDGE
>
> 手动签收  Session.CLIENT_ACKNOWLEDGE 
>
> 允许重复消息 Session.DUPS_OK_ACKNOWLEDGE



在事务模式下,执行了commit() ,就被认为是自动签收,此时这个参数作用不大;

如果事务被回滚,则消息会被再次传送;

如果是非事务模式下,自动签收就是自动,手动签收就是手动;



**事务的设置 > 签收的设置**



**如何保证消息的可靠性/高可用?**

以ActiveMQ举例,说明以下方面:

> 集群-宕机
>
> 可靠性: 持久性、事务、签收



### activeMQ 两种消费方式

1、同步阻塞方式( receive())

> 订阅者或者接收者调用 MessageConsumer 的 receive() 方法来接收消息, receive方法能够在接收到消息之前(或超时之前)一直阻塞

```java
//一直处于消费等待状态
TextMessage textMessage = (TextMessage) messageConsumer.receive();
//设置超时时间，超过时间不再等待
TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L);
```



2、异步非阻塞方式(监听器onMessage()):

> 订阅者或接收者通过 MessageConsumer 的 setMessageListener(MessageListener listener) 注册一个消息监听器, 当消息达到之后, 系统自动调用监听器 MessageListener 的onMessage(Message message) 方法. 

```java
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
});

System.in.read(); //控制台不关闭
```



### 队列Queue消费者3大消费情况:

1、先生产,	只启动1号消费者,	问题: 1号消费者能消费消息吗?

> 1号消费可以消费 Y

2、先生产,	先启动1号消费者,再启动2号消费者,	问题: 2号消费者还能消费消息吗?

> 1号消费可以消费 Y
>
> 2号消费者不可以消费 N

3、先启动1号消费者和2号消费者,	再生产6条消息,	问题:消费消息情况如何?

> 两个消费者,一人一半,平均分配  Y



### 主题Topic消费者消费情况:

1、先生产,	只启动1号消费者,	问题: 1号消费者能消费消息吗?

> 1号消费可以消费 N

2、先生产,	再启动1号消费者和2号消费者,	问题: 1号和2号消费者还能消费消息吗?

> 1号消费可以消费消息 N
>
> 2号消费可以消费消息 N

3、先启动1号消费者和2号消费者,	再生产6条消息,	问题:消费消息情况如何?

> 两个消费者,每个人都获取全部的消息 Y



### 总结:

队列:

> 在点对点的消息传递域中,目的地被称为队列 (Queue) 

点对点消息传递域的特点如下:

- 每个消息只能有一个消费者
- 消息的生产者和消费者没有时间上的相关性, 
- 消息被消费后,队列中不会再存储,所以消费者不会消费以及被消费掉的消息

**异步传输:例如 发短信**



主题:

> 在发布订阅消息传递域中,目的地被称为主题(topic)

- 生产者将消息发布到topic中,每个消息可以有多个消费者,属于1:N关系

- 生产者和消费者有时间上的相关性:订阅某一个主题的消费者,只能消费自它订阅之后发布的消息

  (比如: 订阅公众号)

- 生产者生产时,topic不保存消息,它是无状态的不落地,假如无人订阅就去生产,那是一条废消息,所以一般是先启动消费者,再去启动生产者

**消息订阅者和消息发布者相互独立,不需要接触**

> 非持久订阅
>
> 持久订阅
>
> Ps:  一定要先订阅注册才能接收到发布,只给订阅者发布消息 



### ActiveMQ Broker

```java
package com.atguigu.activemq.Embed;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;

/**
 * 内嵌Broker:mini版的activeMQ
 *
 * @Author cuihaiyan
 * @Create_Time 2019-07-11 21:21
 */
public class EmbedBroker {

    public static void main(String[] args) throws Exception {
        //嵌入式的broker
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        TransportConnector connector = brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();

    }
}
```



### Spring 整合 ActiveMQ

> 35_Spring整合ActiveMQ之队列生产者
> 36_Spring整合ActiveMQ之队列消费者
> 37_Spring整合ActiveMQ之主题生产消费
> 38_Spring整合ActiveMQ之监听器配置

bug 注意顺序

```xml
				<!--activeMQ 所需的jar配置-->
        <!-- https://mvnrepository.com/artifact/org.apache.activemq/activemq-all -->
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-all</artifactId>
            <version>5.15.9</version>
        </dependency>
        
        <!--spring 和 activeMQ 整合-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jms</artifactId>
            <version>4.2.4.RELEASE</version>
        </dependency>
```

如果顺序颠倒,会报错,如下:

```java
Exception in thread "main" java.lang.NoSuchMethodError: org.springframework.beans.factory.config.ConfigurableListableBeanFactory.hasEmbeddedValueResolver()Z
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:845)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:543)
	at org.springframework.context.support.ClassPathXmlApplicationContext.<init>(ClassPathXmlApplicationContext.java:139)
	at org.springframework.context.support.ClassPathXmlApplicationContext.<init>(ClassPathXmlApplicationContext.java:83)
	at com.atguigu.activemq.spring.SpringMQ_Producer.main(SpringMQ_Producer.java:24)

Process finished with exit code 1

```

[受此启发](https://blog.csdn.net/myfortune/article/details/85264170)



Spring 里面不启动消费者,直接通过配置监听完成:

```xml
<!--配置监听程序-->
<bean class="org.springframework.jms.listener.DefaultMessageListenerContainer" id="jmsContainer">
    <property name="connectionFactory" ref="jmsFactory"/>
    <property name="destination" ref="desctinationTopic" />
    <property name="messageListener" ref="myMessageListener"/>
</bean>

<!--配置监听器实现类或者在类上面添加注解：@Component-->
<!--<bean id="myMessageListener" class="com.atguigu.activemq.spring.MyMessageListener"/>-->
```

