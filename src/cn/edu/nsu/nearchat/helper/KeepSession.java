package cn.edu.nsu.nearchat.helper;

import java.util.TimerTask;

import cn.edu.nsu.nearchat.dao.UserDao;
import cn.edu.nsu.nearchat.gui.LoginFrame;

public class KeepSession extends TimerTask {

	@Override
	public void run() {
		new UserDao().LoginOrLogout(LoginFrame.loginName, true);
	}

}
