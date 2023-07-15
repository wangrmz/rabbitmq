package com.atguigu.springbootrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author wrmeng
 * @create 2023-07-03 -22:35
 **/

@RestController
@RequestMapping("/ttl")
@Slf4j
public class SendMessageController {

    /**
     * spring 公司提供
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 开始发消息
    @GetMapping("/sendMessage/{message}")
    public  void send(@PathVariable("message") String message){
        // 前面的{}为占位符
        log.info("当前时间：{},发送一条消息给两个TTL队列：{}",new Date().toString(),message);
        // Message ms =  new Message(message.getBytes(StandardCharsets.UTF_8), MessageProperties.);
        // rabbitTemplate.send("X","XA","消息来自于ttl为10s的队列：" + message);
        rabbitTemplate.convertAndSend("X","XA","消息来自于ttl 为10s的队列" + message);
        rabbitTemplate.convertAndSend("X","XB","消息来自于ttl 为40s的队列" + message);
        //

    }


    /**
     * 基于死信队列的问题
     * 发起请求
     * http://localhost:8080/ttl/sendExpirationMsg/你好 1/20000
     * http://localhost:8080/ttl/sendExpirationMsg/你好 2/2000
     *
     *
     * 如果使用在消息属性上设置 TTL 的方式，消
     * 息可能并不会按时“死亡“，因为 RabbitMQ 只会检查第一个消息是否过期，如果过期则丢到死信队列，
     * 如果第一个消息的延时时长很长，而第二个消息的延时时长很短，第二个消息并不会优先得到执行。
     *
     * @param message
     * @param ttlTime
     */
    // 开始发消息
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public  void sendExpirationMsg(@PathVariable("message") String message,@PathVariable("ttlTime") String ttlTime){
        // 前面的{}为占位符
        log.info("当前时间：{},发送一条消息时长{}毫秒TTL信息给队列QC：{}",new Date().toString(),ttlTime,message);
        rabbitTemplate.convertAndSend("X","XC","消息来自于ttl 为10s的队列" + message,msg->{
            // 发送消息的时候 延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });


    }





}
