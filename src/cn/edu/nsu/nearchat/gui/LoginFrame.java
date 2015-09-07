package cn.edu.nsu.nearchat.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import cn.edu.nsu.nearchat.dao.UserDao;
import cn.edu.nsu.nearchat.db.DBImage;
import cn.edu.nsu.nearchat.helper.XmlHelper;
import cn.edu.nsu.nearchat.resource.Res;

public class LoginFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 415313592339655616L;

	private static final int WINDOW_WIDTH = 400;
	private static final int WINDOW_HEIGHT = 250;

	private static final String USER_LOGINING = "正在登录...";
	private static final String USER_OF_PASSWORD_IS_NULL = "用户名或密码为空";
	private static final String USER_OR_PASSWORD_ERROR = "用户名或密码错误";

	public static String loginName = null;

	private JLabel userNameLabel, passwordLabel;
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JCheckBox rememberPasswordCheckBox;
	private JButton loginButton;
	private JLabel addAcountLabel, loginInfoLabel, findPasswordLabel;

	public LoginFrame(boolean has) {
		setTitle("用户登录");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocation(FrameSetting.getCenterLocal(WINDOW_WIDTH, WINDOW_HEIGHT));
		setLayout(null);

		userNameLabel = new JLabel("用户:");
		userNameLabel.setBounds(100, 50, 40, 30);
		add(userNameLabel);
		userNameField = new JTextField();
		userNameField.setBounds(140, 50, 150, 30);
		add(userNameField);

		passwordLabel = new JLabel("密码:");
		passwordLabel.setBounds(100, 80, 40, 30);
		add(passwordLabel);
		passwordField = new JPasswordField();
		passwordField.setBounds(140, 80, 150, 30);
		add(passwordField);

		loginButton = new JButton("登录");
		loginButton.setBounds(140, 110, 150, 30);
		add(loginButton);

		loginButton.addActionListener(this);

		rememberPasswordCheckBox = new JCheckBox("记住密码");
		rememberPasswordCheckBox.setBounds(300, 80, 80, 30);
		add(rememberPasswordCheckBox);

		addAcountLabel = new JLabel("注册一个账户");
		addAcountLabel.setBounds(50, 180, 100, 30);
		add(addAcountLabel);
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getSource().equals(addAcountLabel)) {
					// TODO
				}
			}

		});

		findPasswordLabel = new JLabel("找回密码...");
		findPasswordLabel.setBounds(300, 180, 80, 30);
		add(findPasswordLabel);

		loginInfoLabel = new JLabel();
		loginInfoLabel.setBounds(100, 180, 120, 30);
		add(loginInfoLabel);

		if (has) {
			userNameField.setText(XmlHelper.getConf("userName"));
			passwordField.setText(XmlHelper.getConf("password"));
			rememberPasswordCheckBox.setSelected(true);
		} else {
			rememberPasswordCheckBox.setSelected(false);
		}

		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(loginButton)) {
			boolean isMatch = false, isNull;
			UserDao dao = new UserDao();
			String userName = userNameField.getText().trim();
			String password = new String(passwordField.getPassword()).trim();

			isNull = userName.equals("") || userName == null
					|| password.equals("") || password == null;
			isMatch = dao.userNameIsMatchPassword(userName, password);
			if (isMatch) {
				loginName = userName;
				new Thread() {
					@Override
					public void run() {
						new UserDao().LoginOrLogout(loginName, true);
					}
				}.start();
				File groupsInfo = new File(Res.INFO_T);
				if (groupsInfo.exists()) {
					groupsInfo.delete();
					new DBImage().getImage(loginName, Res.INFO_T);
				}
				boolean isRemember = rememberPasswordCheckBox.isSelected();
				if (isRemember) {
					XmlHelper.setConf(Res.V_IR, "true");
					XmlHelper.setConf(Res.V_UN, userName);
					XmlHelper.setConf(Res.V_PW, password);
				} else {
					XmlHelper.setConf(Res.V_IR, Res.V_NU);
					XmlHelper.setConf(Res.V_UN, Res.V_NU);
				}
				loginInfoLabel.setText(USER_LOGINING);
				setVisible(false);
				new LinkmanFrame(loginName);
				dispose();
			} else if (isNull) {
				loginInfoLabel.setText(USER_OF_PASSWORD_IS_NULL);
			} else {
				loginInfoLabel.setText(USER_OR_PASSWORD_ERROR);
			}
		}
	}
}
