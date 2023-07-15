package com.atguigu.springbootrabbitmq.consumer;

import com.atguigu.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wrmeng
 * @create 2023-07-15 -15:50
 **/

/**
 * mq比较全面
 * 延时队列在需要延时处理的场景下非常有用，使用 RabbitMQ 来实现延时队列可以很好的利用
 * RabbitMQ 的特性，如：消息可靠发送、消息可靠投递、死信队列来保障消息至少被消费一次以及未被正
 * 确处理的消息不会被丢弃。另外，通过 RabbitMQ 集群的特性，可以很好的解决单点故障问题，不会因为
 * 单个节点挂掉导致延时队列不可用或者消息丢失。
 *
 *
 * 当然，延时队列还有很多其它选择，比如利用 Java 的 DelayQueue，利用 Redis 的 zset，利用 Quartz
 * 或者利用 kafka 的时间轮，这些方式各有特点,看需要适用的场景
 *
 */
@Component
@Slf4j
public class DelayQueueConsumer {


    /**
     * http://localhost:8080/ttl/sendDelayMsg/come on baby1/20000
     * http://localhost:8080/ttl/sendDelayMsg/come on baby2/2000
     *
     *
     * 结果：符合预期
     * 2023-07-15 15:56:22.994  INFO 79321 --- [nio-8080-exec-1] c.a.s.controller.SendMessageController   : 当前时间：Sat Jul 15 15:56:22 CST 2023,发送一条消息时长20000毫秒信息给延迟队列delayed.queue：come on baby1
     * 2023-07-15 15:56:31.982  INFO 79321 --- [nio-8080-exec-2] c.a.s.controller.SendMessageController   : 当前时间：Sat Jul 15 15:56:31 CST 2023,发送一条消息时长2000毫秒信息给延迟队列delayed.queue：come on baby2
     * 2023-07-15 15:56:33.985  INFO 79321 --- [ntContainer#1-1] c.a.s.consumer.DelayQueueConsumer        : 当前时间：Sat Jul 15 15:56:33 CST 2023,收到延迟队列的消息：come on baby2
     * 2023-07-15 15:56:42.838  INFO 79321 --- [ntContainer#1-1] c.a.s.consumer.DelayQueueConsumer        : 当前时间：Sat Jul 15 15:56:42 CST 2023,收到延迟队列的消息：come on baby1
     * @param message
     */
    // 监听信息
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message){
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到延迟队列的消息：{}",new Date().toString(),msg);
    }


}
