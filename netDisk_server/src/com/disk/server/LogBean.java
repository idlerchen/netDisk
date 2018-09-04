package com.disk.server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogBean {
	//�����ͻ���IP
		private String ip;
		//��־��¼ʱ��
		private Date date;
		//��־����  upload��download��delete
		private String type;
		//�ļ�����
		private String fileName;
		//�����ɹ���ʧ�� ��success��error
		private String flag;
		public LogBean() {
		}
		public LogBean(String ip, Date date, String type, String fileName,String flag) {
			this.ip = ip;
			this.date = date;
			this.type = type;
			this.fileName = fileName;
			this.flag = flag;
		}
		@Override
		public String toString() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "["+flag+"]"+ip + "  " + type + "  " + sdf.format(date) + "  " +  fileName;
		}

}
