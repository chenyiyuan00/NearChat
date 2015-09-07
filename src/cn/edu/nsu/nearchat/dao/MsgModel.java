package cn.edu.nsu.nearchat.dao;

import java.io.Serializable;
import java.sql.Timestamp;

public class MsgModel implements Serializable {

	private static final long serialVersionUID = -6850823323964128143L;

	private String type;
	private Timestamp time;
	private long size;
	private Object content;
	private String from;
	private String goal;

	public String getType() {
		return type;
	}

	public Timestamp getTime() {
		return time;
	}

	public long getSize() {
		return size;
	}

	public Object getContent() {
		return content;
	}

	public String getFrom() {
		return from;
	}

	public String getGoal() {
		return goal;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

}
