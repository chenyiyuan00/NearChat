package cn.edu.nsu.nearchat.net;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class FileSender extends Thread {
	private DatagramSocket sockek;
	private DatagramPacket pkg_obj;
	private DatagramPacket pkg_msg;
	private String ip;
	private String path;
	private String senderName;
	private int serverPort;
	private int clientPort;

	public FileSender(String ip, String path, String senderName,
			int serverPort, int clientPort) {
		super();
		this.ip = ip;
		this.path = path;
		this.senderName = senderName;
		this.serverPort = serverPort;
		this.clientPort = clientPort;
	}

	public String getIp() {
		return ip;
	}

	public String getPath() {
		return path;
	}

	public String getSenderName() {
		return senderName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void sendFile() {
		BufferedInputStream bis = null;
		FileInputStream fis = null;
		try {
			// 文件发送者设置监听端口
			sockek = new DatagramSocket(clientPort);
			fis = new FileInputStream(path);
			bis = new BufferedInputStream(fis);
			// 确认信息包
			byte[] msg_buff = new byte[1024];
			pkg_msg = new DatagramPacket(msg_buff, msg_buff.length);
			// 文件包
			byte[] buf = new byte[1024 * 63];
			int len;
			while ((len = bis.read(buf)) != -1) {
				pkg_obj = new DatagramPacket(buf, len, new InetSocketAddress(
						ip, serverPort));
				// 设置确认信息接收时间，2秒后未收到对方确认信息，则重新发送一次
				sockek.setSoTimeout(2 * 1000);
				while (true) {
					sockek.send(pkg_obj);
					sockek.receive(pkg_msg);
					System.out.println(new String(pkg_msg.getData()));
					break;
				}
			}
			// 文件传完后，发送一个结束包
			buf = NetConfig.I_OV.getBytes();
			DatagramPacket endpkg = new DatagramPacket(buf, buf.length,
					new InetSocketAddress(ip, serverPort));
			System.out.println("文件发送完毕");
			sockek.send(endpkg);
			bis.close();
			sockek.close();

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		sendFile();
	}
}
