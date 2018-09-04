package com.disk.server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogBean {
	//操作客户端IP
		private String ip;
		//日志记录时间
		private Date date;
		//日志类型  upload、download、delete
		private String type;
		//文件名称
		private String fileName;
		//操作成功或失败 ：success或error
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
