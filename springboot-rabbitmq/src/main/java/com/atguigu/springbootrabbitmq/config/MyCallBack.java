package com.atguigu.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author wrmeng
 * @create 2023-07-17 -22:20
 **/

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    //RabbitTemplate.ConfirmCallback 是一个内部接口


    @Autowired
    private RabbitTemplate rabbitTemplate;


    @PostConstruct// 在其他注解完成之后才会执行
    public void init(){
        //  注入
        rabbitTemplate.setConfirmCallback(this);
        // 注入
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 交换机不管是否收到消息的一个回调方法
     * CorrelationData
     * 消息相关数据
     * ack
     * 交换机是否收到消息
     * <p>
     * 1、发消息 交换机接收到了 回调
     * 1.1、 correlationData 保存回调消息的ID 以及相关信息
     * 1.2、交换机收到消息 ack = true
     * 1.3、cause null
     * 2、发消息 交换机接收失败了 回调
     * 2.1、 correlationData 保存回调消息的ID 以及相关信息
     * 2.2 交换机收到消息 ack = false
     * 2.3、 cause 失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id="";
        if(correlationData!=null){
            id = correlationData.getId();
        }

        if (ack == true) {
            log.info("交换机已经收到Id为：{} 的消息", id);
        } else {
            log.info("交换机未收到Id为：{} 的消息，由于原因：{}", id, cause);
        }

    }


    /**
     * 方法中的参数不显示具体含义，download 源码
     *  消息的回退接口
     *  路由失败才会调用：可以在当消息传递过程中不可达目的地时将消息返回给生产者
     * @param message the returned message.
     * @param replyCode the reply code.
     * @param replyText the reply text.
     * @param exchange the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

        log.error("消息{},被交换机{} 退回，退回原因：{},路由key:{}",new String(message.getBody()),exchange,replyText,routingKey);
    }
}
