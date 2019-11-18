package cn.edu.ruc.concurrent.threadPool.core;

import cn.edu.ruc.concurrent.threadPool.interfaces.RunnableQueue;

/**
 * @ClassName InnerTaskCosumer
 * @Description: 对传入的任务队列进行消费分配的过程
 * @Author rainmaple
 * @Date 2019/11/14
 * @Version V1.0
 **/
public class InnerTaskCosumer implements Runnable {
    private final RunnableQueue runnableQueue;
    private volatile boolean running = true;

    public InnerTaskCosumer(RunnableQueue runnableQueue) {
        this.runnableQueue = runnableQueue;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            Runnable task = null;
            try {
                task = runnableQueue.take();
                task.run();
            } catch (InterruptedException e) {
                running = false;
                break;
            }

        }
    }
    public void stop(){
        this.running = false;
    }
}
