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
