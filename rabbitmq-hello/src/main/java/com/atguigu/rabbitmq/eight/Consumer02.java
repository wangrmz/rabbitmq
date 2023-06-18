package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-18 -12:02
 *
 *
 * 死信队列 实战
 * 消费者1
 *
 **/

public class Consumer02 {

    // 普通交换机的名称
    public  static final  String NORMAL_EXCHANGE="normal_exchange";
    // 死信交换机名称
    public  static final  String DEAD_EXCHANGE="dead_exchange";
    // 普通队列名称
    public  static final  String NORMAL_QUEUE="normal_queue";
    // 死信队列名称
    public  static final  String DEAD_QUEUE="dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();


        System.out.println("等待接收消息。。。。。。");
        // 接收消息的回调
        DeliverCallback deliverCallback=(consumerTag,message)->{
            String msg = new String(message.getBody(), "UTF-8");
            System.out.println("Consumer02等待接收消息：" + msg);
        };

        // 取消消息消费的回调
        CancelCallback cancelCallback=(var1)->{

        };

        //
        channel.basicConsume(DEAD_QUEUE,true, deliverCallback, cancelCallback);


    }
}
