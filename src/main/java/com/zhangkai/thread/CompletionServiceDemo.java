package com.zhangkai.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by zhangkai on 2017/9/4.
 */
public class CompletionServiceDemo {
    public static void main(String[] args) {
        Long start  = System.currentTimeMillis();

        ExecutorService exs = Executors.newFixedThreadPool(10);
        CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(exs);

        List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
        for (int i = 0; i < 10; i++) {
            futureList.add(completionService.submit(new CallableTask(i+1)));
        }
        try {
            for (Future<Integer> future : futureList) {
                Future<Integer> endFuture = completionService.take();
                Integer result = endFuture.get();//采用completionService.take()，内部维护阻塞队列，任务先完成的先获取到
                System.out.println("任务i=="+result+"完成!"+new Date());
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
