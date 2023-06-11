package com.atguigu.rabbitmq.utils;

/**
 * @author wrmeng
 * @create 2023-06-05 -22:48
 *
 *
 **/

public class SleepUtils {

    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}