package cn.edu.nsu.nearchat.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {

	public String receiveText() {
		String msg;
		byte buff[] = new byte[1024 * 4];
		DatagramPacket packet = new DatagramPacket(buff, buff.length);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(9999);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				socket.receive(packet);
				msg = new String(buff, 0, packet.getLength());
				return msg;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				socket.close();
			}
		}

	}

	public static void main(String[] args) {
		byte buff[] = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buff, buff.length);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(9999);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				socket.receive(packet);
				System.out.println(new String(buff, 0, packet.getLength()));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				socket.close();
			}
		}
	}
}
