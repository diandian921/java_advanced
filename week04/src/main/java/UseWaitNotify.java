/*
 * Ant Group
 * Copyright (c) 2004-2021 All Rights Reserved.
 */

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 使用 synchronized wait/notify
 * @author xiaowu
 * @version UseWaitNotify.java, v 0.1 2021年07月12日 2:25 下午 xiaowu
 */
public class UseWaitNotify {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        Object lock = new Object();
        Callable<Integer> c = () -> {
            try {
                return sum();
            } catch (Exception e) {
                throw e;
            } finally {
                synchronized (lock) {
                    lock.notify();
                }
            }
        };

        FutureTask<Integer> futureTask = new FutureTask<>(c);
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            synchronized (lock) {
                lock.wait();
            }

            int result = futureTask.get(); //这是得到的返回值

            // 确保  拿到result 并输出
            System.out.println("异步计算结果为：" + result);

            System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // 然后退出main线程
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