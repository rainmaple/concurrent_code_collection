package cn.edu.iir.utils.lock;

import cn.edu.iir.utils.Lock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.currentThread;

/**
 * @ClassName BooleanLock
 * @Description: 显式锁，可以中断被阻塞的线程
 * @Author rainmaple
 * @Date 2019/11/14 
 * @Version V1.0
 **/
public class BooleanLock implements Lock {
    private Thread currentThread;
    private boolean locked = false;
    private final List<Thread> blockedList = new ArrayList<>();
    @Override
    public void lock() throws InterruptedException {
        synchronized (this){
            //当前锁被其他线程获取，则将当前线程加入阻塞队列，并wait释放对this的lock
            while(locked){
                final Thread tempThread = currentThread();
                try {
                    if(!blockedList.contains(tempThread))
                     {
                        blockedList.add(currentThread());
                     }
                     this.wait();
                }catch(InterruptedException e ){
                    blockedList.remove(tempThread);
                    throw e;
                }
            }
            //锁未被其他线程获得，则尝试从阻塞队列中删除自己，然后正式获得锁，标记locked 为true
            blockedList.remove(currentThread());
            this.locked = true;
            //记录获取锁的线程
            this.currentThread = currentThread();
        }
    }

    @Override
    public void lock(long mills) throws InterruptedException, TimeoutException {
        synchronized (this){
            if(mills <=0){
                //this.lock();
                throw new IllegalArgumentException("your lock time is illegal");
            }else{
                long remainingMiils = mills;
                long endMill = System.currentTimeMillis() + remainingMiils;
                while(locked){
                    if(remainingMiils <= 0){
                        throw new TimeoutException("can not get the lock during "+ mills);
                    }
                    if(!blockedList.contains(currentThread())){
                        blockedList.add(currentThread());
                    }
                    this.wait(remainingMiils);
                    remainingMiils = endMill - System.currentTimeMillis();
                }
                //终于获得了锁
                blockedList.remove(currentThread());
                this.locked = true;
                this.currentThread = currentThread();
            }
        }
    }

    @Override
    public void unlock() {
        synchronized (this){
            if(currentThread == currentThread()){
                this.locked = false;
                this.notifyAll();
            }
        }
    }

    @Override
    public List<Thread> getBlockedThreads() {
        return Collections.unmodifiableList(blockedList);
    }
}
