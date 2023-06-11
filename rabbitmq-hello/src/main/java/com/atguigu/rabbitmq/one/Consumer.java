package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-01 -22:24
 **/

public class Consumer {

    // 直接使用默认的交换机
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        // 1、创建链接工厂
        // connection中的信道 理解mq的四大核心组件

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("12345");

        Connection connection = factory.newConnection();
        // 2、创建信道,接受消息
        Channel channel = connection.createChannel();

        //声明，接受消息
        DeliverCallback deliverCallback = (var1, var2) -> {
            System.out.println(new String(var2.getBody()));
        };

        //  声明取消消费消息的回调
        CancelCallback cancelCallback = var -> {
            System.out.println("消息消费被中断");
        };

        // 3、
        /**
         * 1、消费者需要消费的队列名称
         * 2、消费成功之后是否自动应答，true自动应答，false代表手动应答
         * 3、消费者成功消费的回调函数
         * 4、消息取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

        // 4、





    }


}

