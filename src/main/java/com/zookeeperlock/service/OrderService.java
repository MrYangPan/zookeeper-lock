package com.zookeeperlock.service;

import com.zookeeperlock.OrderNumGenerator;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mr.PanYang on 2018/8/21.
 * <p>
 * 订单生成调用业务逻辑
 */
public class OrderService implements Runnable {

    OrderNumGenerator orderNumGenerator = new OrderNumGenerator();
    //重入锁
//    private Lock lock = new ReentrantLock();

    //自定义分布式锁
    private ILock lock = new ZookeeperDistrbuteLock();

    //模拟用户生成订单号
    @Override
    public void run() {
        //1.同步锁，解决线程安全问题
//        synchronized (this) {
//            getNumber();
//        }

        //2.lock锁
        try {
            lock.getLock();
            getNumber();
        } catch (Exception e) {
        } finally {
            lock.unLock();
        }
    }

    public void getNumber() {
        String number = orderNumGenerator.getNumber();
        System.out.println(Thread.currentThread().getName() + " ## number ：" + number);
    }

    public static void main(String[] args) {
//        OrderService orderService = new OrderService();
        System.out.println("模拟生成订单号开始........");
        for (int i = 0; i < 100; i++) {
            new Thread(new OrderService()).start();
        }
    }
}
