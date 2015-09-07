package cn.edu.nsu.nearchat.main;

import java.awt.EventQueue;

import cn.edu.nsu.nearchat.check.RuntimeCheck;
import cn.edu.nsu.nearchat.gui.LoginFrame;
import cn.edu.nsu.nearchat.helper.XmlHelper;
import cn.edu.nsu.nearchat.resource.Res;

public class NearChat {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				RuntimeCheck.createRuntime();
				String isremember = XmlHelper.getConf(Res.V_IR);
				new LoginFrame(isremember.equals("true"));
			}
		});
	}
}
