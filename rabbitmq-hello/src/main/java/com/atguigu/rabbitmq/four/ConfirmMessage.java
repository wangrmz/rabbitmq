package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author wrmeng
 * @create 2023-06-10 -21:13
 * <p>
 * 发布确认模式
 * 使用的时间 比较哪种确认方式是最好的
 * 1、单个确认
 * 2、批量确认
 * 3、异步批量确认
 **/

public class ConfirmMessage {

    /**
     * 批量发消息的个数
     */
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
//        1、单个确认:发布速度非常慢，同步的
//        ConfirmMessage.publishMessageIndividually();//发布1000个单独确认消息，耗时176ms
//        2、批量确认：
        /**
         * 先发布一批消息然后一起确认可以极大地提高吞吐量
         * 当然这种方式的缺点就是:当发生故障导致发布出现问题时，不知道是哪个消息出现
         * 问题了，我们必须将整个批处理保存在内存中，以记录重要的信息而后重新发布消息。当然这种
         * 方案仍然是同步的，也一样阻塞消息的发布。
         */
//        ConfirmMessage.publishMessageBatch();// 发布1000个批量确认消息，耗时37ms

//        3、异步批量确认
        ConfirmMessage.publishMessageAsync();// 发布1000个异步确认消息，耗时17ms

    }

    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        for (int i = 1; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");

    }

    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息大小
        int batchSize = 100;
        for (int i = 1; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

            // 判断达到100条消息的时候，批量确认一次
            if (i % batchSize == 0) {
                // 发布确认
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("消息发送成功");
                }
            }

        }

        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
    }

    /**
     * 异步确认虽然编程逻辑比上两个要复杂，但是性价比最高，无论是可靠性还是效率都没得说，
     * 他是利用回调函数来达到消息可靠性传递的，这个中间件也是通过函数回调来保证是否投递成功，
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void publishMessageAsync() throws IOException, TimeoutException, InterruptedException {

        Channel channel = RabbitMqUtils.getChannel();
        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        /**
         * 确认收到，消息确认成功回调函数
         */
        ConfirmCallback ackCallback = (e1, e2) -> {
            System.out.println("确认的消息：" + e1);
        };

        /**
         * 确认未收到 消息确认失败回调函数
         * 参数说明
         * 1、消息的标记
         * 2、是否未批量确认
         */
        ConfirmCallback nackCallback = (e1, e2) -> {
            //记录
            System.out.println("未确认的消息：" + e1);
        };

        // 准备消息的监听器 监听那些消息成功，那些失败？
        channel.addConfirmListener(ackCallback, nackCallback); //异步

        for (int i = 1; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
        }

        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时" + (end - begin) + "ms");

    }


}
