package com.ucash_test.lirongyunindialoan.myosotisutils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {
    private static final ExecutorService executor = null;
    private ThreadPoolUtils(){

    }
    public static ExecutorService get(ThreadPoolType type){
        if(executor == null){
            if(type == ThreadPoolType.SINGLE){
                // 1. 创建单线程化线程池
                ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
                // 2. 创建好Runnable类线程对象 & 需执行的任务
                Runnable task =new Runnable(){
                    public void run(){
                        System.out.println("执行任务啦");
                    }
                };
                // 3. 向线程池提交任务：execute（）
                singleThreadExecutor.execute(task);
                // 4. 关闭线程池
                singleThreadExecutor.shutdown();
                return Executors.newSingleThreadExecutor();
            }
            else if(type == ThreadPoolType.SCHEDULED){
                // 1. 创建 定时线程池对象 & 设置线程池线程数量固定为5
                ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
                // 2. 创建好Runnable类线程对象 & 需执行的任务
                Runnable task =new Runnable(){
                    public void run(){
                        System.out.println("执行任务啦");
                    }
                };
                // 3. 向线程池提交任务：schedule（）
                scheduledThreadPool.schedule(task, 1, TimeUnit.SECONDS); // 延迟1s后执行任务
                scheduledThreadPool.scheduleAtFixedRate(task,10,1000,TimeUnit.MILLISECONDS);// 延迟10ms后、每隔1000ms执行任务
                // 4. 关闭线程池
                scheduledThreadPool.shutdown();
                return Executors.newScheduledThreadPool(2);
            }
            else if(type == ThreadPoolType.CACHED){
                // 1. 创建可缓存线程池对象
                ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
                // 2. 创建好Runnable类线程对象 & 需执行的任务
                Runnable task =new Runnable(){
                    public void run(){
                        System.out.println("执行任务啦");
                    }
                };
                // 3. 向线程池提交任务：execute（）
                cachedThreadPool.execute(task);
                // 4. 关闭线程池
                cachedThreadPool.shutdown();
                return Executors.newCachedThreadPool();
            }
            else{
                ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
                // 2. 创建好Runnable类线程对象 & 需执行的任务
                Runnable task =new Runnable(){
                    public void run(){
                        System.out.println("执行任务啦");
                    }
                };
                // 3. 向线程池提交任务：execute（）
                fixedThreadPool.execute(task);
                // 4. 关闭线程池
                fixedThreadPool.shutdown();
                return Executors.newFixedThreadPool(6);
            }
        }
        else {
            return executor;
        }
    }
}
