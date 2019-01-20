package com.zhongjian.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Device implements  java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Date bindtime;
	
	private Date deadtime;
	
	private String deviceno;
	
	private BigDecimal hashrate;
	
	private BigDecimal achievement;
	
	private Integer id;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}