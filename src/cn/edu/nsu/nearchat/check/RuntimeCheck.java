package cn.edu.nsu.nearchat.check;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.channels.FileChannel;

import cn.edu.nsu.nearchat.db.DBImage;
import cn.edu.nsu.nearchat.helper.XmlHelper;
import cn.edu.nsu.nearchat.resource.Res;

public class RuntimeCheck {

	// 创建设置XML文件
	public static void createRuntime() {
		String userPath = System.getenv().get("USERPROFILE");
		String appPath = userPath + "\\." + Res.AppName;
		updateRes("SET_T", appPath + "\\" + Res.SET_S);
		updateRes("INFO_T", appPath + "\\" + Res.INFO_S);
		File folder = new File(appPath);
		String localPath = folder.getAbsolutePath();
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File setFile_t = new File(Res.SET_T);
		if (!setFile_t.exists()) {
			RuntimeCheck.copyFromRes(Res.SET_S, Res.SET_T);
		}
		XmlHelper.setConf("localPath", localPath);
	}

	// 更新分组信息XML文件
	public static void updateGroupsInfo(String name) {
		new DBImage().getImage(name, Res.INFO_T);
	}

	// 从资源包中复制内容为空的XML文件
	public static void copyFromRes(String res, String aim) {
		InputStream is = null;
		FileOutputStream fos = null;
		int r = 0;
		try {
			is = ClassLoader.getSystemResourceAsStream(res);
			fos = new FileOutputStream(aim);
			while ((r = is.read()) != -1) {
				fos.write(r);
			}
			fos.flush();
			fos.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 使用反射修改Res类中的静态常量
	public static void updateRes(String constant, String value) {
		Field field_S = null;
		try {
			field_S = Res.class.getField(constant);
			Field modify = Field.class.getDeclaredField("modifiers");
			field_S.setAccessible(true);
			modify.setAccessible(true);
			modify.set(field_S, field_S.getModifiers() & ~Modifier.FINAL);
			field_S.set(null, value);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// 利用文件锁
	public static boolean isRunning() {
		boolean isRunning = false;
		File info = new File(Res.INFO_T);
		FileInputStream fis = null;
		FileChannel channel = null;
		try {
			fis = new FileInputStream(info);
			channel = fis.getChannel();
			channel.tryLock();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				channel.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// TODO
		return isRunning;
	}
}
