package cn.edu.nsu.nearchat.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cn.edu.nsu.nearchat.check.RuntimeCheck;
import cn.edu.nsu.nearchat.resource.Res;

public class DBImage {

	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet resultSet;

	// 更新文件
	public void putImage(String to, String path) {
		String update_group = "UPDATE users SET u_group=? WHERE u_loginname=?;";
		File file;
		InputStream imageIS = null;
		try {
			connection = DBUtil.getConnection();
			pstmt = connection.prepareStatement(update_group);
			file = new File(path);
			imageIS = new FileInputStream(file);
			pstmt.setBinaryStream(1, imageIS, (int) file.length());
			pstmt.setString(2, to);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(pstmt, connection);
			try {
				imageIS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 从数据库中保存文件
	public void getImage(String from, String path) {
		String select_group_where_loginname = "SELECT u_group FROM users WHERE u_loginname=?;";
		FileOutputStream fos = null;
		InputStream is = null;
		byte[] buffer = new byte[1024 * 4];
		try {
			connection = DBUtil.getConnection();
			pstmt = connection.prepareStatement(select_group_where_loginname);
			pstmt.setString(1, from);
			resultSet = pstmt.executeQuery();
			resultSet.next();
			is = resultSet.getBinaryStream(1);
			if (null == is) {
				System.out.println("没有找到groupsInfo");
				new File(Res.INFO_T).deleteOnExit();
				System.out.println("Delete");
				RuntimeCheck.copyFromRes(Res.INFO_S, Res.INFO_T);
			} else {
				File file = new File(path);
				fos = new FileOutputStream(file);
				int size = 0;
				while ((size = is.read(buffer)) != -1) {
					fos.write(buffer, 0, size);
				}
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeDB(resultSet, pstmt, connection);
		}
	}
}
