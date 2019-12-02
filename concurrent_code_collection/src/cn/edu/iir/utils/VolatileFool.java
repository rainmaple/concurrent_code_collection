package cn.edu.iir.utils;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName VolatileFool
 * @Description: 为什么要使用volatile,运行程序会发现reader线程感知不到init_value变量值的变化
 * @Author rainmaple
 * @Date 2019/12/2 
 * @Version V1.0
 **/
public class VolatileFool {
    final static int MAX = 5;
    static int init_value = 0;
    public static void main(String args[]){
        //启动一个Reader 线程，当发现 local_value 和 init_value 不同的时候输出 init_value被修改的信息
        new Thread(()->{
            int localValue = init_value;
            while(localValue < MAX){
                if(init_value != localValue){
                    System.out.printf("The init_value is updated to [d%]\n",init_value);
                }
            }
        },"rainmaple_ReaderThread").start();

        new Thread(()->{
            int localValue  = init_value;
            while(localValue < MAX){
                System.out.printf("The init_value will be changed to [%d]\n",++localValue);
                init_value = localValue;
                try{
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"rainmaple_UpdateThread").start();
    }
}
