package com.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.*;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Mr.PanYang on 2018/8/16.
 * <p>
 * JAVA  语言操作 Zookeeper
 * <p>
 * Zookeeper 连接创建一个新的子线程，而不在主线程中进行创建，是为了提高响应速度，防止阻塞导致创建失败
 */
public class JavaZookeeper {

    //zk 连接地址
    private static final String CONNECTSTRING = "127.0.0.1:2181";
    //zk 会话超时时间
    private static final int SESSION_OUTTIME = 2000;
    //  使用CountDownLatch阻塞用户程序，必须等待连接，发送成功信号
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        // 开始连接zookeeper
        ZooKeeper zooKeeper = new ZooKeeper(CONNECTSTRING, SESSION_OUTTIME, new Watcher() {
            //事件通知  子线程
            public void process(WatchedEvent watchedEvent) {
                // 获取连接状态
                KeeperState keeperState = watchedEvent.getState();
                // 判断为连接状态
                if (keeperState == KeeperState.SyncConnected) {
                    // 等待先执行完
                    countDownLatch.countDown();
                    // 获取事件类型
                    EventType eventType = watchedEvent.getType();
                    if (eventType == EventType.None) {
                        System.out.println("Zookeeper 开始连接。。。。");
                    }
                }
            }
        });
        // 阻塞
        countDownLatch.await();
        //  主线程
        //创建持久类型节点，开放权限
//        String node = zooKeeper.create("/itmayiedu_last", "last".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        System.out.println("新增节点：" + node);

        // 创建临时节点,连接关闭后，节点被删除
        String result = zooKeeper.create("/itmayiedu_temp", "temp".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("新增节点：" + result);
        zooKeeper.close();
    }
}
