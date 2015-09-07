package cn.edu.nsu.nearchat.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import cn.edu.nsu.nearchat.dao.UserDao;
import cn.edu.nsu.nearchat.db.DBImage;
import cn.edu.nsu.nearchat.helper.KeepSession;
import cn.edu.nsu.nearchat.helper.StringHelper;
import cn.edu.nsu.nearchat.helper.XmlHelper;
import cn.edu.nsu.nearchat.resource.Res;

public class LinkmanFrame extends JFrame {

	public static ThreadLocal<SendMsgFrame> threadLocal = new ThreadLocal<>();
	public static Map<String, SendMsgFrame> map = new HashMap<>();

	private static final int WINDOW_WIDTH = 350;
	private static final int WINDOW_HEIGHT = 600;
	private static final int WINDOW_MIN_WIDTH = 350;
	private static final int WINDOW_MIN_HEIGHT = 400;

	private Timer timer;// KeepSession

	private JPanel panel_N, panel_C, panel_S;
	private JTextField searchField;
	private JButton searchButton, exitMeButton;
	private JScrollPane likemaScrollPane;
	private static JTree userGroupTree;
	private static DefaultTreeModel newModel;
	private JButton setButton, findUserButton, msgRecordButton;
	private JLabel infoLabel;
	private JList<String> userList;
	private JPopupMenu popupMenu;
	private TreeModel topTreeModel;
	private static JMenuItem U_send, U_move, U_modify, U_delete;
	private static JMenuItem G_add, G_delete, G_modify, G_broadcase;

	private static final long serialVersionUID = -7187116852665976625L;

	public LinkmanFrame(String title) {
		super(title);
		init();
		timer = new Timer();
		timer.schedule(new KeepSession(), new Date(), Res.SESSION_TIME);
	}

	private void init() {
		setLayout(new BorderLayout());

		panel_N = new JPanel(new FlowLayout(20));
		panel_C = new JPanel(new BorderLayout());
		panel_S = new JPanel(new FlowLayout());

		searchField = new JTextField(10);
		searchButton = new JButton("返回/搜索");
		searchButton.setToolTipText("按登录名在联系人中搜索");
		exitMeButton = new JButton("退出登录");
		panel_N.add(searchField);
		panel_N.add(searchButton);
		panel_N.add(exitMeButton);
		searchButton.addActionListener(new ButtonAction());
		exitMeButton.addActionListener(new ButtonAction());

		DefaultMutableTreeNode top = XmlHelper.getUserGroups();
		topTreeModel = new DefaultTreeModel(top);
		userGroupTree = new JTree(topTreeModel);

		NodeRenderer renderer = new NodeRenderer();
		userGroupTree.setCellRenderer(renderer);

		userGroupTree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (userGroupTree.getSelectionPath() != null) {
					final Object[] objPath = userGroupTree.getSelectionPath()
							.getPath();
					if (3 == objPath.length && 2 == e.getClickCount()) {
						new Thread() {
							@Override
							public void run() {
								new SendMsgFrame(objPath[2].toString());
							};
						}.start();
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (userGroupTree.getSelectionPath() != null) {
					int length = userGroupTree.getSelectionPath().getPath().length;
					popupMenu = showPopupMenu(length);
					if (e.isPopupTrigger())
						popupMenu.show((JComponent) e.getSource(), e.getX(),
								e.getY());
				}
			}

		});

		likemaScrollPane = new JScrollPane(userGroupTree);
		panel_C.add(likemaScrollPane, BorderLayout.CENTER);

		infoLabel = new JLabel();
		infoLabel.setForeground(Color.red);
		setButton = new JButton("设置");
		setButton.addActionListener(new ButtonAction());
		findUserButton = new JButton("查找联系人");
		findUserButton.addActionListener(new ButtonAction());
		findUserButton.setToolTipText("按一定条件查找一个没添加的用户");
		msgRecordButton = new JButton("日志");
		msgRecordButton.addActionListener(new ButtonAction());
		msgRecordButton.setToolTipText("查看消息记录");
		panel_S.add(infoLabel);
		panel_S.add(setButton);
		panel_S.add(findUserButton);
		panel_S.add(msgRecordButton);

		add(BorderLayout.NORTH, panel_N);
		add(BorderLayout.CENTER, panel_C);
		add(BorderLayout.SOUTH, panel_S);

