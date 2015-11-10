package com.tarena.clientmanager;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ClientManagerManual {
	// zk����
	static ZooKeeper zk;
	// ������ֹ��������
	private static CountDownLatch countDown = new CountDownLatch(1);
	/* �������������Ϣ */
	// ���ӵ�ַ
	static String connect = "192.168.221.200:2181";
	// ���ӳ�ʱʱ��
	static int TIME_OUT = 2000;
	// �����
	static Watcher watcher = new Watcher() {
		public void process(WatchedEvent event) {
			System.out.println("���յ�zookeeper�����֪ͨ���Ự����������ɣ�");
		}
	};

	/**
	 * ��ȡ��ǰ��Ⱥ�еĽڵ����
	 * 
	 * @return
	 */
	public Map<String, Integer> getNodeNums() throws Exception{
		Map<String, Integer> map = new HashMap();
		System.out.println("�ͻ��˹������ʼ��������");
		zk = new ZooKeeper(connect, TIME_OUT, watcher);
		List<String> list = zk.getChildren("/nodeManager", false);
		map.put("live", list.size());
		map.put("die", 3-list.size());
		return map;
	}
	
	public static void main(String[] args) throws Exception{
		ClientManagerManual cm = new ClientManagerManual();
		Map<String, Integer> map = cm.getNodeNums();
		System.out.println("��Ⱥ�ڵ������������");
		System.out.println("Live Nodes:"+map.get("live"));
		System.out.println("Die  Nodes:"+map.get("die"));
	}

}
