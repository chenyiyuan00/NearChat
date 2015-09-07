package cn.edu.nsu.nearchat.helper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import cn.edu.nsu.nearchat.resource.Res;

public class LogHelper {

	static Logger err_log = Logger.getLogger("NearChatErr");
	static Logger msg_log = Logger.getLogger("NearChatMsg");

	// 区分消息记录与错误记录
	public LogHelper(Logger log, String type, Level level, String msg) {
		super();
		if (type.equalsIgnoreCase(Res.LOG_TYPR_MSG)) {
			try {
				FileHandler handler = new FileHandler(Res.ERR_LOG, true);
				handler.setFormatter(new SimpleFormatter());
				log.addHandler(handler);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writeLog(log, level, msg);
		} else if (type.equalsIgnoreCase(Res.LOG_TYPR_ERR)) {
			File filePath = new File(Res.MSG_LOG);
			filePath.mkdirs();
			String logName = Res.MSG_LOG + LogHelper.logNameByDate();
			System.out.println(logName);
			FileHandler handler;
			try {
				handler = new FileHandler(logName, true);
				handler.setFormatter(new SimpleFormatter());
				log.addHandler(handler);
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
			writeLog(log, level, msg);
		}

	}

	public static void writeLog(Logger log, Level level, String msg) {
		if (level.equals(Level.SEVERE)) {
			log.severe(msg);
		} else if (level.equals(Level.WARNING)) {
			log.warning(msg);
		} else if (level.equals(Level.INFO)) {
			log.info(msg);
		}
	}

	public static String logNameByDate() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
		return format.format(now) + ".log";
	}

	public static void main(String[] args) {
		new LogHelper(err_log, Res.LOG_TYPR_ERR, Level.SEVERE, "Servers");
		new LogHelper(msg_log, Res.LOG_TYPR_MSG, Level.WARNING, "Warning");
	}

}
