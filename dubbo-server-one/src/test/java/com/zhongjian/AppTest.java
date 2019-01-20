package com.zhongjian;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhongjian.dao.DeviceDao;
import com.zhongjian.dao.UserDao;
import com.zhongjian.dto.TransferDevice;
import com.zhongjian.pojo.DeviceApply;
import com.zhongjian.pojo.DeviceType;
import com.zhongjian.service.DeviceService;
import com.zhongjian.service.UserService;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"/META-INF/spring/dubbo-server-one.xml"})
public class AppTest {  
    @Resource
    DeviceDao deviceDao;

    @Resource
    UserDao userDao;
    @Resource
    DeviceService deviceService;
    @Resource
    UserService userService;
    @Test  
    public void getListTest() throws ParseException{
    }  
}
