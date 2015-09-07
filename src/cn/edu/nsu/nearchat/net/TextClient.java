package cn.edu.nsu.nearchat.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import cn.edu.nsu.nearchat.dao.UserDao;
import cn.edu.nsu.nearchat.resource.Res;

public class TextClient extends Thread {

	private String from;
	private String to;
	private String text;

	public TextClient(String from, String to, String text) {
		super();
		this.from = from;
		this.to = to;
		this.text = text;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getText() {
		return text;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void sendText() {
		byte buff_msg[] = new byte[1024 * 4];
		InetSocketAddress address = null;
		DatagramPacket packet = null;
		DatagramSocket socket = null;
		String addr = new UserDao().getIP(to);
		System.out.println(addr);
		address = new InetSocketAddress(addr, Res.S_PROT);
		text = "@" + from + "@" + to + "@" + text;
		buff_msg = text.getBytes();
		try {
			System.out.println(text);
			socket = new DatagramSocket(Res.R_PROT);
			packet = new DatagramPacket(buff_msg, buff_msg.length, address);
			socket.send(packet);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			socket.close();
		}
	}

	@Override
	public void run() {
		sendText();
	}
}
