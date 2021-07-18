/*
 * Ant Group
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package week04;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 使用Future
 *
 * @author xiaowu
 * @version UseFuture.java, v 0.1 2021年07月12日 2:30 下午 xiaowu
 */
public class UseFuture {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Future<Integer> future = executorService.submit(UseFuture::sum);

        try {
            int result = future.get(); //这是得到的返回值

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