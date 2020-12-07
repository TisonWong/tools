package com.ambit.app.process;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Ben Wong
 * @description: 线程处理类
 * @date: 2020-4-27 16:28:48
 */
public class ExecutorProcessPool {

//    private Logger logger = LogManager.getLogger(ExecutorProcessPool.class);

    private ExecutorService executorService;
    private static ExecutorProcessPool pool = new ExecutorProcessPool();
    private final int threadMax = 10;

    private ExecutorProcessPool(){
//        logger.info("threadMax : {}", threadMax);
        executorService = ExecutorServiceFactory.getInstance().createFixedThreadPool(threadMax);
    }

    public static ExecutorProcessPool getInstance(){
        return pool;
    }

    /**
     * @description: 关闭线程池, 这里要说明的是:
     *      调用关闭线程池方法后, 线程池会执行完队列中的所有任务才退出
     */
    public void shutdown(){
        executorService.shutdown();
    }

    /**
     * @description: 提交任务到线程池, 可以接收线程返回值
     * @param task 要执行的任务
     * @return 执行的结果
     */
    public Future<?> submit(Runnable task){
        return executorService.submit(task);
    }

    /**
     * @description: 提交任务到线程池, 可以接收线程返回值
     * @param task 要执行的任务
     * @return
     */
    public Future<?> submit(Callable<?> task){
        return executorService.submit(task);
    }

    /**
     * @description: 直接提交任务到线程池, 无返回值
     * @param task
     */
    public void execute(Runnable task){
        executorService.execute(task);
    }

}
