package com.zhongjian.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.DateUtil;
import com.zhongjian.component.PropComponent;
import com.zhongjian.dao.DeviceDao;
import com.zhongjian.dao.UserDao;
import com.zhongjian.dto.BeInvitedUser;
import com.zhongjian.pojo.Device;
import com.zhongjian.pojo.User;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private DeviceDao deviceDao;
	
	@Autowired
	private PropComponent propComponent;

	@Override
	public User getUserInfoByOwn(Long own) {
		return userDao.getUserByOwn(own);
	}

	@Override
	public User getUserInfoByUid(Integer uid) {
		return userDao.getUserByUid(uid);
	}

	@Override
	public List<BeInvitedUser> getBeInvited(Long own) throws ParseException {
		Integer uid = userDao.getUidByOwn(own);
		List<User> users = userDao.getBeinvitedByOwn(own);
		List<BeInvitedUser> beInvitedUsers = new ArrayList<>();
		for (Iterator iterator = users.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			BeInvitedUser beInvitedUser = new BeInvitedUser();
			beInvitedUser.setId(user.getId());
			beInvitedUser.setNickname(user.getNickname());
			beInvitedUser.setHeadimg(user.getHeadimg());
			// 取被分享者uid对应设备
			List<Device> devices = deviceDao.getDeviceByUid(user.getId());
			for (Iterator iterator2 = devices.iterator(); iterator2.hasNext();) {
				Device device = (Device) iterator2.next();
				// 只要有 没收的就break
				String cantakeTimeStr = DateUtil.getDateStringByPoint((String)propComponent.getMap().get("sjserver.timepoint"));
				Date canTakeTime = DateUtil.StringToDate(cantakeTimeStr, "yyyy-MM-dd");
				if (canTakeTime.compareTo(device.getBindtime()) > 0 && canTakeTime.compareTo(device.getDeadtime()) <= 0
						&& !deviceDao.getTagForUD(uid, device.getId())) {// 确保刚绑定上的第二天才能收
					beInvitedUser.setCansteal(true);
					break;
				}
			}
			beInvitedUsers.add(beInvitedUser);
		}
		return beInvitedUsers;
	}

	@Override
	public Integer additionInfo(String realname, String phoneNum, Integer userId) {
		return userDao.additionInfo(userId, realname, phoneNum);
	}

	@Override
	public Integer deviceVerifyApply(Integer userId, String deviceNo) {
		if (userDao.userIsAuth(userId) == 2) {
			return deviceDao.diviceVerifyApply(userId, deviceNo, new java.sql.Timestamp(System.currentTimeMillis()));
		} else {
			return 0;
		}

	}

	@Override
	public Boolean isAuth(Integer userId) {
		if (userDao.userIsAuth(userId) == 2) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<HashMap<String, String>> getUserLog(Integer userId) {
		List<Document> logs = userDao.find("zjsj", "zjsj_user_log", userId);
		List<HashMap<String, String>> listMap = new ArrayList<HashMap<String, String>>();
		for (Iterator<Document> iterator = logs.iterator(); iterator.hasNext();) {
			Document document = iterator.next();
			HashMap<String, String> map = new HashMap<>();
			map.put("time", (String) document.get("time"));
			map.put("event", (String) document.get("event"));
			listMap.add(map);
		}
		return listMap;
	}

}
