package com.disk.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ���Ӷ˿ڣ�����client��
 * 
 * @author cgq
 *
 */
public class Server {
	private ServerSocket serversocket;
	private ExecutorService thread_pool;
	// ������־ʵ����������������
	private BlockingQueue<LogBean> logQueue;
	// д��־
	private LogWriter logWriter;

	public Server() {
		try {
			//������־��������
			logQueue = new LinkedBlockingQueue<LogBean>();
			//������־��¼����
			logWriter = new  LogWriter(logQueue);
			serversocket = new ServerSocket(Xmlproperty.PORT);
			thread_pool = Executors.newCachedThreadPool();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void action() {
		Socket socket = null;
		thread_pool.execute(logWriter);
		while (true) {
			try {
				socket = serversocket.accept();
				ServerHandler handler = new ServerHandler(socket,logQueue);
				thread_pool.execute(handler);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("�пͻ�������ʧ��");
			}

		}
	}

	public static void main(String[] args) throws IOException {
		new Server().action();
	}
}
