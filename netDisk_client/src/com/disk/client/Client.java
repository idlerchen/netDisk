package com.disk.client;

import javax.swing.JFrame;

public class Client {

public static void main(String[] args) {
		//创建窗体
		JFrame frame = new JFrame("EE网盘");
		//窗体大小
		frame.setSize(1200, 800);
		//窗体关闭退出程序
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//窗体大小不可变
		frame.setResizable(false);
		//窗体居中
		frame.setLocationRelativeTo(null);//居中
		//创建面板并添加
		DiskPanel panel = new DiskPanel();
		panel.setLayout(null);
		frame.add(panel);
		//创建窗体可见
		frame.setVisible(true);
		System.out.println();
	}
}
