package cn.edu.nsu.nearchat.testcase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import cn.edu.nsu.nearchat.gui.SendMsgFrame;

public class TestThreadLocal {

	public static ThreadLocal<SendMsgFrame> msgFrameThreadLocal = new ThreadLocal<>();
	public static AtomicInteger unique = new AtomicInteger(0);
	public static Map<String, SendMsgFrame> sendMsgMap = new HashMap<>();

	public static void main(String[] args) {
		for (int i = 0; i < 11; i++) {
			SendMsgFrame frame = new SendMsgFrame("" + i);
			sendMsgMap.put(frame.getTitle(), frame);
		}
		System.out.println(sendMsgMap.get("" + 10).getTitle());
		sendMsgMap.get("" + 5).requestFocusInWindow();
	}

}
