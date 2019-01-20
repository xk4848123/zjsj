package com.zhongjian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.dao.TestDao;

@Service("testService")
public class TestServiceImpl implements TestService {

	@Autowired
	private TestDao testDao;
	
	public String testMethod(String param) {
		return "Hello World!";
	}
	
	@Transactional
	public String crud(String param) {
		testDao.update1();
		double a = 3/0;
		testDao.update2();
		return "ok";
	}

}