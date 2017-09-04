package com.zhangkai.thread;

import java.util.concurrent.Callable;

/**
 * Created by zhangkai on 2017/9/4.
 */
public class CallableTask implements Callable<Integer> {
    private Integer result;

    public CallableTask(Integer param) {
        this.result = param;
    }

    @Override
    public Integer call() throws Exception {
        if(result==1){
Thread.sleep(3000);//任务1耗时3秒
}else if(result==5){
Thread.sleep(5000);//任务5耗时5秒
 }else{
Thread.sleep(1000);//其它任务耗时1秒
}
System.out.println("task线程："+Thread.currentThread().getName()+"任务i="+result+",完成！");
        return result;
    }
}
