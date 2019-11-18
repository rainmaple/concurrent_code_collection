package cn.edu.ruc.concurrent.threadPool.interfaces;

/**
 * @program: Ij_javatest
 * @description: 线程池接口
 * @author: rainmaple
 * @date: 2019-11-14 21:01
 **/
public interface ThreadPool {
    void execute(Runnable runnable);
    void shutdown();
    int getInitSize();
    int getMaxSize();
    int getCoreSize();
    int getQueueSize();
    int getActiveCount();
    boolean isShutDown();
}
