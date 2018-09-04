package com.disk.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class LogWriter implements Runnable {
	private BlockingQueue<LogBean> logQueue;
	private BufferedWriter writer;
	
	public LogWriter(BlockingQueue<LogBean> logQueue) {
		this.logQueue = logQueue;
		File logFile = new File(Xmlproperty.LOG_PATH);
		try {
			writer = new BufferedWriter(new FileWriter(logFile, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true) {
			try {
				LogBean logBean  = logQueue.take();
				writer.write(logBean.toString());
				writer.newLine();
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
