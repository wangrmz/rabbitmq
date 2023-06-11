package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-11 -11:40
 **/

public class ReceiveLog1 {

    public static  final  String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        // 声明一个交换机
        /**
         * 1、交换机名称
         * 2、交换机类型： 4种类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        // 声明一个队列.  这里使用临时队列
        /**
         * 生成一个临时队列，队列名称是随机的，
         * 当消费者断开与队列的链接的时候 队列会被删除，队列没有持久化
         */
        String queueName = channel.queueDeclare().getQueue();

        // 绑定交换机
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("等待接收消息");
        // 接收消息成功回调

        DeliverCallback deliverCallback=(e1,e2)->{
            System.out.println("ReceiveLog1控制台打印接收到的消息：" + new String(e2.getBody(),"UTF-8"));
        };
        // 取消消费回调
        CancelCallback cancelCallback=(e)->{

        };

        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);

    }
}
