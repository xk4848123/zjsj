package com.zhongjian.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TransferDevice implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;

	private Date bindtime;

	private Date deadtime;

	private String deviceno;

	private BigDecimal hashrate;

	private BigDecimal achievement;

	private String realname;

	private String phonenum;

	private String sex;
	
	private boolean isOn;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getBindtime() {
		return bindtime;
	}

	public void setBindtime(Date bindtime) {
		this.bindtime = bindtime;
	}

	public Date getDeadtime() {
		return deadtime;
	}

	public void setDeadtime(Date deadtime) {
		this.deadtime = deadtime;
	}

	public String getDeviceno() {
		return deviceno;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}

	public BigDecimal getHashrate() {
		return hashrate;
	}

	public void setHashrate(BigDecimal hashrate) {
		this.hashrate = hashrate;
	}

	public BigDecimal getAchievement() {
		return achievement;
	}

	public void setAchievement(BigDecimal achievement) {
		this.achievement = achievement;
	}

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}
	
	
	
}
