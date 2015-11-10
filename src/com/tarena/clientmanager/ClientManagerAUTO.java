package com.tarena.clientmanager;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ClientManagerAUTO {
	// zk对象
	static ZooKeeper zk;
	// 用来阻止程序运行
	private static CountDownLatch countDown = new CountDownLatch(1);
	/* 创建连接相关信息 */
	// 连接地址
	static String connect = "zkserver1:2181,zkserver2:2181,zkserver3:2181";
	// 连接超时时间
	static int TIME_OUT = 2000;
	// 连接监控器
	Watcher connectWatcher = new Watcher() {
		public void process(WatchedEvent event) {
			System.out.println("接收到zookeeper服务端通知，会话真正创建完成！");
		}
	};
	// 节点是否存在监控器
	Watcher existsWatcher = new Watcher() {
		public void process(WatchedEvent event) {
			try {
				if (event.getType().toString().equals("None")) {
					System.out.println("none");
				}else if (event.getType().toString().equals("NodeCreated")) {
					System.out.println("节点1上线");
				}else if (event.getType().toString().equals("NodeDeleted")) {
					System.out.println("节点1下线");
				}else if (event.getType().toString().equals("NodeDataChanged")) {
					System.out.println("节点1中的数据已经被修改");
				}else if (event.getType().toString().equals("NodeChildrenChanged")) {
					System.out.println("节点1的子节点被修改");
				}else{
					System.out.println("event的信息:"+event);
				}
				//重复注册watcher
				zk.exists("/nodeManager/tempNode1", existsWatcher);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 初始化zk对象
	 */
	public void init() throws Exception{
		zk = new ZooKeeper(connect,TIME_OUT,connectWatcher);
	}
	
	/**
	 * 向zk服务器注册对节点1是否存在（即上线）的通知
	 * @param zk 客户端
	 * @throws Exception
	 */
	public void checkExists(ZooKeeper zk) throws Exception{
		zk.exists("/nodeManager/tempNode1", existsWatcher);
	}

	public static void main(String[] args) throws Exception{
		ClientManagerAUTO auto = new ClientManagerAUTO();
		auto.init();
		auto.checkExists(zk);
		countDown.await();
	}
}
