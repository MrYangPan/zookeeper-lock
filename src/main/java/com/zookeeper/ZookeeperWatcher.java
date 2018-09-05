package com.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.*;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.ZooDefs.Ids;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Mr.PanYang on 2018/8/16.
 * <p>
 * Zookeeper   Watcher 事件监听
 */
public class ZookeeperWatcher implements Watcher {

    //zk 连接地址
    private static final String CONNECTSTRING = "127.0.0.1:2181";
    //zk 会话超时时间
    private static final int SESSION_OUTTIME = 2000;
    // 信号量,让zk在连接之前等待,连接成功后才能往下走.
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String LOG_MAIN = "【main】 ";
    private ZooKeeper zooKeeper;

    //  zk创建连接
    public void createConnection(String connectString, int sessionTimeout) {
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, this);
            System.out.println(LOG_MAIN + "zk 开始启动连接服务器....");
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createNode(String path, String data) {
        try {
            exists(path, true);
            zooKeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(LOG_MAIN + "节点创建成功, Path:" + path + ",data:" + data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("##########事件通知开始###########");
        // 获取连接状态
        KeeperState keeperState = watchedEvent.getState();
        //  获取节点路径
        String path = watchedEvent.getPath();
        //  获取事件类型
        EventType eventType = watchedEvent.getType();
        System.out.println("进入到 process() keeperState:" + keeperState + ", eventType:" + eventType + ", path:" + path);
        // 判断为连接状态
        if (keeperState == KeeperState.SyncConnected) {
            // 获取事件类型
            if (eventType == EventType.None) {
                // 如果建立建立成功,让后程序往下走
                System.out.println(LOG_MAIN + "zk 建立连接成功!");
                countDownLatch.countDown();
            } else if (eventType == EventType.NodeCreated) {
                System.out.println("获取事件节点通知，新增node节点" + path);
            } else if (eventType == EventType.NodeDataChanged) {
                System.out.println("获取事件节点通知，当前node节点" + path + "被修改");
            }
        }
        System.out.println("##########事件通知结束###########");
    }

    public void close() {
        try {
            if (zooKeeper != null)
                zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 是否开启监听事件
    public Stat exists(String path, boolean isWatch) {
        try {
            return this.zooKeeper.exists(path, isWatch);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        ZookeeperWatcher zookeeperWatcher = new ZookeeperWatcher();
        zookeeperWatcher.createConnection(CONNECTSTRING, SESSION_OUTTIME);
        zookeeperWatcher.createNode("/itmayiedu03", "001");
        zookeeperWatcher.close();
    }
}
