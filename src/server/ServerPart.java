package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerPart {
	JTextArea area;
	JProgressBar jpb;
	JFrame f;

	DataInputStream din = null;
	DataOutputStream outD = null;
	Socket soket = null;

	ServerPart() {
		f = new JFrame("Server");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(200, 250);
		f.setLayout(new BorderLayout());
		area = new JTextArea();
		f.add(area, BorderLayout.CENTER);
		JScrollPane sp = new JScrollPane(area);
		f.getContentPane().add(sp);
		f.setAlwaysOnTop(true);
		f.setVisible(true);
		area.setBackground(Color.LIGHT_GRAY);
		connect();
	}

	public void connect() {
		int port = 2154;
		try {
			ServerSocket ss = new ServerSocket(port);
			while (true) {
				area.append("Ожидание подключения...\n");
				jpb = new JProgressBar();
				jpb.setIndeterminate(true);
				jpb.setForeground(Color.black);
				f.add(jpb, BorderLayout.AFTER_LAST_LINE);
				soket = ss.accept();
				InputStream in = soket.getInputStream();
				din = new DataInputStream(in);
				outD = new DataOutputStream(soket.getOutputStream());
				area.append("Передается файл\n");
				area.append("Прием нового файла: \n");
				byte flag = din.readByte();

				if (flag == 0)
					sendVideo();
				else if (flag == 1)
					sendButtonIcon();
				else if (flag == 2)
					sendUserIcon();
				else if (flag == 3)
					getVideo();
				else
					throw new Exception();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getVideo() throws IOException {
		// TODO Auto-generated method stub
		long fileSize = din.readLong(); // получаем размер файла
		int fsaize = (int) (fileSize / (1024));
		String fileName = din.readUTF(); // прием имени файла
		area.append("Имя файла: " + fileName + "\n");
		area.append("Размер файла: " + fileSize + " байт\n");
		jpb.setMaximum(fsaize);
		jpb.setIndeterminate(false);
		byte[] buffer = new byte[64 * 1024];
		FileOutputStream outF = new FileOutputStream(fileName);
		int count, total = 0;
		while ((count = din.read(buffer)) != -1) {
			total += count;
			outF.write(buffer, 0, count);
			jpb.setValue(total / 1024);
			if (total == fileSize) {
				break;
			}
		}
		outF.flush();
		outF.close();
		area.append("Файл принят\n---------------------------------\n");
		jpb.setVisible(false);
	}

	private void sendUserIcon() {
		// TODO Auto-generated method stub

	}

	private void sendButtonIcon() {
		// TODO Auto-generated method stub

	}

	private void sendVideo() {
		// TODO Auto-generated method stub
		try {
			String str = din.readUTF();
			try {
				FileInputStream in = new FileInputStream(str);
				byte[] buffer = new byte[64 * 1024];
				int count;

				File file = new File(str);
				System.out.println(file.length());
				outD.writeLong(file.length());

				while ((count = in.read(buffer)) != -1) {
					outD.write(buffer, 0, count);
				}
				outD.flush();
				in.close();
				soket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] arg) {
		new ServerPart();
	}
}
