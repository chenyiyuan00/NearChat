package cn.edu.nsu.nearchat.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {

	public static void main(String[] args) {
		byte buff[] = args[0].getBytes();
		File file = new File("info.txt");
		try (FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis)) {
			ois.read(buff);
		} catch (FileNotFoundException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		InetSocketAddress address = new InetSocketAddress(getLocalIP(), 9999);
		DatagramPacket packet;
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(5678);
			packet = new DatagramPacket(buff, buff.length, address);
			socket.send(packet);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			socket.close();
		}
	}

	public static String getLocalIP() {
		String IP = null;
		try {
			InetAddress address = InetAddress.getLocalHost();
			IP = address.getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return IP;
	}

}
