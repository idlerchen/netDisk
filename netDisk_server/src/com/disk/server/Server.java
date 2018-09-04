package com.disk.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 连接端口，接收client，
 * 
 * @author cgq
 *
 */
public class Server {
	private ServerSocket serversocket;
	private ExecutorService thread_pool;
	// 保存日志实体类对象的阻塞队列
	private BlockingQueue<LogBean> logQueue;
	// 写日志
	private LogWriter logWriter;

	public Server() {
		try {
			//创建日志阻塞队列
			logQueue = new LinkedBlockingQueue<LogBean>();
			//创建日志记录对象
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
				System.out.println("有客户端请求失败");
			}

		}
	}

	public static void main(String[] args) throws IOException {
		new Server().action();
	}
}
