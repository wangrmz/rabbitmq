package com.atguigu.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-04 -18:21
 *
 * 链接工厂，创建信道的工具类
 **/

public class RabbitMqUtils {


    /**
     * 得到一个链接的channel
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static  Channel getChannel() throws IOException, TimeoutException {

        // 1、创建链接工厂
        // connection中的信道 理解mq的四大核心组件
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("12345");
        Connection connection = factory.newConnection();

        // 2、创建信道,信道来发送消息，链接中有多个信道
        Channel channel = connection.createChannel();

        return  channel;
    }

}
