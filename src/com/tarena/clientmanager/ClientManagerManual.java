package com.tarena.clientmanager;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ClientManagerManual {
	// zk对象
	static ZooKeeper zk;
	// 用来阻止程序运行
	private static CountDownLatch countDown = new CountDownLatch(1);
	/* 创建连接相关信息 */
	// 连接地址
	static String connect = "192.168.221.200:2181";
	// 连接超时时间
	static int TIME_OUT = 2000;
	// 监控器
	static Watcher watcher = new Watcher() {
		public void process(WatchedEvent event) {
			System.out.println("接收到zookeeper服务端通知，会话真正创建完成！");
		}
	};

	/**
	 * 获取当前集群中的节点情况
	 * 
	 * @return
	 */
	public Map<String, Integer> getNodeNums() throws Exception{
		Map<String, Integer> map = new HashMap();
		System.out.println("客户端管理程序开始启动……");
		zk = new ZooKeeper(connect, TIME_OUT, watcher);
		List<String> list = zk.getChildren("/nodeManager", false);
		map.put("live", list.size());
		map.put("die", 3-list.size());
		return map;
	}
	
	public static void main(String[] args) throws Exception{
		ClientManagerManual cm = new ClientManagerManual();
		Map<String, Integer> map = cm.getNodeNums();
		System.out.println("集群节点存活整体情况：");
		System.out.println("Live Nodes:"+map.get("live"));
		System.out.println("Die  Nodes:"+map.get("die"));
	}

}
