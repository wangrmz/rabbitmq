package com.atguigu.springbootrabbitmq.controller;

import com.atguigu.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wrmeng
 * @create 2023-07-15 -16:58
 *
 * 开始发消息  测试确认
 *
 **/

@RestController
@Slf4j
@RequestMapping("/confirm")
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 发消息
    @GetMapping("/sendMessage/{message}")
    public  void sendMessage(@PathVariable String message){
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUNTINGKEY,message);
        log.info("发送消息内容：{}",message);
    }

    // 发消息
    @GetMapping("/sendMessageNotGood/{message}")
    public  void sendMessageNotGood(@PathVariable String message){
        CorrelationData correlationData1 = new CorrelationData("1");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME ,ConfirmConfig.CONFIRM_ROUNTINGKEY,message,correlationData1);
        log.info("发送消息内容：{}",message,correlationData1);

        // 队列收不到消息：叫做不可路由 routinkey
        /**
         * 在仅开启了生产者确认机制的情况下，交换机接收到消息后，会直接给消息生产者发送确认消息，如
         * 果发现该消息不可路由，那么消息会被直接丢弃，此时生产者是不知道消息被丢弃这个事件的。那么如何
         * 让无法被路由的消息帮我想办法处理一下？最起码通知我一声，我好自己处理啊。通过设置 mandatory
         * 参数可以在当消息传递过程中不可达目的地时将消息返回给生产者。
         */
        CorrelationData correlationData2 = new CorrelationData("2");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME ,ConfirmConfig.CONFIRM_ROUNTINGKEY+"2",message,correlationData2);
        log.info("发送消息内容：{}",message,correlationData2);

    }


}
