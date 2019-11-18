package cn.edu.ruc.concurrent.threadPool.core;

import cn.edu.ruc.concurrent.threadPool.interfaces.DennyPolicy;
import cn.edu.ruc.concurrent.threadPool.interfaces.RunnableQueue;
import cn.edu.ruc.concurrent.threadPool.interfaces.ThreadPool;

import java.util.LinkedList;

/**
 * @ClassName LinkedRunnableQueue
 * @Description: TODO
 * @Author rainmaple
 * @Date 2019/11/14 
 * @Version V1.0
 **/
public class LinkedRunnableQueue implements RunnableQueue {
    //任务队列最大容量
    private final int limit;
    //拒绝策略
    private final DennyPolicy dennyPolicy;
    //存放任务的队列
    private final LinkedList<Runnable> runnableLinkedList = new LinkedList<>();

    private final ThreadPool threadPool;

    public LinkedRunnableQueue(int limit,DennyPolicy denyPolicy,ThreadPool threadPool){
        this.limit = limit;
        this.dennyPolicy = denyPolicy;
        this.threadPool = threadPool;
    }

    @Override
    public void offer(Runnable runnable) {
        synchronized (runnableLinkedList){
            if(runnableLinkedList.size() >= limit){
                dennyPolicy.reject(runnable,threadPool);
            }else{
                runnableLinkedList.addLast(runnable);
                runnableLinkedList.notifyAll();
            }
        }
    }

    @Override
    public Runnable take() throws InterruptedException {
        synchronized (runnableLinkedList){
            while (runnableLinkedList.isEmpty()){
                try{
                    runnableLinkedList.wait();
                }catch(InterruptedException e){
                    throw e;
                }
            }
            return runnableLinkedList.removeFirst();
        }
    }

    @Override
    public int size() {
        synchronized (runnableLinkedList){
            return runnableLinkedList.size();
        }
    }
}
