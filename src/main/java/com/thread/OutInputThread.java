package com.thread;

/**
 * Created by Mr.PanYang on 2018/8/17.
 */
public class OutInputThread {

    public static void main(String[] args) {
        Res res = new Res();
        Out out = new Out(res);
        out.start();
        Input input = new Input(res);
        input.start();
    }
}

/**
 * 共享资源
 */
class Res {

    public String userName;
    public String sex;
    public boolean flag = false;
}

// 写的线程
class Out extends Thread {
    Res res;

    public Out(Res res) {
        this.res = res;
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            synchronized (res) {
                if (res.flag) {
                    try {
                        res.wait();
                    } catch (Exception e) {
                    }
                }
                if (count == 0) {
                    res.userName = "小红";
                    res.sex = "女";
                } else {
                    res.userName = "余胜军";
                    res.sex = "男";
                }
                // 计算基数或者偶数的公式
                count = (count + 1) % 2;
                res.flag = true;
                res.notify();
            }
        }
    }
}

// 读的线程
class Input extends Thread {
    Res res;

    public Input(Res res) {
        this.res = res;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (res) {
                if (!res.flag) {
                    try {
                        res.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(res.userName + "," + res.sex);
                res.flag = false;
                res.notify();
            }
        }
    }
}