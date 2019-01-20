package com.zhongjian.pojo;

import java.util.Date;

public class DeviceApply implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	private String realname;
	
	private String phonenum;
	
	private String deviceno;
	
	private Date createtime;
	
	private Integer userid;

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public String getDeviceno() {
		return deviceno;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	
}
