package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-05 -22:30
 *
 * 消息在手动应答时不丢失，放回队列中重新消费
 *
 **/

public class Task2 {

    public  static  final String TASK_QUEUE_NAME="ack_name_new";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        // 开启发布确认
        channel.confirmSelect();// 确认发布有3种：单个，批量，异步

        // 声明一个队列
        // 让消息队持久化，队列持久化
        // 区分消息持久化   和  消息队列持久化
        // 如果队列不持久化，消息持久化，mq重启的话，消息依然会丢失，只是队列仍然存在
        // 1，2设置后依然会出现消息的丢失
        // 1、队列持久化
        // 2、消息持久化
        // 3、发布确认：保存到磁盘上之后，mq通知消费者消息已经保存到磁盘上
        // 只有3也完成才能确保消息不丢失

        boolean durable=true;
        // 队列持久化
        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);
        // 控制台输入
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String message = scanner.next();
            // 默认交换机，路由，其他参数
            // MessageProperties.PERSISTENT_TEXT_PLAIN:设置生产者发送消息为持久化，保存在磁盘中，不设置消息就是存储在内存中
            // 消息持久化
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }



    }
}
