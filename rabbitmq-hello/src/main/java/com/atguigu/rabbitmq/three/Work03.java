package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.atguigu.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-05 -22:37
 *
 * 手动应答的好处是可以批量应答并且减少网络拥堵
 *
 *   消息自动重新入队 -mq 确保消息不丢失的机制
 *
 **/

public class Work03 {
    public  static  final String TASK_QUEUE_NAME="ack_name_new";

    /**
     *
     * A.Channel.basicAck(用于肯定确认)
     * RabbitMQ 已知道该消息并且成功的处理消息，可以将其丢弃了
     * B.Channel.basicNack(用于否定确认) C.Channel.basicReject(用于否定确认) 与 Channel.basicNack 相比少一个参数
     * 不处理该消息了直接拒绝，可以将其丢弃了
     *
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1等到接收消息处理时间较短");

        //采取手动应答
        DeliverCallback deliverCallback=( var1,  var2)->{
            //沉睡1s
            SleepUtils.sleep(1);
            System.out.println("接收到的消息：" + new String(var2.getBody(),"UTF-8"));

            // 手动应答
            /**
             * 1、消息的标记 tag
             * 2、是否批量应答 false:不批量应答信道中的消息
             */
            channel.basicAck(var2.getEnvelope().getDeliveryTag(),false);
        };


        //  声明取消消费消息的回调
        CancelCallback cancelCallback = var -> {
            System.out.println("消息消费被中断");
        };

        // 设置不公平分发 非常常用 消费方设置
//        int prefetchCount=1;
        // 欲取值是2
        int prefetchCount=2;

        // 0 默认是轮训方式
        channel.basicQos(prefetchCount);
        boolean autoAck=false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);


    }
}

