package server;

import java.sql.Date;

public class Score implements java.io.Serializable {
	private int id;
	private int user;
	private Date date;
	private int rate;
	private String userStr;

	public Score(int id, int user, Date date, int rate) {
		this.id = id;
		this.user = user;
		this.date = date;
		this.rate = rate;
	}
	
	public Score(String user, int rate) {
		this.userStr = user;
		this.rate = rate;
	}

	public Score() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser() {
		return user;
	}
	
	public void setUser(int user) {
		this.user = user;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date dateField1) {
		this.date = dateField1;
	}
	
	public int getRate() {
		return rate;
	}
	
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public String getUserStr() {
		return userStr;
	}
	
	public void setUserStr(String userStr) {
		this.userStr = userStr;
	}
}
