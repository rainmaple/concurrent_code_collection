package cn.edu.ruc.concurrent.threadPool.interfaces;

/**
 * @program: Ij_javatest
 * @description: 任务队列接口
 * @author: rainmaple
 * @date: 2019-11-14 21:04
 **/
public interface RunnableQueue {
    void offer(Runnable runnable);
    Runnable take() throws InterruptedException;
    int size();
}