		setLocation(FrameSetting.getCenterLocal(WINDOW_WIDTH, WINDOW_HEIGHT));
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension min = new Dimension(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT);
		setMinimumSize(min);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new UserDao().LoginOrLogout(LoginFrame.loginName, false);
				new DBImage().putImage(LoginFrame.loginName, Res.INFO_T);
			}
		});
	}

	public JPopupMenu showPopupMenu(int lenth) {
		popupMenu = new JPopupMenu();
		if (3 == lenth) {
			U_send = new JMenuItem("发送消息");
			U_move = new JMenuItem("改变分组");
			U_modify = new JMenuItem("修改信息");
			U_delete = new JMenuItem("移除好友");
			U_send.addActionListener(new ButtonAction());
			U_move.addActionListener(new ButtonAction());
			U_modify.addActionListener(new ButtonAction());
			U_delete.addActionListener(new ButtonAction());
			popupMenu.add(U_send);
			popupMenu.add(U_move);
			popupMenu.add(U_modify);
			popupMenu.add(U_delete);
		} else if (2 == lenth) {
			G_add = new JMenuItem("添加分组");
			G_delete = new JMenuItem("删除分组");
			G_modify = new JMenuItem("修改分组");
			G_broadcase = new JMenuItem("广播消息");
			G_add.addActionListener(new ButtonAction());
			G_delete.addActionListener(new ButtonAction());
			G_modify.addActionListener(new ButtonAction());
			G_broadcase.addActionListener(new ButtonAction());
			popupMenu.add(G_add);
			popupMenu.add(G_delete);
			popupMenu.add(G_modify);
			popupMenu.add(G_broadcase);
		} else if (1 == lenth) {
			G_add = new JMenuItem("添加分组");
			G_add.addActionListener(new ButtonAction());
			popupMenu.add(G_add);
		}
		return popupMenu;
	}

	private static void updateUserGroupTreeUI() {
		newModel = new DefaultTreeModel(XmlHelper.getUserGroups());
		userGroupTree.setModel(newModel);
		userGroupTree.updateUI();
		userGroupTree.repaint();
	}

	class ButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object event = e.getSource();
			if (event.equals(searchButton)) {
				String prefix = searchField.getText().trim();
				if (!prefix.equals("") || prefix == null) {
					panel_C.removeAll();
					Vector<String> list = StringHelper.like(
							XmlHelper.getLinkman(), prefix);
					if (XmlHelper.getLinkman().isEmpty()) {
						JOptionPane.showMessageDialog(getParent(), "你还没有任何好友！");
					}
					userList = new JList<String>(list);
					panel_C.add(userList, BorderLayout.CENTER);
					userList.updateUI();
					repaint();
					userList.addMouseListener(new MouseAdapter() {

						@Override
						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount() == 2) {
								String sendTo = userList.getSelectedValue();
								new SendMsgFrame(sendTo);
							}
						}
					});
					panel_C.add(userList, BorderLayout.CENTER);
					repaint();
				} else {
					panel_C.removeAll();
					panel_C.add(userGroupTree);
					repaint();
				}
			} else if (event.equals(exitMeButton)) {
				setVisible(false);
				new UserDao().LoginOrLogout(LoginFrame.loginName, false);
				dispose();
				new LoginFrame(XmlHelper.getConf(Res.V_IR).equals("true"));
			} else if (event.equals(setButton)) {

			} else if (event.equals(findUserButton)) {
				panel_C.removeAll();
				UserDao dao = new UserDao();
				userList = new JList<>(dao.getLanLinkman());
				userList.setToolTipText("双击添加到分组！");
				userList.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2) {
							String addTo = userList.getSelectedValue();
							String group = JOptionPane
									.showInputDialog("添加到分组(没有就创建)!");
							int state = XmlHelper.addUserTOGroup(group, addTo);
							if (1 == state) {
								infoLabel.setText("该用户已经在联系人中");
								new Thread() {
									@Override
									public void run() {
										try {
											sleep(2000);
											infoLabel.setText("");
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									};
								}.start();
							}
							updateUserGroupTreeUI();
						}
					}

				});
				panel_C.add(userList, BorderLayout.CENTER);
				userList.updateUI();
			} else if (event.equals(msgRecordButton)) {

			} else if (event.equals(U_send)) {
				String sendTo = userGroupTree.getSelectionPath().getPath()[2]
						.toString();
				SendMsgFrame sendMsg = new SendMsgFrame(sendTo);
				map.put(sendTo, sendMsg);
			} else if (event.equals(U_move)) {
				String uName = userGroupTree.getSelectionPath().getPath()[2]
						.toString();
				String alias = JOptionPane.showInputDialog("输入目标分组(没有创建)!");
				XmlHelper.moveUserToOtherGroup(uName, alias);
				updateUserGroupTreeUI();
			} else if (event.equals(U_modify)) {

			} else if (event.equals(U_delete)) {

			} else if (event.equals(G_add)) {
				String groupAlias = JOptionPane.showInputDialog("输入分组别名!");
				if (groupAlias != null) {
					XmlHelper.addGroup(groupAlias);
				}
				updateUserGroupTreeUI();
			} else if (event.equals(G_delete)) {
				String alias = userGroupTree.getSelectionPath().getPath()[1]
						.toString();
				XmlHelper.deleteGroup(alias);
				updateUserGroupTreeUI();
			} else if (event.equals(G_modify)) {
				String alias = userGroupTree.getSelectionPath().getPath()[1]
						.toString();
				String realias = JOptionPane.showInputDialog("输入新的别名！");
				XmlHelper.modifyGroup(alias, realias);
				updateUserGroupTreeUI();
			} else if (event.equals(G_broadcase)) {
				String toGroup = userGroupTree.getSelectionPath().getPath()[1]
						.toString();
				new SendMsgFrame(toGroup + "(广播)");
			}
		}
	}

	class NodeRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = -5345510458834242886L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);
			String nodeName = node.getUserObject().toString().trim();
			UserDao dao = new UserDao();
			if (node.isLeaf()
					&& dao.getOnlineLineman(XmlHelper.getAllLinkman())
							.contains(nodeName)) {
				setForeground(Color.green);
			}
			return this;
		}
	}
}
