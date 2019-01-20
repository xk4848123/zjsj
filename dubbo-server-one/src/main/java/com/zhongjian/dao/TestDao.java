package com.zhongjian.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TestDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void update1() {
		String sql = "update device set type = 2";
		int count = jdbcTemplate.update(sql);
		System.out.println("1数据总数：" + count);
	}

	public void update2() {
		String sql = "update device_type set typeid = 2";
		int count = jdbcTemplate.update(sql);
		System.out.println("2数据总数：" + count);
	}

}

