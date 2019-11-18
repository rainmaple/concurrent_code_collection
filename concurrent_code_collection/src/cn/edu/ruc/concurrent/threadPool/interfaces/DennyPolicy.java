package cn.edu.ruc.concurrent.threadPool.interfaces;

import cn.edu.ruc.concurrent.exception.RunnableDennyException;

/**
 * @program: Ij_javatest
 * @description: 拒绝策略
 * @author: rainmaple
 * @date: 2019-11-14 21:07
 **/
@FunctionalInterface
public interface DennyPolicy {
    void reject(Runnable runnable,ThreadPool threadPool);

    /**
     * @description: 直接丢弃
     */
    class DiscardDenyPolicy implements DennyPolicy{

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            //do nothing
        }
    }
    /**
     * @description: 拒绝向任务提交者抛出异常
     */
    class AbortDenyPolicy implements DennyPolicy{
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            throw new RunnableDennyException("The runnable "+runnable+" will be abort");
        }
    }
    /**
     * @description: 让任务提交者自行handle
     */
    class RunnerDennyDennyPolicy implements DennyPolicy{
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            if(!threadPool.isShutDown()){
                runnable.run();
            }
        }
    }

}
