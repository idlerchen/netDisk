package com.disk.client;

import javax.swing.JFrame;

public class Client {

public static void main(String[] args) {
		//��������
		JFrame frame = new JFrame("EE����");
		//�����С
		frame.setSize(1200, 800);
		//����ر��˳�����
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//�����С���ɱ�
		frame.setResizable(false);
		//�������
		frame.setLocationRelativeTo(null);//����
		//������岢���
		DiskPanel panel = new DiskPanel();
		panel.setLayout(null);
		frame.add(panel);
		//��������ɼ�
		frame.setVisible(true);
		System.out.println();
	}
}
