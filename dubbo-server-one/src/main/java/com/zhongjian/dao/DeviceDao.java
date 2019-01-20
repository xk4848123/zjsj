package com.zhongjian.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;
import com.zhongjian.DateUtil;
import com.zhongjian.component.PropComponent;
import com.zhongjian.dto.TransferDevice;
import com.zhongjian.pojo.Device;
import com.zhongjian.pojo.DeviceApply;
import com.zhongjian.pojo.DeviceType;

@Repository
public class DeviceDao extends MongoDBDaoBase {

	@Autowired
	private PropComponent propComponent;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 获取非共享水机
	public List<Device> getDeviceByUid(int userid) {
		String now = DateUtil.getACertainYearMonth(new Date());
		String sql = "SELECT ud.bindtime,ud.deadtime,d.deviceno,dt.hashrate,ud.achievement,d.id FROM user_device ud,device d ,device_type dt WHERE userid = "
				+ userid + " AND (date_format(bindtime, '%Y-%m-%d') < '" + now
				+ "' OR date_format(bindtime, '%Y-%m-%d') = '" + now + "') AND (date_format(deadtime, '%Y-%m-%d') > '"
				+ now + "' OR date_format(deadtime, '%Y-%m-%d') = '" + now
				+ "') AND ud.deviceid = d.id and d.type = dt.typeid AND locate('GX',deviceno)=0 ";
		RowMapper<Device> rowMapper = new BeanPropertyRowMapper<>(Device.class);
		List<Device> deviceList = jdbcTemplate.query(sql, rowMapper);
		return deviceList;
	}

