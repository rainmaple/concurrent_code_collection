package cn.edu.ruc.concurrent.threadPool;

import cn.edu.ruc.concurrent.threadPool.core.InnerTaskCosumer;
import cn.edu.ruc.concurrent.threadPool.core.LinkedRunnableQueue;
import cn.edu.ruc.concurrent.threadPool.interfaces.DennyPolicy;
import cn.edu.ruc.concurrent.threadPool.interfaces.RunnableQueue;
import cn.edu.ruc.concurrent.threadPool.interfaces.ThreadFactory;
import cn.edu.ruc.concurrent.threadPool.interfaces.ThreadPool;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName BasicThreadPool
 * @Description: 基础的线程池实现
 * @Author rainmaple
 * @Date 2019/11/14 
 * @Version V1.0
 **/
public class BasicThreadPool extends Thread implements ThreadPool {
    private final int initSize;
    private final int maxSize;
    private final int coreSize;
    private int activeCount;

    private final ThreadFactory threadFactory;

    private final RunnableQueue runnableQueue;
    private volatile boolean isShutdown = false;
    private Queue<ThreadTask> threadTaskQueue = new ArrayDeque<>();

    private final static DennyPolicy DEFAULT_DENNY_POLICY = new DennyPolicy.DiscardDenyPolicy();
    private final static ThreadFactory DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();

    private final long keepAliveTime ;
    private final TimeUnit timeUnit;

    public BasicThreadPool(int initSize, int maxSize, int coreSize, int queueSize){
        this(initSize,maxSize,coreSize,DEFAULT_THREAD_FACTORY,queueSize,DEFAULT_DENNY_POLICY,10, TimeUnit.SECONDS);
    }
    public BasicThreadPool(int initSize, int maxSize, int coreSize,ThreadFactory threadFactory,int queueSize,
                           DennyPolicy dennyPolicy, long keepAliveTime, TimeUnit timeUnit){
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.coreSize = coreSize;
        this.threadFactory = threadFactory;
        this.runnableQueue = new LinkedRunnableQueue(queueSize,dennyPolicy,this);

        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.init();
    }
    public void init(){
        start();
        for(int i = 0; i < initSize; i++){
            newThread();
        }
    }
    //创建任务线程并且启动
    private void newThread(){
        InnerTaskCosumer innerTaskCosumer = new InnerTaskCosumer(runnableQueue);
        Thread thread = this.threadFactory.createThread(innerTaskCosumer);
        ThreadTask threadTask = new ThreadTask(thread,innerTaskCosumer);
        threadTaskQueue.offer(threadTask);
        this.activeCount++;
        thread.start();
    }
    private void removeThread(){
        //从线程池中移除某个线程
        ThreadTask threadTask = threadTaskQueue.remove();
        threadTask.innerTaskCosumer.stop();
        this.activeCount--;
    }

    @Override
    public void execute(Runnable runnable) {
        if(this.isShutdown){
            throw new IllegalStateException("this thread pool has been destroyed");
        }
        this.runnableQueue.offer(runnable);
    }

    @Override
    public void shutdown() {
        //同步，避免和线程池本身维护的线程冲突
        synchronized (this){
            if(isShutdown) return;
            isShutdown = true;
            threadTaskQueue.forEach(threadTask -> {
                threadTask.innerTaskCosumer.stop();
                threadTask.thread.interrupt();
            });
            this.interrupt();
        }

    }

    @Override
    public int getInitSize() {
        if(isShutdown)
            throw new IllegalStateException("the thread pool have been destroyed");
        return this.initSize;
    }

    @Override
    public int getMaxSize() {
        if(isShutdown)
            throw new IllegalStateException("the thread pool have been destroyed");
        return this.maxSize;
    }

    @Override
    public int getCoreSize() {
        if(isShutdown)
            throw new IllegalStateException("the thread pool have been destroyed");
        return this.coreSize;
    }

    @Override
    public int getQueueSize() {
        if(isShutdown)
            throw new IllegalStateException("the thread pool have been destroyed");
        return runnableQueue.size();
    }

    @Override
    public int getActiveCount() {
        synchronized (this){
            return this.activeCount;
        }
    }

    @Override
    public boolean isShutDown() {
        return this.isShutdown;
    }
    @Override
    public void run(){
        //维护线程数量、扩容、回收
        while(!isShutdown && !isInterrupted()){
            try{
                timeUnit.sleep(keepAliveTime);
            }catch (InterruptedException e){
                isShutdown = true;
                break;
            }
            synchronized (this){
                if(isShutdown) break;
                //任务有，但是活跃线程不足够提供服务--> 扩容
                if(runnableQueue.size() > 0 && activeCount < coreSize){
                    for(int i = initSize; i < coreSize ; i++){
                        newThread();
                    }
                    continue;
                }

                //进一步扩容
                if(runnableQueue.size() > 0 && activeCount < maxSize){
                    for(int i = coreSize; i < maxSize; i++){
                        newThread();
                    }
                }
                //任务队列没有任务，需要回收线程
                if(runnableQueue.size() == 0 && activeCount > coreSize){
                    for(int i = coreSize; i < activeCount; i++){
                        removeThread();
                    }
                }
            }
        }
    }

//将线程同线程消费者建立绑定形成一个整体
    private static class ThreadTask{
        Thread thread;
        InnerTaskCosumer innerTaskCosumer;
        public ThreadTask(Thread thread, InnerTaskCosumer innerTaskCosumer){
            this.thread = thread;
            this.innerTaskCosumer = innerTaskCosumer;
        }
    }
    private static class DefaultThreadFactory implements ThreadFactory{
        private static final AtomicInteger GROUP_COUNTER = new AtomicInteger(1);
        private static final ThreadGroup group = new ThreadGroup("Rainmaple_simpleThreadPool-"
                + GROUP_COUNTER.getAndDecrement());
        private static final AtomicInteger COUNTER = new AtomicInteger(0);

        @Override
        public Thread createThread(Runnable runnable) {
            return new Thread(group,runnable,"thread-pool-" + COUNTER.getAndDecrement());
        }
    }
}
