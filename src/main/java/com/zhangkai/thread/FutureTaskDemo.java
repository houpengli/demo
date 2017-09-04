package com.zhangkai.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by zhangkai on 2017/9/4.
 */
public class FutureTaskDemo {
    public static void main(String[] args) {
        Long start  = System.currentTimeMillis();

        ExecutorService exs = Executors.newFixedThreadPool(10);
        List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
        for (int i = 0; i < 10; i++) {
            FutureTask<Integer> futureTask = new FutureTask<Integer>(new CallableTask(i+1));
            exs.submit(futureTask);//Runnable特性
            futureList.add(futureTask);//Future特性
        }
        try {
            for (Future<Integer> future : futureList) {
                while (true) {//CPU高速轮询：每个future都并发轮循，判断完成状态然后获取结果，这一行，是本实现方案的精髓所在。即有10个future在高速轮询，完成一个future的获取结果，就关闭一个轮询
                    if (future.isDone() && !future.isCancelled()) {//获取future成功完成状态，如果想要限制每个任务的超时时间，取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可。
                        Integer i = future.get();//获取结果
                        System.out.println("任务i=" + i + "获取完成!" + new Date());
                        break;//当前future获取结果完毕，跳出while
                    } else {
                        Thread.sleep(1);//每次轮询休息1毫秒（CPU纳秒级），避免CPU高速轮循耗空CPU---》新手别忘记这个
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            exs.shutdown();
        }
        System.out.println("总耗时="+(System.currentTimeMillis()-start));

    }
}
