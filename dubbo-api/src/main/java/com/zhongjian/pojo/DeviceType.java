package com.zhongjian.pojo;

import java.math.BigDecimal;

public class DeviceType implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer typeid; 
	
	private BigDecimal hashrate;
	
	private String type_name;

	public Integer getTypeid() {
		return typeid;
	}

	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}

	public BigDecimal getHashrate() {
		return hashrate;
	}

	public void setHashrate(BigDecimal hashrate) {
		this.hashrate = hashrate;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	
	
}
