package com.disk.client;

import java.awt.JobAttributes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DiskPanel extends JPanel {
	// �������
	// �׽���
	private Socket socket;
	// �ͻ���������
	private InputStream in;
	// �ͻ��������
	private OutputStream out;

	private File[] files;
	// �ϴ��ļ���ť
	private JButton uploadButton;
	// ˢ�°�ť
	private JButton refreshButton;
	// ������� - �������
	private JScrollPane scroll;
	// �ļ��б�
	private JTable filesTable;
	// �ļ���Ϣ -��ά����
	private Object[][] filesList;

	// ���췽�� -��ʼ����� - ���ӷ���� - �����ļ��б���Ϣ
	public DiskPanel() {
		uploadButton = new JButton("�ϴ��ļ�");
		uploadButton.setBounds(50, 10, 100, 40);
		uploadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();// ѡ����ļ�
					uploadFile(file);
					try {
						refresh();
					}catch(Exception e1) {
						JOptionPane.showMessageDialog(null, "ˢ��ʧ��");
					}
				}
			}
		});
		this.add(uploadButton);

		refreshButton = new JButton("ˢ��");
		refreshButton.setBounds(151, 10, 150, 40);
		refreshButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					refresh();
			}catch(Exception e1) {
					JOptionPane.showMessageDialog(null, "ˢ��ʧ��");
			}	
			}
		});
		this.add(refreshButton);

		connectServer();
		filesListLoad();
	}

	protected void uploadFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			DataOutputStream dos = new DataOutputStream(out);
			dos.writeUTF(Constant.STATE_UPLOAD + "," + file.getName() + "," + file.length());
			dos.flush();

			int len = 0;
			byte[] bytes = new byte[1024];
			while ((len = fis.read(bytes)) != -1) {
				dos.write(bytes, 0, len);
				dos.flush();
			}

			DataInputStream dis = new DataInputStream(in);
			String msg = dis.readUTF();
			JOptionPane.showMessageDialog(null, msg);
			// fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void refresh() {
		DataOutputStream dos = new DataOutputStream(out);
		try {
			dos.writeUTF(Constant.STATE_FILELIST + ",");
			dos.flush();
			ObjectInputStream ois = new ObjectInputStream(in);
			files = (File[]) ois.readObject();
			filesList = arrayTrans(files);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		TableModel model = new DefaultTableModel(filesList, Constant.COLUMNNAMES) {

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return getValueAt(0, columnIndex).getClass();
			}
		};
		filesTable.setModel(model);
		filesTable.repaint();
	}

	// ���ӷ����
	private void connectServer() {
		try {
			socket = new Socket(Xmlproperty.IP, Xmlproperty.PORT);
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// �����ļ�
	public void filesListLoad() {
		scroll = new JScrollPane();
		scroll.setBounds(50, 60, 1000, 800);
		DataOutputStream dos = new DataOutputStream(out);
		try {
			dos.writeUTF(Constant.STATE_FILELIST + ",");
			dos.flush();
			ObjectInputStream ois = new ObjectInputStream(in);
			files = (File[]) ois.readObject();
			filesList = arrayTrans(files);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		filesTable = new JTable(new DefaultTableModel(filesList, Constant.COLUMNNAMES) {

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return getValueAt(0, columnIndex).getClass();
			}
		});

		filesTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					int row = filesTable.rowAtPoint(e.getPoint());
					int col = filesTable.columnAtPoint(e.getPoint());
					String fileName = (String)filesList[row][1];
					if (col == 4) {
						JFileChooser dirChooser = new JFileChooser();
						dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int returnVal = dirChooser.showSaveDialog(null);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							String dir = dirChooser.getSelectedFile().toString();// ѡ����ļ���
							String fileFullName = dir + File.separator+ (String) filesList[row][1]; // �ļ��� +�ļ��� �γ��ļ���ַ
							System.out.println(fileFullName);
							download(fileName, fileFullName);
						}
					}
					if (col == 5) {
						int value = JOptionPane.showConfirmDialog(null, "�Ƿ�ɾ���ļ�", "confirm", JOptionPane.YES_NO_OPTION);
						if (value == JOptionPane.YES_OPTION) {
							deleteFile((String) fileName);
							try {
								refresh();
						}catch(Exception e1) {
								JOptionPane.showMessageDialog(null, "ˢ��ʧ��");
						}	
						}
					}
				}
			}
		});
		filesTable.setEnabled(false);
		filesTable.setRowHeight(50);
		scroll.setViewportView(filesTable);
		this.add(scroll);

	}

	protected void download(String name, String path) {
		File file = new File(path);
		if (file.exists()) {
			JOptionPane.showMessageDialog(null, "�ļ��Ѵ���");
		} else {
			DataOutputStream dos = new DataOutputStream(out);
			DataInputStream dis = new DataInputStream(in);
			BufferedOutputStream bos = null;
			try {
				dos.writeUTF(Constant.STATE_DOWNLOAD + ","+name);
				dos.flush();
				
				long fileLength = Long.parseLong(dis.readUTF());
				if(fileLength == 0) {
					file.createNewFile();
					JOptionPane.showMessageDialog(null, "�ļ��������");
				}
				else if(fileLength ==-1) {
					JOptionPane.showMessageDialog(null, "�ļ��Ѳ�����");
				}
				else {
					bos = new BufferedOutputStream(new FileOutputStream(file));
					long sum = 0;
					byte[] bytes = new byte[1024];
					int len = 0;
					while(sum<fileLength) {
					    len = dis.read(bytes);
						bos.write(bytes,0,len);
						bos.flush();
						sum+=len;
					}
					JOptionPane.showMessageDialog(null, "�ļ��������");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "����ʧ��");
			}
		}
	}

	protected void deleteFile(String name) {
		DataInputStream dis = new DataInputStream(in);
		DataOutputStream dos = new DataOutputStream(out);
		try {
			dos.writeUTF(Constant.STATE_DELETE + "," + name);
			dos.flush();

			String result = dis.readUTF();
			JOptionPane.showMessageDialog(null, result);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "ɾ���쳣");
		}
		//refresh();
	}

	// �����ܵ��ļ� һά����תΪ��ά����
	private Object[][] arrayTrans(File[] files) {
		Object[][] obj = new Object[files.length][6];
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String fileName = file.getName();
			String[] names = fileName.split("\\.");
			obj[i][0] = Constant.GUI_IMG.get(names[names.length - 1]);
			obj[i][1] = fileName;
			obj[i][2] = fileSize(file.length());
			obj[i][3] = new Date(file.lastModified());
			obj[i][4] = Constant.GUI_IMG.get("download");
			obj[i][5] = Constant.GUI_IMG.get("delete");
		}

		return obj;
	}

	// �����ļ���С������λ
	private Object fileSize(long length) {
		if (length < 1024) {
			return length + "B";
		} else if (length < 1024 * 1024) {
			return length / 1024 + "KB";
		} else if (length < 1024 * 1024 * 1024) {
			return length / 1024 / 1024 + "MB";
		} else if (length < 1024 * 1024 * 1024 * 1024) {
			return length / 1024 / 1024 / 1024 + "GB";
		} else
			return "�ļ�̫���޷�����";
	}
}
