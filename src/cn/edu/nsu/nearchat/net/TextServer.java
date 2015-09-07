package cn.edu.nsu.nearchat.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import cn.edu.nsu.nearchat.resource.Res;

public class TextServer extends Thread {

	private String from;
	private String to;
	private String text;

	public TextServer(String from, String to, String text) {
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

	public void receiveText() {
		byte buff[] = new byte[1024 * 4];
		DatagramPacket packet = new DatagramPacket(buff, buff.length);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(Res.R_PROT);
			while (true) {
				socket.receive(packet);
				System.out.println(new String(buff, 0, packet.getLength()));
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		receiveText();
	}
}
