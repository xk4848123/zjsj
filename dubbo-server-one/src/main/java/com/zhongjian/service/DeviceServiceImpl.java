package com.zhongjian.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.DateUtil;
import com.zhongjian.RandomUtil;
import com.zhongjian.component.PropComponent;
import com.zhongjian.dao.DeviceDao;
import com.zhongjian.dao.UserDao;
import com.zhongjian.dto.TransferDevice;
import com.zhongjian.pojo.Device;
import com.zhongjian.pojo.DeviceApply;
import com.zhongjian.pojo.DeviceType;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

    String timePoint = "07:00:00";
	
	@Autowired
	private PropComponent propComponent;

	@Autowired
	private DeviceDao deviceDao;

	@Autowired
	private UserDao userDao;

	@PostConstruct
	void Init(){
		this.timePoint = (String)propComponent.getMap().get("sjserver.timepoint");
	}
	
	// 查看自己的基础水机(矿机页面)
	@Override
	public List<Device> getBaseDeviceByUid(Integer uid) {
		return deviceDao.getDeviceByUid(uid);
	}

	// //后台查看用户的矿机
	// @Override
	// public List<Device> getBaseDeviceByUid(Integer uid) {
	// return deviceDao.getDeviceByUid(uid);
	// }

	@Override
	public HashMap<String, Object> getDeviceCanTakeByUid(Integer uid) throws ParseException {
		HashMap<String, Object> resultMap = new HashMap<>();
		Date now = new Date();
		Date todayTimePoint = DateUtil.getTodayTimePoint(timePoint);// 今天的凌晨timepoint
		long remainTimeLong;
		String sec = null;// 剩余时间
		long pastTimeLong;
		if (now.compareTo(todayTimePoint) < 0) {
			// 当前时间未超过当天凌晨七点 这时候
			// 剩余时间
			remainTimeLong = todayTimePoint.getTime() - now.getTime();
			Date yesterdayTimePoint = DateUtil.getYesterdayTimePoint(timePoint);// 昨天的凌晨timepoint
			pastTimeLong = (now.getTime() - yesterdayTimePoint.getTime()) / 1000;
			sec = DateUtil.secToTime((int) remainTimeLong / 1000);
		} else {
			Date tomorroTimePoint = DateUtil.getTomorroTimePoint(timePoint);// 明天凌晨的timepoint
			remainTimeLong = tomorroTimePoint.getTime() - now.getTime();
			pastTimeLong = (now.getTime() - todayTimePoint.getTime()) / 1000;
			sec = DateUtil.secToTime((int) remainTimeLong / 1000);
		}
		BigDecimal pastTimeB = new BigDecimal(pastTimeLong);
		// 一天的秒数
		BigDecimal wholeDayTimeB = new BigDecimal("86400");
		resultMap.put("remainTime", sec);
		List<Device> devices = deviceDao.getDeviceByUid(uid);
		List<java.util.Map<String, String>> toBeBornList = new ArrayList<java.util.Map<String, String>>();
		List<java.util.Map<String, String>> BornList = new ArrayList<java.util.Map<String, String>>();
		BigDecimal wholeHashrate = BigDecimal.ZERO;
		for (Iterator iterator = devices.iterator(); iterator.hasNext();) {
			Device device = (Device) iterator.next();
			// 待生成的水滴
			HashMap<String, String> toBeBornMap = new HashMap<>();
			BigDecimal NumEverySec = device.getHashrate().multiply(new BigDecimal("0.0000004"));
			wholeHashrate = wholeHashrate.add(device.getHashrate());
			toBeBornMap.put("NumEverySec", NumEverySec.stripTrailingZeros().toString());
			toBeBornMap.put("alreadyProducedNum", NumEverySec.multiply(pastTimeB).stripTrailingZeros().toString());
			toBeBornList.add(toBeBornMap);
			// 可以收的水滴
			Date canTakeTime = DateUtil.StringToDate(DateUtil.getDateStringByPoint(timePoint), "yyyy-MM-dd");
			if (canTakeTime.compareTo(device.getBindtime()) > 0) {
				if (!deviceDao.getTagForUD(uid, device.getId())) {
					HashMap<String, String> BornMap = new HashMap<>();
					BornMap.put("producedNum", NumEverySec.multiply(wholeDayTimeB).stripTrailingZeros().toString());
					BornMap.put("deviceid", String.valueOf(device.getId()));
					BornList.add(BornMap);
				}
			}
		}
		resultMap.put("toBeBornList", toBeBornList);
		resultMap.put("BornList", BornList);
		List<Device> gxDevices = deviceDao.getGXDeviceByUid(uid);
		BigDecimal ProducedNum = BigDecimal.ZERO;
		for (Iterator iterator = gxDevices.iterator(); iterator.hasNext();) {
			Device device = (Device) iterator.next();
			// 待生成的水滴
			HashMap<String, String> toBeBornMap = new HashMap<>();
			// 每秒的矿
			BigDecimal NumEverySec = device.getHashrate().multiply(new BigDecimal("0.0000004"));
			wholeHashrate = wholeHashrate.add(device.getHashrate());
			// 可以收的水滴
			Date canTakeTime = DateUtil.StringToDate(DateUtil.getDateStringByPoint(timePoint), "yyyy-MM-dd");
			if (canTakeTime.compareTo(device.getBindtime()) > 0) {
				if (!deviceDao.getTagForUD(uid, device.getId())) {
					ProducedNum = ProducedNum.add(NumEverySec.multiply(wholeDayTimeB));
				}
			}
		}
		resultMap.put("gxBorn", ProducedNum.stripTrailingZeros().toString());
		resultMap.put("wholeHashrate", wholeHashrate);
		return resultMap;
	}

	@Override
	public HashMap<String, Object> getBaseDeviceCanStealByOtherUid(Integer uid, Integer otherId) throws ParseException {
		HashMap<String, Object> resultMap = new HashMap<>();
		Date now = new Date();
		Date todayTimePoint = DateUtil.getTodayTimePoint(timePoint);// 今天的凌晨timepoint
		long remainTimeLong;
		String sec = null;// 剩余时间
		long pastTimeLong;
		if (now.compareTo(todayTimePoint) < 0) {
			// 当前时间未超过当天凌晨七点 这时候
			// 剩余时间
			remainTimeLong = todayTimePoint.getTime() - now.getTime();
			Date yesterdayTimePoint = DateUtil.getYesterdayTimePoint(timePoint);// 昨天的凌晨timepoint
			pastTimeLong = (now.getTime() - yesterdayTimePoint.getTime()) / 1000;
			sec = DateUtil.secToTime((int) remainTimeLong / 1000);
		} else {
			Date tomorroTimePoint = DateUtil.getTomorroTimePoint(timePoint);// 明天凌晨的timepoint
			remainTimeLong = tomorroTimePoint.getTime() - now.getTime();
			pastTimeLong = (now.getTime() - todayTimePoint.getTime()) / 1000;
			sec = DateUtil.secToTime((int) remainTimeLong / 1000);
		}
		BigDecimal pastTimeB = new BigDecimal(pastTimeLong);
		// 一天的秒数
		BigDecimal wholeDayTimeB = new BigDecimal("86400");
		resultMap.put("remainTime", sec);
		List<Device> devices = deviceDao.getDeviceByUid(otherId);
		List<java.util.Map<String, String>> toBeBornList = new ArrayList<java.util.Map<String, String>>();
		List<java.util.Map<String, String>> BornList = new ArrayList<java.util.Map<String, String>>();
		BigDecimal wholeHashrate = BigDecimal.ZERO;
		for (Iterator iterator = devices.iterator(); iterator.hasNext();) {
			Device device = (Device) iterator.next();
			// 待生成的水滴
			HashMap<String, String> toBeBornMap = new HashMap<>();
			BigDecimal NumEverySec = device.getHashrate().multiply(new BigDecimal("0.0000004"));
			wholeHashrate = wholeHashrate.add(device.getHashrate());
			toBeBornMap.put("NumEverySec", NumEverySec.stripTrailingZeros().toString());
			toBeBornMap.put("alreadyProducedNum", NumEverySec.multiply(pastTimeB).stripTrailingZeros().toString());
			toBeBornList.add(toBeBornMap);
			// 可以收的水滴
			Date canTakeTime = DateUtil.StringToDate(DateUtil.getDateStringByPoint(timePoint), "yyyy-MM-dd");
			if (canTakeTime.compareTo(device.getBindtime()) > 0) {
				if (!deviceDao.getTagForUD(uid, device.getId())) {
					HashMap<String, String> BornMap = new HashMap<>();
					BornMap.put("producedNum", NumEverySec.multiply(wholeDayTimeB).stripTrailingZeros().toString());
					BornMap.put("deviceid", String.valueOf(device.getId()));
					BornList.add(BornMap);
				}
			}
		}
		resultMap.put("toBeBornList", toBeBornList);
		resultMap.put("BornList", BornList);
		List<Device> gxDevices = deviceDao.getGXDeviceByUid(otherId);
		for (Iterator iterator = gxDevices.iterator(); iterator.hasNext();) {
			Device device = (Device) iterator.next();
			wholeHashrate = wholeHashrate.add(device.getHashrate());
		}
		resultMap.put("wholeHashrate", wholeHashrate);
		return resultMap;
	}

	@Override
	@Transactional
	public BigDecimal takeOrSteal(Integer uid, Integer deviceid) throws ParseException {
		// 首先查有没有收过
		String cantakeTimeStr = DateUtil.getDateStringByPoint(timePoint);
		Date canTakeTime = DateUtil.StringToDate(cantakeTimeStr, "yyyy-MM-dd");
		Device device = deviceDao.queryDeviceById(deviceid);
		if (canTakeTime.compareTo(device.getBindtime()) > 0 && canTakeTime.compareTo(device.getDeadtime()) <= 0
				&& !deviceDao.getTagForUD(uid, device.getId())) {// 确保刚绑定上的第二天才能收
			// can write data
			//
		} else {
			return BigDecimal.ZERO;
		}
		// 一天的秒数
		BigDecimal wholeDayTimeB = new BigDecimal("86400");
		BigDecimal NumEverySec = device.getHashrate().multiply(new BigDecimal("0.0000004"));
		BigDecimal productedNum = NumEverySec.multiply(wholeDayTimeB);
		if (deviceDao.jungleUDForSelf(uid, deviceid)) {
			// 水机属于自己
			deviceDao.takeOrSteal(uid, deviceid, cantakeTimeStr);
			// 如果是共享水机
			if (deviceDao.jungleGX(deviceid)) {
				// 增加奖励矿值
				userDao.updateUserAwardPetrolatum(uid, productedNum);
				
			} else {
				// 基础水机
				// 增加基础矿值
				userDao.updateUserBasePetrolatum(uid, productedNum);
				deviceDao.inSert("zjsj", "zjsj_user_log", uid, DateUtil.DateToStr(new Date()), "收取矿值 " + productedNum);
				userDao.updateUDAcheivement(uid, deviceid, productedNum);
			}
			return productedNum;
		} else if (deviceDao.jungleUDForParent(uid, deviceid)) {
			// 推荐的人的水机
			deviceDao.takeOrSteal(uid, deviceid, cantakeTimeStr);
			BigDecimal halfProductedNum = productedNum.multiply(new BigDecimal("0.5"));
			userDao.updateUserBasePetrolatum(uid, halfProductedNum);
			deviceDao.inSert("zjsj", "zjsj_user_log", uid, DateUtil.DateToStr(new Date()), "收取矿值 " + halfProductedNum);
			return halfProductedNum;
		} else {
			return BigDecimal.ZERO;
		}
	}

	@Override
	@Transactional
	public BigDecimal takeAward(Integer uid) throws ParseException {
		BigDecimal result = BigDecimal.ZERO;
		// 查出所有的共享水机
		List<Device> gxDevices = deviceDao.getGXDeviceByUid(uid);
		for (Iterator iterator = gxDevices.iterator(); iterator.hasNext();) {
			Device device = (Device) iterator.next();
			BigDecimal currentAward = takeOrSteal(uid, device.getId());
			result = result.add(currentAward);
		}
		deviceDao.inSert("zjsj", "zjsj_user_log", uid, DateUtil.DateToStr(new Date()), "收取奖励矿值 " + result);
		return result;
	}

	@Override
	public List<DeviceApply> getDeviceBindApply() {
		return deviceDao.getDeviceApplyInfo();
	}

	@Override
	public List<DeviceType> getAllDeviceType() {
		return deviceDao.getAllDeviceType();
	}

	@Override
	public List<DeviceType> getDeviceType(Integer isgx) {
		return deviceDao.getDeviceType(isgx);
	}

	@Override
	@Transactional
	public boolean bindDevice(Integer uid, String deviceNo, Integer typeid, Integer days) {
		if (days <= 0) {
			return false;
		}
		Date now = new Date();
		Date later = DateUtil.AddOrSubDate(now, days);
		java.sql.Date nowSqlDate = new java.sql.Date(now.getTime());
		java.sql.Date laterSqlDate = new java.sql.Date(later.getTime());
		// 增加设备
		Integer deviceId = deviceDao.addDevice(typeid, deviceNo);
		//绑定设备
		deviceDao.bindDevice(uid, deviceId, nowSqlDate, laterSqlDate);
		//删除申请
		deviceDao.deleteDeviceApply(deviceNo);
		if (userDao.getParentUid(uid) != null) {
			this.bindGXDevice(userDao.getParentUid(uid), "GX_01_" + RandomUtil.getDateNumber() +(String)propComponent.getMap().get("sjserver.tag"), 2, 30);
		}
		return true;
	}
	
	public void bindGXDevice(Integer uid, String deviceNo, Integer typeid, Integer days) {
		Date now = new Date();
		Date later = DateUtil.AddOrSubDate(now, days);
		java.sql.Date nowSqlDate = new java.sql.Date(now.getTime());
		java.sql.Date laterSqlDate = new java.sql.Date(later.getTime());
		// 增加设备
		Integer deviceId = deviceDao.addDevice(typeid, deviceNo);
		//绑定设备
		deviceDao.bindDevice(uid, deviceId, nowSqlDate, laterSqlDate);
		deviceDao.inSert("zjsj", "zjsj_user_log", uid, DateUtil.DateToStr(new Date()), "您的推荐用户绑定水机一台，您会获得更多额外奖励");
	}

	@Override
	public List<TransferDevice> getAllDevice() throws ParseException {
		 List<TransferDevice> transferDevices = deviceDao.getAllDeviceForControl();
		for (Iterator iterator = transferDevices.iterator(); iterator.hasNext();) {
			TransferDevice transferDevice = (TransferDevice) iterator.next();
			if (deviceDao.checkDevice(transferDevice.getId())) {
				transferDevice.setOn(true);
			}else {
				transferDevice.setOn(false);
			}
		}
		return transferDevices;
	}

	@Override
	public List<TransferDevice> getAllDeviceByPage(Integer page, Integer pageSize) throws ParseException {
		List<TransferDevice> transferDevices = deviceDao.getAllDeviceForControl(page, pageSize);
		for (Iterator iterator = transferDevices.iterator(); iterator.hasNext();) {
			TransferDevice transferDevice = (TransferDevice) iterator.next();
			if (deviceDao.checkDevice(transferDevice.getId())) {
				transferDevice.setOn(true);
			}else {
				transferDevice.setOn(false);
			}
		}
		return transferDevices;
	}

	@Override
	public boolean stopDevice(Integer deviceId) throws ParseException {
		if (!deviceDao.checkDevice(deviceId)) {
			return false;
		}
		java.sql.Date deadtime = new java.sql.Date(DateUtil.getYesterday().getTime());
		deviceDao.setDeadTime(deadtime, deviceId);
		return true;
	}


	@Override
	public boolean startDevice(Integer deviceId, Integer days) throws ParseException {
		if (deviceDao.checkDevice(deviceId)) {
			return false;
		}
		Date now = new Date();
		Date later = DateUtil.AddOrSubDate(now, days);
		java.sql.Date deadtime = new java.sql.Date(later.getTime());
		deviceDao.setDeadTime(deadtime, deviceId);
		return true;
	}

	@Override
	public boolean updateHashrateByTypeId(Integer typeid, BigDecimal hashrate) {
		return deviceDao.setHashrate(hashrate,typeid);
	}
	

}
