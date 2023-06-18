package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-18 -12:02
 *
 *
 * 死信队列 实战
 * 消费者1
 *
 **/

public class Consumer01 {

    // 普通交换机的名称
    public  static final  String NORMAL_EXCHANGE="normal_exchange";
    // 死信交换机名称
    public  static final  String DEAD_EXCHANGE="dead_exchange";
    // 普通队列名称
    public  static final  String NORMAL_QUEUE="normal_queue";
    // 死信队列名称
    public  static final  String DEAD_QUEUE="dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明队列
        Map<String, Object> arguments=new HashMap<>();
        // 过期时间 10s
        // 两种方式：1、在生产者发送消息指定。2、在
        // arguments.put("x-message-ttl",10*1000);
        // 正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key","lisi");
        // 设置正常队列的长度
//        arguments.put("x-max-length",6);



        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);

        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        // 绑定交换机
        // 绑定普通的队列和交换机
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        // 绑定死信队列和交换机
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        System.out.println("等待接收消息。。。。。。");
        // 接收消息的回调
        DeliverCallback deliverCallback=(consumerTag,message)->{
            String msg = new String(message.getBody(), "UTF-8");
            if(msg.equals("info5")){
                System.out.println("Consumer01等待接收消息：" + msg + "此消息是被C1拒绝的");
                // tag；false:不放回队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else{
                System.out.println("Consumer01等待接收消息：" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };

        // 取消消息消费的回调
        CancelCallback cancelCallback=(var1)->{

        };

        //  开启手动应答
        channel.basicConsume(NORMAL_QUEUE,false, deliverCallback, cancelCallback);


    }
}
