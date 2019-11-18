package cn.edu.ruc.concurrent.threadPool.interfaces;

/**
 * @program: Ij_javatest
 * @description: 线程工厂类
 * @author: rainmaple
 * @date: 2019-11-14 21:05
 **/
@FunctionalInterface
public interface ThreadFactory {
    Thread createThread(Runnable runnable);
}