	// 获取共享水机
	public List<Device> getGXDeviceByUid(int userid) {
		String now = DateUtil.getACertainYearMonth(new Date());
		String sql = "SELECT ud.bindtime,ud.deadtime,d.deviceno,dt.hashrate,ud.achievement,d.id FROM user_device ud,device d ,device_type dt WHERE userid = "
				+ userid + " AND (date_format(bindtime, '%Y-%m-%d') < '" + now
				+ "' OR date_format(bindtime, '%Y-%m-%d') = '" + now + "') AND (date_format(deadtime, '%Y-%m-%d') > '"
				+ now + "' OR date_format(deadtime, '%Y-%m-%d') = '" + now
				+ "') AND ud.deviceid = d.id and d.type = dt.typeid AND locate('GX',deviceno)>0 ";
		List<Device> deviceList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Device.class));
		return deviceList;
	}

	// 查看某个uid当天有没有收某个deviceid的矿
	public boolean getTagForUD(int uid, int deviceId) throws ParseException {
		String nowYMD = DateUtil.getDateStringByPoint((String) propComponent.getMap().get("sjserver.timepoint"));
		String sql = "SELECT COUNT(1) from user_devicetaketag where userid = ? and deviceid = ? and taketime = date_format('"
				+ nowYMD + "','%Y-%m-%d')";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { uid, deviceId });
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 查看某个矿机是否属于自己（可以包括共享水机）
	public boolean jungleUDForSelf(int uid, int deviceId) throws ParseException {
		String sql = "SELECT COUNT(1) FROM user_device WHERE userid = " + uid + " And deviceid = " + deviceId;
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 查看某个矿机是否属于上级
	public boolean jungleUDForParent(int uid, int deviceId) throws ParseException {
		String sql = "SELECT COUNT(1) FROM user_device,device WHERE userid in(SELECT id  from user where parent = "
				+ "(SELECT own from user where id = ?))  and deviceid = ? and deviceid =device.id  AND locate('GX',deviceno)=0";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { uid, deviceId });
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 查看某个矿机是否属于上级
	public boolean jungleGX(int deviceId) throws ParseException {
		String sql = "SELECT COUNT(1) from device where id = ? and locate('GX',deviceno)>0";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { deviceId });
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Device queryDeviceById(int deviceId) {
		String sql = "SELECT ud.bindtime,ud.deadtime,d.deviceno,dt.hashrate,ud.achievement,d.id FROM "
				+ "user_device ud,device d ,device_type dt WHERE ud.deviceid = d.id and d.type = dt.typeid and d.id = "
				+ deviceId;
		RowMapper<Device> rowMapper = new BeanPropertyRowMapper<>(Device.class);
		Device device = jdbcTemplate.queryForObject(sql, rowMapper);
		return device;
	}

	public void takeOrSteal(int uid, int deviceId, String takeTime) {
		String sql = "INSERT into user_devicetaketag (deviceid,userid,taketime) VALUES (?,?,?)";
		jdbcTemplate.update(sql, deviceId, uid, takeTime);
	}

	// 申请绑定水机
	public Integer diviceVerifyApply(int uid, String deviceNo, java.sql.Timestamp date) {
		String sql = "INSERT into device_apply (userid,deviceno,createtime) VALUES (?,?,?)";
		return jdbcTemplate.update(sql, uid, deviceNo, date);
	}

	// 查看申请绑定的水机
	public List<DeviceApply> getDeviceApplyInfo() {
		String sql = "SELECT da.deviceno, u.realname, u.phonenum,da.createtime,da.userid FROM device_apply "
				+ "da,user u WHERE da.userid = u.id ORDER BY da.createtime ASC";
		RowMapper<DeviceApply> rowMapper = new BeanPropertyRowMapper<>(DeviceApply.class);
		List<DeviceApply> deviceApplyInfos = jdbcTemplate.query(sql, rowMapper);
		return deviceApplyInfos;
	}

	// 查看所有水机（包共享水机）类型
	public List<DeviceType> getAllDeviceType() {
		String sql = "SELECT typeid,hashrate,type_name from device_type " + "ORDER BY isgx asc, hashrate asc";
		RowMapper<DeviceType> rowMapper = new BeanPropertyRowMapper<>(DeviceType.class);
		List<DeviceType> allDeviceTypes = jdbcTemplate.query(sql, rowMapper);
		return allDeviceTypes;
	}

	// 查看共享or基础水机
	public List<DeviceType> getDeviceType(Integer isgx) {
		String sql = "SELECT typeid,hashrate,type_name from device_type WHERE isgx = ? ORDER BY hashrate asc";
		RowMapper<DeviceType> rowMapper = new BeanPropertyRowMapper<>(DeviceType.class);
		List<DeviceType> deviceTypes = jdbcTemplate.query(sql, rowMapper, new Object[] { isgx });
		return deviceTypes;
	}

	public Integer addDevice(Integer typeid, String deviceNo) {
		final String sql = "INSERT INTO device (deviceno,type,createtime) VALUES (?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, new String[] { "deviceno", "type", "createtime" });
				ps.setString(1, deviceNo);
				ps.setInt(2, typeid);
				ps.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public Integer bindDevice(int uid, int deviceId, java.sql.Date now, java.sql.Date later) {
		String sql = "INSERT into user_device (userid,deviceid,bindtime,deadtime) VALUES (?,?,?,?)";
		int update = jdbcTemplate.update(sql, uid, deviceId, now, later);
		return update;
	}

	public List<TransferDevice> getAllDeviceForControl() {
		String sql = "SELECT d.id,d.deviceno,ud.bindtime,ud.deadtime,ud.achievement,u.realname,u.phonenum,u.sex,dt.hashrate from device d , "
				+ "user_device ud,user u,device_type dt where d.id =ud.deviceid and u.id = ud.userid "
				+ "and dt.typeid = d.type and locate('GX',deviceno)=0  order by bindtime DESC,deadtime asc";
		List<TransferDevice> transferDevices = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TransferDevice.class));
		return transferDevices;
	}

	public List<TransferDevice> getAllDeviceForControl(Integer page, Integer pageSize) {
		Integer limitParam1 = (page - 1) * pageSize;
		Integer limitParam2 = pageSize;
		String sql = "SELECT d.id,d.deviceno,ud.bindtime,ud.deadtime,ud.achievement,u.realname,u.phonenum,u.sex,dt.hashrate from device d , "
				+ "user_device ud,user u,device_type dt where d.id =ud.deviceid and u.id = ud.userid "
				+ "and dt.typeid = d.type and locate('GX',deviceno)=0  order by bindtime DESC,deadtime asc limit ?,?";
		List<TransferDevice> transferDevices = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TransferDevice.class),
				limitParam1, limitParam2);
		return transferDevices;
	}

	public boolean checkDevice(Integer deviceId) throws ParseException {
		String sql = "SELECT bindtime,deadtime from user_device where deviceid = ?";
		RowMapper<Device> rowMapper = new BeanPropertyRowMapper<>(Device.class);
		Device device = jdbcTemplate.queryForObject(sql, rowMapper, deviceId);
		if (device.getDeadtime().compareTo(DateUtil.getDateYMD()) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setDeadTime(java.sql.Date date, Integer deviceId) {
		String sql = "update user_device set deadtime = ? where deviceid = ?";
		jdbcTemplate.update(sql, date, deviceId);
	}

	public boolean setHashrate(BigDecimal hashrate, Integer typeid) {
		String sql = "update device_type set hashrate = ? where typeid = ?";
		int update = jdbcTemplate.update(sql, hashrate, typeid);
		if (update > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void inSert(String dbName, String collectionName, Integer userId, String time, String event) {
		MongoCollection<Document> collection = getCollection(dbName, collectionName);
		Document document = new Document();
		document.append("userid", userId).append("time", time).append("event", event);
		collection.insertOne(document);
	}


	public void deleteDeviceApply(String deviceNO) {
		String sql = "delete from device_apply where deviceno = ?";
		jdbcTemplate.update(sql, deviceNO);
	}

}