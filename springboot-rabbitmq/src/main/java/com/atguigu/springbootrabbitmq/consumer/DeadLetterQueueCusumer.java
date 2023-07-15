package com.atguigu.springbootrabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import java.util.Date;


/**
 * @author wrmeng
 * @create 2023-07-10 -10:00
 * 消费者:通过监听的方式
 **/

@Component
@Slf4j
public class DeadLetterQueueCusumer {

    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel){
        //
        String msg = new String(message.getBody());

        // 占位符替换
        log.info("当前时间：{},收到死信队列的消息：{}",new Date().toString(),message);


    }



}
