package com.zhongjian.dto;

public class BeInvitedUser implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String nickname;
	
	private String headimg;
	
	private Boolean cansteal = false;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public Boolean getCansteal() {
		return cansteal;
	}

	public void setCansteal(Boolean cansteal) {
		this.cansteal = cansteal;
	}
	
	
}
