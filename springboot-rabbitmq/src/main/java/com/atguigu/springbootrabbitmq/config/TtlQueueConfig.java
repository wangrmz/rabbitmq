package com.atguigu.springbootrabbitmq.config;

import org.springframework.amqp.core.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wrmeng
 * @create 2023-07-03 -21:57
 *
 * 延迟队列是死信队列的一种
 * 死信队列：三种情况
 * 1、
 * 2、设置了ttl,过期则成为死信
 * 3、
 *
 *
 **/
// 配置类，注解 注册
@Configuration
public class TtlQueueConfig {
    // 普通交换机
    public static final String X_EXCHANGE = "X";
    // 普通队列
    public static final String QUEUE_A = "QA";
    // 普通队列
    public static final String QUEUE_B = "QB";

    // 普通队列 绑定普通交换机
    public static final String QUEUE_C = "QC";

    // 死信交换机
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    // 死信队列
    public static final String DEAD_LETTER_QUEUE = "QD";

    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    // 声明普通队列 TTL 10s
    @Bean("queueA")
    public Queue queueA(){
        Map<String,Object> arguments= new HashMap<>(3);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        // 设置死信RountingKey
        arguments.put("x-dead-letter-routing-key","YD");
        // 设置 TTL 10s
        arguments.put("x-message-ttl",10*1000);

        return  QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }
    // 声明普通队列 TTL 10s
    @Bean("queueB")
    public Queue queueB(){
        Map<String,Object> arguments= new HashMap<>(3);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        // 设置死信RountingKey
        arguments.put("x-dead-letter-routing-key","YD");
        // 设置 TTL 10s
        arguments.put("x-message-ttl",40*1000);

        return  QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    @Bean("queueC")
    public Queue queueC(){
        Map<String,Object> arguments= new HashMap<>(3);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        // 设置死信RountingKey
        arguments.put("x-dead-letter-routing-key","YD");
        // 没有声明TTL属性

        return  QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    // 声明死信队列
    @Bean("queueD")
    public Queue queueD(){
        return  QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    // 绑定
    /**
     *
     * 通过将 @Qualifier 注解与我们想要使用的特定 Spring bean 的名称一起进行装配，
     * Spring 框架就能从多个相同类型并满足装配要求的 bean 中找到我们想要的
     * 解决依赖冲突
     *
     * @param queueA
     * @param xExchange
     * @return
     */
    @Bean
    public Binding queueABingX(@Qualifier("queueA") Queue queueA,
                               @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    public Binding queueBBingX(@Qualifier("queueB") Queue queueB,
                               @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean // 队列C 绑定普通交换机
    public Binding queueCBingX(@Qualifier("queueC") Queue queueB,
                               @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XC");
    }

    /**
     * 队列D 绑定死信交换机
     * @param
     * @param yExchange
     * @return
     */
    @Bean
    public Binding queueABingY(@Qualifier("queueD") Queue queueD,
                               @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }



}
