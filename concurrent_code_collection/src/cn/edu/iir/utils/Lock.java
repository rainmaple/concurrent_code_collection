package cn.edu.iir.utils;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @program: Ij_javatest
 * @description: 自定义锁接口
 * @author: rainmaple
 * @date: 2019-11-14 19:26
 **/
public interface Lock {
    void lock() throws InterruptedException;
    void lock(long mills) throws InterruptedException, TimeoutException;
    void unlock();
    List<Thread> getBlockedThreads();
}
