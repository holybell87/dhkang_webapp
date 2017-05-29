package my.web.domain;

import java.util.Date;

public class UserVO {

	private String uid;
	private String upw;
	private String uname;
	private int upoint;
	private String sessionkey;
	private Date sessionlimit;
	private Date regdate;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUpw() {
		return upw;
	}

	public void setUpw(String upw) {
		this.upw = upw;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public int getUpoint() {
		return upoint;
	}

	public void setUpoint(int upoint) {
		this.upoint = upoint;
	}

	public String getSessionkey() {
		return sessionkey;
	}

	public void setSessionkey(String sessionkey) {
		this.sessionkey = sessionkey;
	}

	public Date getSessionlimit() {
		return sessionlimit;
	}

	public void setSessionlimit(Date sessionlimit) {
		this.sessionlimit = sessionlimit;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	@Override
	public String toString() {
		return "UserVO [uid=" + uid + ", upw=" + upw + ", uname=" + uname + ", upoint=" + upoint + ", sessionkey="
				+ sessionkey + ", sessionlimit=" + sessionlimit + ", regdate=" + regdate + "]";
	}

}
