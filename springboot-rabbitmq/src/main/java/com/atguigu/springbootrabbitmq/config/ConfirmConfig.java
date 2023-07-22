package com.atguigu.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wrmeng
 * @create 2023-07-15 -16:14
 * 发布 确认 高级
 *
 **/

@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    public static final String CONFIRM_ROUNTINGKEY = "confirm.key";

    // 备份交换机
    /**
     * mandatory 参数与备份交换机可以一起使用的时候，如果两者同时开启，消息究竟何去何从？
     * 谁优先级高，答案是备份交换机优先级高
     */
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    // 备份队列
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    // 报警队列
    public static final String WARNING_QUEUE_NAME = "warning.queue";


    //声明业务 Exchange
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){

        // 需要转发到备份交换机alternate-exchange
        return  ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true).
                withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();

        //return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean("warningQueue")
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    @Bean("backupQueue")
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    // 声明确认队列
    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }
    // 声明确认队列绑定关系
    @Bean
    public Binding queueBinding(@Qualifier("confirmQueue") Queue queue,
                                @Qualifier("confirmExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUNTINGKEY);
    }

    @Bean
    public Binding queueBindingWarning(@Qualifier("warningQueue") Queue queue,
                                @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding queueBindingbackup(@Qualifier("backupQueue") Queue queue,
                                       @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }





}
