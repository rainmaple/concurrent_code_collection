package cn.edu.ruc.concurrent.exception;
/**
 * @ClassName RunnableDennyException
 * @Description: 定义拒绝线程池添加的策略异常类
 * @Author rainmaple
 * @Date 2019/11/14 
 * @Version V1.0
 **/
public class RunnableDennyException extends RuntimeException{
    public RunnableDennyException(String message){
        super(message);
    }
}
