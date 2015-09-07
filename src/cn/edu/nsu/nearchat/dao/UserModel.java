package cn.edu.nsu.nearchat.dao;

import java.sql.Blob;
import java.sql.Timestamp;

public class UserModel {
	private int u_id;
	private String u_loginname;
	private String u_password;
	private int u_status;
	private String u_ip;
	private Timestamp u_session;
	private String u_nickname;
	private String u_realname;
	private String u_phone;
	private String u_email;
	private String u_description;
	private String u_PPQ;
	private String u_PPA;
	private Blob u_group;

	public int getU_id() {
		return u_id;
	}

	public String getU_loginname() {
		return u_loginname;
	}

	public String getU_password() {
		return u_password;
	}

	public int getU_status() {
		return u_status;
	}

	public String getU_ip() {
		return u_ip;
	}

	public Timestamp getU_session() {
		return u_session;
	}

	public String getU_nickname() {
		return u_nickname;
	}

	public String getU_realname() {
		return u_realname;
	}

	public String getU_phone() {
		return u_phone;
	}

	public String getU_email() {
		return u_email;
	}

	public String getU_description() {
		return u_description;
	}

	public String getU_PPQ() {
		return u_PPQ;
	}

	public String getU_PPA() {
		return u_PPA;
	}

	public Blob getU_group() {
		return u_group;
	}

	public void setU_id(int u_id) {
		this.u_id = u_id;
	}

	public void setU_loginname(String u_loginname) {
		this.u_loginname = u_loginname;
	}

	public void setU_password(String u_password) {
		this.u_password = u_password;
	}

	public void setU_status(int u_status) {
		this.u_status = u_status;
	}

	public void setU_ip(String u_ip) {
		this.u_ip = u_ip;
	}

	public void setU_session(Timestamp u_session) {
		this.u_session = u_session;
	}

	public void setU_nickname(String u_nickname) {
		this.u_nickname = u_nickname;
	}

	public void setU_realname(String u_realname) {
		this.u_realname = u_realname;
	}

	public void setU_phone(String u_phone) {
		this.u_phone = u_phone;
	}

	public void setU_email(String u_email) {
		this.u_email = u_email;
	}

	public void setU_description(String u_description) {
		this.u_description = u_description;
	}

	public void setU_PPQ(String u_PPQ) {
		this.u_PPQ = u_PPQ;
	}

	public void setU_PPA(String u_PPA) {
		this.u_PPA = u_PPA;
	}

	public void setU_group(Blob u_group) {
		this.u_group = u_group;
	}

}
