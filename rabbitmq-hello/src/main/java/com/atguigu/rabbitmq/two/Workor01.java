package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-04 -18:28
 * 这是一个工作线程（相当于之前消费者）
 *
 * 注意事项：一个消息只能被处理一次，不能处理多次
 * 工作线程之间是竞争关系，采取轮训的方式
 **/

public class Workor01 {

    public static final String QUEUE_NAME = "hello";


    /**
     * 接受消息
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        //声明，接受消息
        DeliverCallback deliverCallback = (var1, var2) -> {
            System.out.println("接收到的消息：" + new String(var2.getBody()));
        };

        //  声明取消消费消息的回调
        CancelCallback cancelCallback = var -> {
            System.out.println(var + "消费者取消消费接口回调逻辑");
        };

        System.out.println("C2等待接收消息。。。。。。");
        // 消息的接受
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }


}
