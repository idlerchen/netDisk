package com.disk.client;

import java.util.HashMap;

import javax.swing.ImageIcon;

public class Constant {
	//文件操作状态
	public static final int STATE_UPLOAD = 0;
	public static final int STATE_FILELIST = 1;
	public static final int STATE_DOWNLOAD = 2;
	public static final int STATE_DELETE = 3;
	//文件图标对应map
	public static final HashMap<String,ImageIcon> GUI_IMG= new HashMap<>();
	//文件列表对应表头
	public static final String[] COLUMNNAMES = { "类型", "文件名", "大小", "时间", "下载", "删除" };
	//添加文件类型/文件图标   - 至map
	static {
		GUI_IMG.put("delete", new ImageIcon("image/delete.png"));
		GUI_IMG.put("download", new ImageIcon("image/download.png"));
		GUI_IMG.put("doc", new ImageIcon("image/doc.png"));
		GUI_IMG.put("pdf", new ImageIcon("image/pdf.png"));
		GUI_IMG.put("docx", new ImageIcon("image/doc.png"));
		GUI_IMG.put("ppt", new ImageIcon("image/ppt.png"));
		GUI_IMG.put("pptx", new ImageIcon("image/ppt.png"));
		GUI_IMG.put("xls", new ImageIcon("image/xls.png"));
		GUI_IMG.put("xlsx", new ImageIcon("image/xls.png"));
		
		GUI_IMG.put("txt", new ImageIcon("image/txt.png"));
		GUI_IMG.put("sql", new ImageIcon("image/sql.png"));
		GUI_IMG.put("xml", new ImageIcon("image/xml.png"));
	}
}
