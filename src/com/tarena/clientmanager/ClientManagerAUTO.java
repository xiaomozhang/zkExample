package com.tarena.clientmanager;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ClientManagerAUTO {
	// zk����
	static ZooKeeper zk;
	// ������ֹ��������
	private static CountDownLatch countDown = new CountDownLatch(1);
	/* �������������Ϣ */
	// ���ӵ�ַ
	static String connect = "zkserver1:2181,zkserver2:2181,zkserver3:2181";
	// ���ӳ�ʱʱ��
	static int TIME_OUT = 2000;
	// ���Ӽ����
	Watcher connectWatcher = new Watcher() {
		public void process(WatchedEvent event) {
			System.out.println("���յ�zookeeper�����֪ͨ���Ự����������ɣ�");
		}
	};
	// �ڵ��Ƿ���ڼ����
	Watcher existsWatcher = new Watcher() {
		public void process(WatchedEvent event) {
			try {
				if (event.getType().toString().equals("None")) {
					System.out.println("none");
				}else if (event.getType().toString().equals("NodeCreated")) {
					System.out.println("�ڵ�1����");
				}else if (event.getType().toString().equals("NodeDeleted")) {
					System.out.println("�ڵ�1����");
				}else if (event.getType().toString().equals("NodeDataChanged")) {
					System.out.println("�ڵ�1�е������Ѿ����޸�");
				}else if (event.getType().toString().equals("NodeChildrenChanged")) {
					System.out.println("�ڵ�1���ӽڵ㱻�޸�");
				}else{
					System.out.println("event����Ϣ:"+event);
				}
				//�ظ�ע��watcher
				zk.exists("/nodeManager/tempNode1", existsWatcher);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * ��ʼ��zk����
	 */
	public void init() throws Exception{
		zk = new ZooKeeper(connect,TIME_OUT,connectWatcher);
	}
	
	/**
	 * ��zk������ע��Խڵ�1�Ƿ���ڣ������ߣ���֪ͨ
	 * @param zk �ͻ���
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
