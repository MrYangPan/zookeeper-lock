package com.thread;

/**
 * Created by Mr.PanYang on 2018/8/17.
 * <p>
 * synchronized 解决线程安全问题的条件：
 * <p>
 * 1.必须有2个以上线程需要发生同步
 * 2.多个线程同步，必须用同一把锁
 * 3.保证只有一个线程执行
 * <p>
 * 优点：保证数据原子性，安全问题
 * <p>
 * 缺点：效率很低
 */
public class ThreadTrainDemo {

    public static void main(String[] args) {
        ThreadTrain threadTrain = new ThreadTrain();
        Thread thread1 = new Thread(threadTrain, "窗口①");
        thread1.start();
        Thread thread2 = new Thread(threadTrain, "窗口②");
        thread2.start();
    }
}

class ThreadTrain implements Runnable {
    //总共100张火车票
    private static int trainCount = 100;
    Object obj = new Object();

    @Override
    public void run() {
        //模拟一直抢票
        while (trainCount > 0) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
            //售票，同步代码块
            synchronized (obj) {
                //方式一：同步代码块，解决数据的安全问题
                if (trainCount > 0) {
                    System.out.println(Thread.currentThread().getName() + "售出第：" + (100 - trainCount + 1) + "票");
                    trainCount--;
                }
            }
        }
    }

    //方式二：同步函数，解决数据安全问题     注意：使用的是 this 作为锁
    public synchronized void sale() {
        if (trainCount > 0) {
            System.out.println(Thread.currentThread().getName() + "售出第：" + (100 - trainCount + 1) + "票");
            trainCount--;
        }
    }

    //方式三：静态同步函数，解决数据安全问题   注意：使用的是当前类的字节码文件 ThreadTrain.class 作为锁
    public static synchronized void sale2() {
        if (trainCount > 0) {
            System.out.println(Thread.currentThread().getName() + "售出第：" + (100 - trainCount + 1) + "票");
            trainCount--;
        }
    }
}


