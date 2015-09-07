package cn.edu.nsu.nearchat.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import cn.edu.nsu.nearchat.resource.Res;

public class XmlHelper {

	public static Document getDoc(String path) {
		Document doc = null;
		SAXReader sax = new SAXReader();
		InputStream in = null;
		try {
			in = new FileInputStream(path);
			doc = sax.read(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doc;
	}

	public static void writerDoc(Document doc, String path) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		FileOutputStream out = null;
		XMLWriter writer = null;
		try {
			out = new FileOutputStream(path);
			format.setEncoding("UTF-8");
			writer = new XMLWriter(out, format);
			writer.write(doc);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static String getConf(String conf) {
		Document doc = XmlHelper.getDoc(Res.SET_T);
		Element root = doc.getRootElement();
		List<Element> confs = root.elements(conf);
		for (Element con : confs) {
			return con.getTextTrim();
		}
		return null;
	}

	public static void setConf(String config, String value) {
		Document doc = XmlHelper.getDoc(Res.SET_T);
		Element root = doc.getRootElement();
		Element conf = root.element(config);
		conf.setText(value);
		XmlHelper.writerDoc(doc, Res.SET_T);
	}

	public static void set(String xmlPath, String config, String value) {
		Document doc = XmlHelper.getDoc(xmlPath);
		Element root = doc.getRootElement();
		Element conf = root.element(config);
		conf.setText(value);
		XmlHelper.writerDoc(doc, xmlPath);
	}

	// 没有才添加
	@SuppressWarnings("unchecked")
	public static void addGroup(String gName) {
		boolean flag = false;
		Document doc = getDoc(Res.INFO_T);
		Element root = doc.getRootElement();
		List<Element> groups = root.elements(Res.V_G);
		for (Element group : groups) {
			if (group.getTextTrim().equals(gName)) {
				flag = true;
			}
		}
		if (!flag) {
			root.addElement(Res.V_G).setText(gName);
			XmlHelper.writerDoc(doc, Res.INFO_T);
		}
	}

	@SuppressWarnings("unchecked")
	public static void deleteGroup(String gName) {
		Document doc = getDoc(Res.INFO_T);
		Element root = doc.getRootElement();
		List<Element> groups = root.elements(Res.V_G);
		for (Element group : groups) {
			if (gName.equals(group.getTextTrim())) {
				root.remove(group);
			}
		}
		XmlHelper.writerDoc(doc, Res.INFO_T);
	}

	@SuppressWarnings({ "unchecked", })
	public static void modifyGroup(String old, String newly) {
		boolean flag = false;// 新的组名是否存在
		Document doc = getDoc(Res.INFO_T);
		List<Element> groups = doc.getRootElement().elements(Res.V_G);
		for (Element group : groups) {
			if (group.getTextTrim().equals(newly)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			for (Element group : groups) {
				if (group.getTextTrim().equals(old)) {
					group.setText(newly);
				}
			}
		}
		XmlHelper.writerDoc(doc, Res.INFO_T);
	}

	/*
	 * @ return 1 meaning success
	 * 
	 * @ return -1 meaning uName exist,not have change
	 */
	@SuppressWarnings("unchecked")
	public static int addUserTOGroup(String gName, String uName) {
		boolean flag_g = false;// 分组名标记
		boolean flag_u = false;// 用户名标记
		Document doc = XmlHelper.getDoc(Res.INFO_T);
		Element root = doc.getRootElement();
		List<Element> groups = root.elements();
		for (Element group : groups) {
			List<Element> users = group.elements(Res.V_U);
			for (Element user : users) {
				if (user.getTextTrim().equals(uName)) {
					flag_u = true;
					return -1;
				}
			}
		}
		for (Element group : groups) {
			if (group.getTextTrim().equals(gName)) {
				group.addElement(Res.V_U).addAttribute(Res.V_N, Res.V_NU)
						.setText(uName);
				flag_g = true;
				break;
			}
		}

		if (flag_g && !flag_u) {
			XmlHelper.writerDoc(doc, Res.INFO_T);
		} else if (!flag_u && !flag_g) {
			addGroup(gName);
			addUserTOGroup(gName, uName);
		}
		return 1;
	}

	@SuppressWarnings("unchecked")
	public static void deleteUser(String name) {
		Document doc = getDoc(Res.INFO_T);
		Element root = doc.getRootElement();
		List<Element> groups = root.elements(Res.V_G);
		for (Element group : groups) {
			List<Element> users = group.elements(Res.V_U);
			for (Element user : users) {
				if (user.getTextTrim().equals(name)) {
					user.detach();
				}
			}
		}
		writerDoc(doc, Res.INFO_T);
	}

	@SuppressWarnings("unchecked")
	public static void moveUserToOtherGroup(String name, String groupName) {
		Document doc = getDoc(Res.INFO_T);
		Element root = doc.getRootElement();
		for (Iterator<Element> i_g = root.elementIterator(); i_g.hasNext();) {
			Element group = i_g.next();
			if (group.getTextTrim().equals(groupName)) {
				addUserTOGroup(groupName, name);
				break;
			}
		}
		XmlHelper.writerDoc(doc, Res.INFO_T);
	}

	@SuppressWarnings("unchecked")
	public static DefaultMutableTreeNode getUserGroups() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(Res.V_A);
		DefaultMutableTreeNode userNode = null;
		Document doc = getDoc(Res.INFO_T);
		Element root = doc.getRootElement();
		List<Element> groups = root.elements(Res.V_G);
		for (Element group : groups) {
			String gName = group.getTextTrim();
			DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(gName);
			top.add(groupNode);
			List<Element> users = group.elements(Res.V_U);
			for (Element user : users) {
				String uName = user.getTextTrim();
				userNode = new DefaultMutableTreeNode(uName);
				groupNode.add(userNode);
			}
		}
		return top;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> getAllLinkman() {
		ArrayList<String> all = new ArrayList<>();
		Document doc = getDoc(Res.INFO_T);
		Element root = doc.getRootElement();
		List<Element> groups = root.elements(Res.V_G);
		for (Element group : groups) {
			List<Element> users = group.elements(Res.V_U);
			for (Element user : users) {
				all.add(user.getTextTrim());
			}
		}
		return all;
	}

	@SuppressWarnings("unchecked")
	public static Vector<String> getLinkman() {
		Vector<String> linkman = new Vector<>();
		Document doc = XmlHelper.getDoc(Res.INFO_T);
		List<Element> groups = doc.getRootElement().elements(Res.V_G);
		for (Element group : groups) {
			List<Element> users = group.elements(Res.V_U);
			for (Element user : users) {
				linkman.add(user.getTextTrim());
			}
		}
		return linkman;
	}

}