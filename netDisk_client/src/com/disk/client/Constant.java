package com.disk.client;

import java.util.HashMap;

import javax.swing.ImageIcon;

public class Constant {
	//�ļ�����״̬
	public static final int STATE_UPLOAD = 0;
	public static final int STATE_FILELIST = 1;
	public static final int STATE_DOWNLOAD = 2;
	public static final int STATE_DELETE = 3;
	//�ļ�ͼ���Ӧmap
	public static final HashMap<String,ImageIcon> GUI_IMG= new HashMap<>();
	//�ļ��б��Ӧ��ͷ
	public static final String[] COLUMNNAMES = { "����", "�ļ���", "��С", "ʱ��", "����", "ɾ��" };
	//����ļ�����/�ļ�ͼ��   - ��map
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
