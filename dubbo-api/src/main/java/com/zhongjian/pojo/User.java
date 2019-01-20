package com.zhongjian.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class User implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String realname;

    private String phonenum;

    private Integer isauth;
    
    private String nickname;

    private String sex;

    private String headimg;

    private String unionid;

    private BigDecimal basehashrate;

    private Long parent;

    private Long own;

    private BigDecimal basepetrolatum;

    private BigDecimal awardpetrolatum;

    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public BigDecimal getBasehashrate() {
        return basehashrate;
    }

    public void setBasehashrate(BigDecimal basehashrate) {
        this.basehashrate = basehashrate;
    }

    public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Long getOwn() {
		return own;
	}

	public void setOwn(Long own) {
		this.own = own;
	}

	public BigDecimal getBasepetrolatum() {
        return basepetrolatum;
    }

    public void setBasepetrolatum(BigDecimal basepetrolatum) {
        this.basepetrolatum = basepetrolatum;
    }

    public BigDecimal getAwardpetrolatum() {
        return awardpetrolatum;
    }

    public void setAwardpetrolatum(BigDecimal awardpetrolatum) {
        this.awardpetrolatum = awardpetrolatum;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	public Integer getIsauth() {
		return isauth;
	}

	public void setIsauth(Integer isauth) {
		this.isauth = isauth;
	}
    
}
