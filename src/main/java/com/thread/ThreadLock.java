package com.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mr.PanYang on 2018/8/17.
 */
public class ThreadLock {
    public static void main(String[] args) {
        ResLock resLock = new ResLock();
        OutLocak outLocak = new OutLocak(resLock);
        outLocak.start();
        InputLock inputLock = new InputLock(resLock);
        inputLock.start();
    }
}

/**
 * 共享资源
 */
class ResLock {
    public String userName;
    public String sex;
    public boolean flag = false;
    //  是使用lock 锁，比喻手动挡，使用灵活
    Lock lock = new ReentrantLock();
}

// 写的线程
class OutLocak extends Thread {
    ResLock res;

    public OutLocak(ResLock res) {
        this.res = res;
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            try {
                res.lock.lock();
                if (count == 0) {
                    res.userName = "小红";
                    res.sex = "女";
                } else {
                    res.userName = "余胜军";
                    res.sex = "男";
                }
                // 计算基数或者偶数的公式
                count = (count + 1) % 2;
            } catch (Exception e) {
            } finally {
                res.lock.unlock();
            }
        }
    }
}

// 读的线程
class InputLock extends Thread {
    ResLock res;

    public InputLock(ResLock res) {
        this.res = res;
    }

    @Override
    public void run() {
        while (true) {
            try {
                res.lock.lock();
                System.out.println(res.userName + "," + res.sex);
            } catch (Exception e) {
            } finally {
                res.lock.unlock();
            }
        }
    }
}