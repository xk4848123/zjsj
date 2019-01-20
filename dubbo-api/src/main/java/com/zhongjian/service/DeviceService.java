package com.zhongjian.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import com.zhongjian.dto.TransferDevice;
import com.zhongjian.pojo.Device;
import com.zhongjian.pojo.DeviceApply;
import com.zhongjian.pojo.DeviceType;

public interface DeviceService  {

	
	List<Device> getBaseDeviceByUid(Integer uid);
	
	HashMap<String, Object> getDeviceCanTakeByUid(Integer uid) throws ParseException;
	
	HashMap<String, Object> getBaseDeviceCanStealByOtherUid(Integer uid,Integer otherId) throws ParseException;
	
	BigDecimal takeOrSteal(Integer uid,Integer deviceid) throws ParseException;
	
	BigDecimal takeAward(Integer uid) throws ParseException;
	
	List<DeviceApply> getDeviceBindApply();
	
	List<DeviceType> getAllDeviceType();
	
	List<DeviceType> getDeviceType(Integer isgx);
	
	boolean bindDevice(Integer uid,String deviceNo,Integer typeid,Integer days);
	
	List<TransferDevice> getAllDevice() throws ParseException;
	
	List<TransferDevice> getAllDeviceByPage(Integer page,Integer pageSize) throws ParseException;
	
	boolean stopDevice(Integer deviceId) throws ParseException;
	
	boolean startDevice(Integer deviceId,Integer days) throws ParseException;
	
	boolean updateHashrateByTypeId(Integer typeid,BigDecimal hashrate);
}
