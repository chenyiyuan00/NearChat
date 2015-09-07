package cn.edu.nsu.nearchat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import cn.edu.nsu.nearchat.db.DBUtil;
import cn.edu.nsu.nearchat.helper.NetHelper;
import cn.edu.nsu.nearchat.helper.XmlHelper;
import cn.edu.nsu.nearchat.resource.Res;

public class UserDao {

	private Connection connection;
	private Statement statement;
	private PreparedStatement pstmt;
	private ResultSet resultSet;

	static HashMap<String, Timestamp> session = new HashMap<>();
	static ArrayList<String> online = new ArrayList<>();

	// loginName 是否存在
	public boolean isExist(String loginName) {
		boolean is = false;
		String sql_select_by_name = "SELECT u_loginname FROM users WHERE u_loginname=?;";
		connection = DBUtil.getConnection();
		try {
			pstmt = connection.prepareStatement(sql_select_by_name);
			pstmt.setString(1, loginName);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				String res = resultSet.getString(1);
				if (res.equals(loginName)) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(resultSet, pstmt, connection);
		}
		return is;
	}

	// loginName是否与密码匹配
	public boolean userNameIsMatchPassword(String loginName, String password) {
		boolean is = false;
		String select_loginname_and_password = "SELECT u_loginname,u_password FROM users WHERE u_loginname=?;";
		connection = DBUtil.getConnection();
		UserDao dao = new UserDao();
		if (dao.isExist(loginName)) {
			try {
				pstmt = connection
						.prepareStatement(select_loginname_and_password);
				pstmt.setString(1, loginName);
				resultSet = pstmt.executeQuery();
				while (resultSet.next()) {
					String user = resultSet.getString(1);
					String pass = resultSet.getString(2);
					if (user.equals(loginName) && pass.equals(password)) {
						is = true;
						return is;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.closeDB(resultSet, pstmt, connection);
			}
		}
		return is;
	}

	/**
	 * @isOff 是否离线 离线修改为当前
	 * @minute session的分钟数
	 * */
	public void changeSession(int status, String loginName, int minute) {
		String sql_modify_session = "UPDATE users SET u_session=? WHERE u_loginname=?;";
		connection = DBUtil.getConnection();
		try {
			pstmt = connection.prepareStatement(sql_modify_session);
			long now = new Date().getTime();
			Timestamp time = null;
			if (0 == status) {
				time = new Timestamp(now);
			} else if (1 == status) {
				time = new Timestamp(now + 1000 * 60 * minute);
			}
			pstmt.setTimestamp(1, time);
			pstmt.setString(2, loginName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * users 联系人loginName数组 session 一个key为loginName,value为Timestamp
	 * */
	public HashMap<String, Timestamp> getSession(String[] users) {
		String select_loginname_and_session = "SELECT u_loginname,u_session FROM users";
		connection = DBUtil.getConnection();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(select_loginname_and_session);
			while (resultSet.next()) {
				String loginName = resultSet.getString(1);
				Timestamp sessionTime = resultSet.getTimestamp(2);
				for (String user : users) {
					if (user.equals(loginName)) {
						session.put(loginName, sessionTime);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(resultSet, statement, connection);
		}
		return session;
	}

	// session status IP XML
	public int LoginOrLogout(String loginName, Boolean isLogin) {
		String sql_update = "UPDATE users SET u_status=?,u_ip=?,u_session=? WHERE u_loginname=?";
		connection = DBUtil.getConnection();
		try {
			pstmt = connection.prepareStatement(sql_update);
			Timestamp time = null;
			if (isLogin) {
				pstmt.setInt(1, 1);// 1 OR 0 代表状态
				pstmt.setString(2, NetHelper.getLocalIP());
				long sessionTime = new Date().getTime() + 1000 * 60 * 20;// 20分钟
				time = new Timestamp(sessionTime);
				pstmt.setTimestamp(3, time);
			} else {
				pstmt.setInt(1, 0);// 1 OR 0 代表状态
				pstmt.setString(2, Res.V_LOCALIP);
				pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			}
			pstmt.setString(4, loginName);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(pstmt, connection);
		}
		return -1;
	}

	public ArrayList<String> isOnline(ArrayList<String> all) {
		String sql = "SELECT u_loginname,u_session FROM users";
		connection = DBUtil.getConnection();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			int count = 0;
			while (resultSet.next()) {
				String loginName = resultSet.getString(1);
				while (loginName.equals(all.get(count++))) {
					Timestamp session = resultSet.getTimestamp(2);
					if (session.after(new Date())) {
						online.add(loginName);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(resultSet, statement, connection);
		}
		return online;
	}

	public ArrayList<String> getOnlineLineman(ArrayList<String> all) {
		ArrayList<String> online = new ArrayList<>();
		String sql_select = "SELECT u_status,u_loginname,u_session FROM users;";
		all = XmlHelper.getAllLinkman();
		connection = DBUtil.getConnection();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql_select);
			while (resultSet.next()) {
				int status = resultSet.getInt(1);
				if (1 == status) {
					Timestamp stamp = resultSet.getTimestamp(3);
					if (new Date().before(stamp)) {
						String loginName = resultSet.getString(2);
						if (all.contains(loginName)) {
							online.add(loginName);
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return online;
	}

	public Vector<String> getLanLinkman() {
		Vector<String> lanLinkman = new Vector<>();
		String select_all = "SELECT u_loginname FROM users;";
		connection = DBUtil.getConnection();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(select_all);
			while (resultSet.next()) {
				String linkmanName = resultSet.getString(1);
				if (!XmlHelper.getConf(Res.V_UN).equals(linkmanName)) {
					lanLinkman.add(linkmanName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(resultSet, statement, connection);
		}
		return lanLinkman;
	}

	public String getIP(String loginName) {
		String address = null;
		String sql_select = "SELECT u_ip FROM users WHERE u_loginname=?;";
		connection = DBUtil.getConnection();
		try {
			pstmt = connection.prepareStatement(sql_select);
			pstmt.setString(1, loginName);
			resultSet = pstmt.executeQuery();
			resultSet.next();
			address = resultSet.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return address;
	}
}
