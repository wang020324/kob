package com.kob.newbotrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//一整个消息队列
public class BotPool extends Thread{
    private final static ReentrantLock lock = new ReentrantLock();//添加锁
    private final Condition condition =lock.newCondition();//条件变量，以对不同情况下不同且特殊处理
    private final Queue<Bot> bots =new LinkedList<>();//定义一个队列以存储信息

    //定义函数实现从队列里插入一个Bot
    public void addBot(Integer userId,String botCode,String input){
        lock.lock();//涉及到对于队列的修改因此需要加上一个锁
        try{
          bots.add(new Bot(userId,botCode,input));
          condition.signalAll();//加完之后要唤醒另一个线程
        }finally{
            lock.unlock();//无论如何都需要把锁解锁掉
        }
    }

    //取出队头之后对Consume进行消费
    private void consume(Bot bot){
        Consumer consumer =new Consumer();
        consumer.startTimeout(2000,bot);//给bot等待或者执行2s
    }



    @Override
    public void run() {
       while(true){
        lock.lock();
        //当前队列为空，需要把进程阻塞，如果当前队列有新的任务就需要唤醒
        if(bots.isEmpty()){
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                lock.unlock();//解锁的时候记得解锁
                break;
            }
        }else{
            Bot bot =bots.remove();//返回并且删除队头元素
            lock.unlock();//取完队头元素之后就可以解锁

            consume(bot);//然后消费一下队列
        }

       }

    }
}
