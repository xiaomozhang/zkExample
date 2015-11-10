package com.tarena.zkclient;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class Client1 {
	
	//������ֹ��������
		private static CountDownLatch countDown = new CountDownLatch(1);
		/* �������������Ϣ */
		// ���ӵ�ַ
		static String connect = "zkserver1:2181,zkserver2:2181,zkserver3:2181";
		// ���ӳ�ʱʱ��
		static int TIME_OUT = 2000;
		// �����
		static Watcher watcher = new Watcher() {
			public void process(WatchedEvent event) {
				System.out.println("���յ�zookeeper�����֪ͨ���Ự����������ɣ�");
				System.out.println("��ʱzk������Ϣ��"+zk);
				try {
					//�ȴ����������пͻ��˵Ľڵ�
					Stat s = zk.exists("/nodeManager", true);
					if (null==s) {
						System.out.println("����ͻ��˽ڵ㲻���ڣ���ʼ����");
						zk.create("/nodeManager", "nodeManager".getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
					}
					System.out.println("node1��ʼ��zk�����ע��>>>>>>");
					String path = zk.create("/nodeManager/tempNode1", "node1".getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
					System.out.println("��zk�����ע��ɹ�!!!");
				} catch (Exception e) {
					System.out.println("�Ự������ɺ󴴽���ʱ�ڵ�ʱ�����쳣��"+e.getMessage());
				}
			}
		};
		//zk����
		static ZooKeeper zk;
		public static void main(String[] args) throws Exception {
			System.out.println("�ͻ���1����ʼ��������");
			zk = new ZooKeeper(connect, TIME_OUT, watcher);
			System.out.println("zk���󴴽���ɣ������첽������zk������֮������ӣ�");
			System.out.println("zk�������Ϣ��" + zk);
			countDown.await();
		}

}
