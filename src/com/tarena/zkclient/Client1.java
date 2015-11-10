package com.tarena.zkclient;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class Client1 {
	
	//用来阻止程序运行
		private static CountDownLatch countDown = new CountDownLatch(1);
		/* 创建连接相关信息 */
		// 连接地址
		static String connect = "zkserver1:2181,zkserver2:2181,zkserver3:2181";
		// 连接超时时间
		static int TIME_OUT = 2000;
		// 监控器
		static Watcher watcher = new Watcher() {
			public void process(WatchedEvent event) {
				System.out.println("接收到zookeeper服务端通知，会话真正创建完成！");
				System.out.println("此时zk对象信息："+zk);
				try {
					//先创建管理所有客户端的节点
					Stat s = zk.exists("/nodeManager", true);
					if (null==s) {
						System.out.println("管理客户端节点不存在，开始创建");
						zk.create("/nodeManager", "nodeManager".getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
					}
					System.out.println("node1开始向zk服务端注册>>>>>>");
					String path = zk.create("/nodeManager/tempNode1", "node1".getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
					System.out.println("向zk服务端注册成功!!!");
				} catch (Exception e) {
					System.out.println("会话创建完成后创建临时节点时出现异常："+e.getMessage());
				}
			}
		};
		//zk对象
		static ZooKeeper zk;
		public static void main(String[] args) throws Exception {
			System.out.println("客户端1程序开始启动……");
			zk = new ZooKeeper(connect, TIME_OUT, watcher);
			System.out.println("zk对象创建完成！正在异步创建和zk服务器之间的连接：");
			System.out.println("zk对象的信息：" + zk);
			countDown.await();
		}

}
