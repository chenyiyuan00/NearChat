package cn.edu.nsu.nearchat.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetHelper {

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
