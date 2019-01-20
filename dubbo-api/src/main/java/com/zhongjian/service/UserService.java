package com.zhongjian.service;


import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import com.zhongjian.dto.BeInvitedUser;
import com.zhongjian.pojo.User;

public interface UserService {

	User getUserInfoByOwn(Long own);
	
	User getUserInfoByUid(Integer uid);
	
	List<BeInvitedUser> getBeInvited(Long own) throws ParseException; 
	
    Integer additionInfo(String realname,String phoneNum,Integer userId);
    
    Integer deviceVerifyApply(Integer userId,String deviceNo);
    
    Boolean isAuth(Integer userId);
    
    List<HashMap<String, String>>getUserLog(Integer userId);
}
