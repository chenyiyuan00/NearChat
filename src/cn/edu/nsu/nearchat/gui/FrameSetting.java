package cn.edu.nsu.nearchat.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

public class FrameSetting {

	private static final int TASKBAR_HEIGHT = 40;

	public static Point getCenterLocal(int w, int h) {
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension screen = tool.getScreenSize();
		int local_X = (int) (screen.getWidth() - w) / 2;
		int local_Y = (int) (screen.getHeight() - h - TASKBAR_HEIGHT) / 2;
		Point point = new Point(local_X, local_Y);
		return point;
	}

}
