package com.disk.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * 建立连接之后，传入socket，处理in，传输out
 * 
 * @author cgq
 *
 */
public class ServerHandler implements Runnable {
	private Socket socket;
	private BlockingQueue<LogBean> logQueue;
	public ServerHandler(Socket socket, BlockingQueue<LogBean> logQueue) {
		this.socket = socket;
		this.logQueue = logQueue;
	}

	@Override
	public void run() {
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			OutputStream out = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);
			while (true) {
				String header = dis.readUTF();
				String[] findHeader = header.split(",");
				switch (Integer.parseInt(findHeader[0].trim())) {
				case Constant.STATE_FILELIST:
					getFileOut(out);
					break;
				case Constant.STATE_UPLOAD:
					saveFile(findHeader[1], Integer.parseInt(findHeader[2]), dis, dos);
					break;
				case Constant.STATE_DELETE:
					deleteFile(findHeader[1],dos);
					break;
				case Constant.STATE_DOWNLOAD:
					downloadFile(findHeader[1],dos);
					break;
				default:
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void downloadFile(String name, DataOutputStream dos) {
		File file = new File(Xmlproperty.PATH+File.separator+name);
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			if(! file.exists()) {
				dos.writeUTF("-1");
				dos.flush();
			}
			else {
				dos.writeUTF(file.length() +"");
				System.out.println(file.length());
				dos.flush();
				byte[] bytes = new byte[1024];
				int len=0;
				while( (len = bis.read(bytes)) != -1) {
					dos.write(bytes,0,len);
					dos.flush();
				}
				LogBean log = new LogBean(socket.getInetAddress().toString(),
						new Date(), Constant.LOG_DOWNLOAD, name,Constant.SUCCESS);
				logQueue.add(log);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			LogBean log = new LogBean(socket.getInetAddress().toString(),
					new Date(), Constant.LOG_DOWNLOAD, name,Constant.ERROR);
			logQueue.add(log);
		}
		finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void deleteFile(String name, DataOutputStream dos) {
		File file = new File(Xmlproperty.PATH+File.separator+name);
		if(file.exists()) {
			if(file.delete()) {
				try {
					LogBean log = new LogBean(socket.getInetAddress().toString(),
							new Date(), Constant.LOG_DELETE, name,Constant.SUCCESS);
					logQueue.add(log);
					dos.writeUTF("文件删除成功");
					dos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					dos.writeUTF("删除失败");
					LogBean log = new LogBean(socket.getInetAddress().toString(),
							new Date(), Constant.LOG_DELETE, name,Constant.ERROR);
					logQueue.add(log);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			try {
				dos.writeUTF("文件已不存在");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveFile(String name, long length, DataInputStream dis, DataOutputStream dos) {
		File file = new File(Xmlproperty.PATH + File.separator + name);
		FileOutputStream fos;
		
			try {
				fos = new FileOutputStream(file);
			
			byte[] bytes = new byte[1024];
			int len = 0;
			int sum = 0;
			while (sum < length) {
				len = dis.read(bytes);
				sum += len;
				fos.write(bytes, 0, len);
				fos.flush();
			}
			dos.writeUTF("文件上传成功");
			dos.flush();
			LogBean log = new LogBean(socket.getInetAddress().toString(),
					new Date(), Constant.LOG_UPLOAD, name,Constant.SUCCESS);
			logQueue.add(log);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				LogBean log = new LogBean(socket.getInetAddress().toString(),
						new Date(), Constant.LOG_UPLOAD, name,Constant.ERROR);
				logQueue.add(log);
			}
	}

	private void getFileOut(OutputStream out) {
		try {
			File dir = new File(Xmlproperty.PATH);
			File[] files = dir.listFiles();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(files);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
