package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-05-31 -21:52
 **/

public class Producer {

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
        // 2、创建信道,信道来发送消息，链接中有多个信道
        Channel channel = connection.createChannel();


        // 3、生成一个队列
        /**
         * 1、队列名称
         * 2、队列中的消息是否持久化，默认消息是存储在内存中，持久化消息存储到磁盘
         * 3、该消息是否只供一个消费者消费，true代表可以共享，一般都是false
         * 4、是否自动删除，最后一个消费者端开链接后，true表示自动删除
         * 5、其他参数，延迟，死信
         */

        /**
         * 实现优先级队列
         * 1.队列需设置为优先级队列
         * 2.消息需设置优先级
         * 注： 消费者需等消息全部进入队列后再消费，消息才能进行优先级排队
         */

        Map<String,Object> arguments=new HashMap<>();
        // 官方允许0-255，超过范围报错，这里设置为10，允许为0-10，不建议设置太大，会浪费cpu和内存
        arguments.put("x-max-priority",10);
        channel.queueDeclare(QUEUE_NAME,true,false,false,arguments);


        // 发送一堆消息进行测试优先级队列，消息全部进行队列后在进行消费，按照优先级进行消费
        //
        for (int i = 1; i < 11 ; i++) {
            String message="info-" + i;
            if(i==5){ // 优先级构建
              AMQP.BasicProperties properties=new AMQP.BasicProperties()
                      .builder().priority(5).build();
                channel.basicPublish("",QUEUE_NAME,properties,message.getBytes());
            }else {
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            }
        }
        // 4、发送消息
        // String message="hello world";

        /**
         * 1、发送到哪个交换机
         * 2、路由的key值是哪个，本次是队列名称
         * 3、其他参数
         * 4、消息主体
         */
//        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
//        System.out.println("消息已发送");


    }


}
