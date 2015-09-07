package cn.edu.nsu.nearchat.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class StringHelper {

	// 模糊查询
	public static Vector<String> like(Vector<String> res, String prefix) {
		Vector<String> list = new Vector<>();
		for (String source : res) {
			boolean isLike = source.startsWith(prefix)
					|| source.contains(prefix)
					|| source.equalsIgnoreCase(prefix)
					|| source.endsWith(prefix);
			if (isLike) {
				list.add(source);
			}
		}
		return list;
	}

	// 获取时间字符串HH:mm
	public static String getNowTime() {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm");
		return format.format(new Date());
	}
}
