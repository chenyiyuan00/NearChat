package cn.edu.nsu.nearchat.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import cn.edu.nsu.nearchat.helper.StringHelper;

public class SendMsgFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 5436400232103025792L;

	private static int WINDOW_WIDTH = 600;
	private static int WINDOW_HEIGHT = 500;

	private JSplitPane splitPane;
	private JScrollPane historyScrollPane, inputScrollPane;
	private JTextArea msgHistoryArea, msgInpuArea;
	private JButton fileButton, recoderButton, sendButton, clsButton;
	private JLabel infoLabel;
	private JPanel panel_S;

	public SendMsgFrame(String sendTo) {
		super(sendTo);
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		msgHistoryArea = new JTextArea();
		msgHistoryArea.setFont(new Font("仿宋", Font.PLAIN, 16));
		msgHistoryArea.setLineWrap(true);
		msgHistoryArea.setEditable(false);
		msgHistoryArea.setToolTipText("聊天记录");
		historyScrollPane = new JScrollPane(msgHistoryArea);
		msgInpuArea = new JTextArea();
		msgInpuArea.setLineWrap(true);
		msgInpuArea.setToolTipText("输入你想发送的消息");
		msgInpuArea.setWrapStyleWord(true);
		inputScrollPane = new JScrollPane(msgInpuArea);
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				historyScrollPane, inputScrollPane);
		splitPane.setDividerLocation(300);

		panel_S = new JPanel(new FlowLayout(10));
		infoLabel = new JLabel();
		infoLabel.setForeground(Color.RED);
		sendButton = new JButton("发送");
		fileButton = new JButton("文件");
		recoderButton = new JButton("录制");
		clsButton = new JButton("清屏");
		clsButton.addActionListener(new ButtonAction());
		fileButton.addActionListener(new ButtonAction());
		recoderButton.addActionListener(new ButtonAction());
		sendButton.addActionListener(new ButtonAction());
		panel_S.add(infoLabel, FlowLayout.LEFT);
		panel_S.add(sendButton, FlowLayout.LEFT);
		panel_S.add(fileButton, FlowLayout.LEFT);
		panel_S.add(recoderButton, FlowLayout.LEFT);
		panel_S.add(clsButton, FlowLayout.LEFT);

		add(splitPane, BorderLayout.CENTER);
		add(panel_S, BorderLayout.SOUTH);
		setVisible(true);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocation(FrameSetting.getCenterLocal(WINDOW_WIDTH, WINDOW_WIDTH));
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	// 添加消息到消息历史框
	public void appendMsg(String msg) {
		msgHistoryArea.append(msg);
		String start = "@when " + StringHelper.getNowTime();
		msgHistoryArea.append(start + " Me Say: \r\n" + msg + "\r\n" + "\r\n");
		msgHistoryArea.setCaretPosition(msgHistoryArea.getText().length());
	}

	//
	public void hintMsg(String hint) {
		infoLabel.setText(hint);
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

	class ButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object event = e.getSource();
			if (event.equals(clsButton)) {
				msgHistoryArea.setText("");
			} else if (event.equals(fileButton)) {
				String path = new File("").getAbsolutePath();
				JFileChooser fileChooser = new JFileChooser(path);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.showOpenDialog(null);
				File[] files = fileChooser.getSelectedFiles();
				hintMsg("选择了" + files.length + "个文件!");
				StringBuilder filesName = new StringBuilder();
				for (File file : files) {
					filesName.append("@file# " + file.getName() + "\r\n");
				}
				msgInpuArea.setText(filesName.toString());
			} else if (event.equals(recoderButton)) {

			} else if (event.equals(sendButton)) {
				String text = msgInpuArea.getText();
				if (0 == text.getBytes().length) {

				} else if (text.getBytes().length > 1024 * 4) {
					hintMsg("文本过长！");
				} else {
					infoLabel.setText("");
					msgInpuArea.setText("");
					appendMsg(text);
				}
			}
		}
	}

	@Override
	public void run() {
		// TODO
	}

}
