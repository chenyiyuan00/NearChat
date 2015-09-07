package cn.edu.nsu.nearchat.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadDBConfig {

	private static Properties properties = new Properties();

	static String filePath = "dbconfig.properties";

	static {
		try {
			InputStream in = new FileInputStream(filePath);
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String CLASS_NAME = properties.getProperty("CLASS_NAME");
	public static String DATABASE_URL = properties.getProperty("DATABASE_URL");
	public static String SERVER_IP = properties.getProperty("SERVER_IP");
	public static String SERVER_PORT = properties.getProperty("SERVER_PORT");
	public static String DATABASE_SID = properties.getProperty("DATABASE_SID");
	public static String USERNAME = properties.getProperty("USERNAME");
	public static String PASSWORD = properties.getProperty("PASSWORD");

	public String getProperie(String prop) {
		String result = null;
		result = properties.getProperty(prop);
		return result;
	}

}
