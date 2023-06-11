package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-05 -21:49
 *
 * 生产者，发送大量消息
 **/

public class Task01 {

    // 队列名称
    public  static final  String QUEUE_NAME="hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();

        //队列的声明
        // 3、生成一个队列
        /**
         * 1、队列名称
         * 2、队列中的消息是否持久化，默认消息是存储在内存中，持久化消息存储到磁盘
         * 3、该消息是否只供一个消费者消费，true代表可以共享，一般都是false
         * 4、是否自动删除，最后一个消费者端开链接后，true表示自动删除
         * 5、其他参数，延迟，死信
         */

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        // 控制台输入
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String message = scanner.next();
            // 默认交换机，路由，其他参数
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        }



    }

}
