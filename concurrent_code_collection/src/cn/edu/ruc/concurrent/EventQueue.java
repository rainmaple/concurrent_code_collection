package cn.edu.ruc.concurrent;
import java.util.LinkedList;

import static cn.edu.iir.utils.InOrOutputUtils.console;

/**
 * @ClassName EventQueue
 * @Description: 对一个事件的生产消费过程的并发处理
 * @Author rainmaple
 * @Date 2019/11/14 
 * @Version V1.0
 **/
public class EventQueue {
    private int max ;
    static class Event
    {}
    private final LinkedList<Event> eventQueue = new LinkedList<>();
    private final static int DEFAULT_MAX_EVENT = 10 ;
    public EventQueue(){
        this(DEFAULT_MAX_EVENT);
    }
    public EventQueue(int max){
        this.max = max;
    }

    /**
     * @description: 生产者
     * @param event
     */
    public void offer(Event event){
        synchronized (eventQueue){
            while(eventQueue.size() >= max){
                try{
                    console("this queue is full.");
                    eventQueue.wait();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            console("this new event is submitted");
            eventQueue.addLast(event);
            eventQueue.notifyAll();
        }
    }

    /**
     * @description: 消费者
     * @return
     */
    public Event take(){
        synchronized (eventQueue){
            while(eventQueue.isEmpty()){
                try{
                    console("this queue is empty");
                    eventQueue.wait();
                }catch (InterruptedException interruptE){
                    interruptE.printStackTrace();
                }
            }
            Event event = eventQueue.removeFirst();
            this.eventQueue.notifyAll();
            console("this event "+event+" has been handled");
            return event;
        }
    }

}

