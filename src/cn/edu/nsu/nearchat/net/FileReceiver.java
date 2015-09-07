package cn.edu.nsu.nearchat.net;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class FileReceiver extends Thread {
	private DatagramSocket socket;
	private DatagramPacket pkg_obj;
	private DatagramPacket pag_msg;
	private String ip;
	private String path;
	private String receiverName;
	private int serverPort;
	private int clientPort;

	public FileReceiver(String ip, String path, String receiverName,
			int serverPort, int clientPort) {
		super();
		this.ip = ip;
		this.path = path;
		this.receiverName = receiverName;
		this.serverPort = serverPort;
		this.clientPort = clientPort;
	}

	public String getIp() {
		return ip;
	}

	public String getPath() {
		return path;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public int getClientPort() {
		return clientPort;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}

	public void receive() {
		try {
			// 接收文件监听端口
			socket = new DatagramSocket(serverPort);
			// 写文件路径
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(new File(path)));

			// 读取文件包
			byte[] buf = new byte[1024 * 63];
			pkg_obj = new DatagramPacket(buf, buf.length);
			// 发送收到文件后 确认信息包
			byte[] messagebuf = new byte[1024];
			messagebuf = NetConfig.I_OK.getBytes();
			pag_msg = new DatagramPacket(messagebuf, messagebuf.length,
					new InetSocketAddress(ip, clientPort));
			// 循环接收包，每接到一个包后回给对方一个确认信息，对方才发下一个包(避免丢包和乱序)，直到收到一个结束包后跳出循环，结束文件传输，关闭流
			while (true) {
				socket.receive(pkg_obj);
				if (new String(pkg_obj.getData(), 0, pkg_obj.getLength())
						.equals("end")) {
					System.out.println("文件接收完毕");
					bos.close();
					socket.close();
					break;
				}
				socket.send(pag_msg);
				System.out.println(new String(pag_msg.getData()));
				bos.write(pkg_obj.getData(), 0, pkg_obj.getLength());
				bos.flush();
			}
			bos.close();
			socket.close();
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
		receive();
	}
}
