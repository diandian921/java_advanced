/*
 * Ant Group
 * Copyright (c) 2004-2021 All Rights Reserved.
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 使用阻塞队列
 *
 * @author wuweihua
 * @version UseBlockingQueue.java, v 0.1 2021年07月12日 2:35 下午 wuweihua
 */
public class UseBlockingQueue {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);

        new Thread(() -> {
            try {
                queue.put(sum());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            int result = queue.take();
            // 确保  拿到result 并输出
            System.out.println("异步计算结果为：" + result);
            System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}