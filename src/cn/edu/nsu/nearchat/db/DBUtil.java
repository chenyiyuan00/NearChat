package cn.edu.nsu.nearchat.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

	private static final String JDBC_DRIVER = LoadDBConfig.CLASS_NAME;
	private static final String DB_URL = LoadDBConfig.DATABASE_URL + "://"
			+ LoadDBConfig.SERVER_IP + ":" + LoadDBConfig.SERVER_PORT + "/"
			+ LoadDBConfig.DATABASE_SID;
	private static final String USER = LoadDBConfig.USERNAME;
	private static final String PASS = LoadDBConfig.PASSWORD;

	private static Connection conn = null;

	public static Connection getConnection() {
		try {
			Class.forName(JDBC_DRIVER);
			try {
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Load driver failed!");
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeDB(ResultSet resultSet, Statement statement,
			Connection connection) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeDB(Statement statement, Connection connection) {

		try {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
