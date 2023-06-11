package com.atguigu.rabbitmq.seven;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-11 -15:27
 * 生产者
 **/

public class EmitLogTopic {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        // 声明一个交换机
        /**
         * 1、交换机名称
         * 2、交换机类型： 4种类型
         */
        //channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);


        /**
         *
         * Q1-->绑定的是
         *  中间带 orange 带 3 个单词的字符串(*.orange.*)
         * Q2-->绑定的是
         * 最后一个单词是 rabbit 的 3 个单词(*.*.rabbit)
         * 第一个单词是 lazy 的多个单词(lazy.#)
         *
         *
         * 创建map 存储routingKey 和message
         *
         * quick.orange.rabbit 被队列 Q1Q2 接收到
         * lazy.orange.elephant 被队列 Q1Q2 接收到
         * quick.orange.fox 被队列 Q1 接收到
         * lazy.brown.fox 被队列 Q2 接收到
         * lazy.pink.rabbit 虽然满足两个绑定但只被队列 Q2 接收一次
         * quick.brown.fox 不匹配任何绑定不会被任何队列接收到会被丢弃
         * quick.orange.male.rabbit 是四个单词不匹配任何绑定会被丢弃
         * lazy.orange.male.rabbit 是四个单词但匹配 Q2
         *
         */
        Map<String, String> bindingMap = new HashMap<>();
        bindingMap.put("quick.orange.rabbit", "被队列 Q1Q2 接收到");
        bindingMap.put("lazy.orange.elephant", "被队列 Q1Q2 接收到");
        bindingMap.put("quick.orange.fox", "被队列 Q1 接收到");
        bindingMap.put("lazy.brown.fox", "被队列 Q2 接收到");
        bindingMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingMap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingMap.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定会被丢弃");
        bindingMap.put("lazy.orange.male.rabbit", "是四个单词但匹配 Q2");


        for (Map.Entry<String, String> bindingKey : bindingMap.entrySet()) {
            String routingKey = bindingKey.getKey();
            String message = bindingKey.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + message);
        }


    }

}
