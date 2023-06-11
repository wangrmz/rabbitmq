package com.atguigu.rabbitmq.seven;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-11 -15:14
 **/

public class ReceiveLogsTopic02 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        // 声明一个交换机
        /**
         * 1、交换机名称
         * 2、交换机类型： 4种类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 声明一个队列.  这里使用临时队列
        /**
         * 生成一个临时队列，队列名称是随机的，
         * 当消费者断开与队列的链接的时候 队列会被删除，队列没有持久化
         */
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);

        // 绑定交换机
        // 1、队列名称  2、交换机名称 3、routingKey
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");

        System.out.println("等待接收消息......");
        // 接收消息成功回调
        DeliverCallback deliverCallback = (e1, e2) -> {
            System.out.println("接收队列：" + queueName + "绑定键："+ e2.getEnvelope().getRoutingKey() + " 接收到的消息为： " + new String(e2.getBody(), "UTF-8"));
        };
        // 取消消费回调
        CancelCallback cancelCallback = (e) -> {

        };

        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);

    }
}
