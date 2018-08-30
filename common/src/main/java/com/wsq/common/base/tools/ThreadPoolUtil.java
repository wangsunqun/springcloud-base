package com.wsq.common.base.tools;

import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池服务类
 */
public class ThreadPoolUtil {

    protected static Logger log = Logger.getLogger(ThreadPoolUtil.class);

    /**
     * 默认线程池大小
     */
    public static final int DEFAULT_POOL_SIZE = 100;

    public static final int DEFAULT_MAX_POOL_SIZE = 200;

    public static final int DEFAULT_QUEUE_SIZE = 500;

    private static volatile ExecutorService executorService;

    private static final ExecutorService destroyExecutorService = Executors.newSingleThreadExecutor();

    public static void load() {
        final ExecutorService tempExecutorService = executorService;

        int threadCount = DEFAULT_POOL_SIZE;
        int queueSize = DEFAULT_QUEUE_SIZE;
        int maxThreadCount = DEFAULT_MAX_POOL_SIZE;
        executorService = new ThreadPoolExecutor(threadCount, maxThreadCount, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));

        // 刷新后释放原线程池
        if (tempExecutorService != null) {
            destroyExecutorService.execute(() -> destoryExecutorService(tempExecutorService, 60000));

        }
    }


    public static void destoryExecutorService(ExecutorService executorService, long timeout) {
        if (executorService != null && !executorService.isShutdown()) {
            try {
                executorService.awaitTermination(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            executorService.shutdown();
        }
    }

    /**
     * 使用线程池中的线程来执行任务
     */
    public static void execute(Runnable task) {
        if (executorService == null) {
            load();
        }
        executorService.execute(task);
    }

    /**
     * 方法用途: 异步获取结果<br>
     * 实现步骤: <br>
     *
     * @param callable
     * @return
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        if (executorService == null) {
            load();
        }
        return executorService.submit(callable);
    }

}
