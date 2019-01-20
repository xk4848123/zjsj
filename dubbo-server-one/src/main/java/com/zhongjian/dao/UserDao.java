package com.zhongjian.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.zhongjian.pojo.User;

@Repository
public class UserDao extends MongoDBDaoBase{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public User getUserByOwn(Long own) {
		String sql = "select * from user where own = ?";
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
		User user = jdbcTemplate.queryForObject(sql, rowMapper, own);
		return user;
	}
	
	public User getUserByUid(Integer uid) {
		String sql = "select * from user where id = ?";
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
		User user = jdbcTemplate.queryForObject(sql, rowMapper, uid);
		return user;
	}
	
	public List<User> getBeinvitedByOwn(Long own) {
		String sql = "select * from user where parent = ?";
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
		List<User> users = jdbcTemplate.query(sql, rowMapper, own);
		return users;
	}
	
	public Integer getUidByOwn(Long own) {
		String sql = "select id from user where own = ?";
		return jdbcTemplate.queryForObject(sql,Integer.class, new Object[]{own});
	}
	
	public Integer updateUserAwardPetrolatum(Integer userId ,BigDecimal addAward) {
		String sql = "update user set awardpetrolatum = awardpetrolatum + ? where id = ?";
		return jdbcTemplate.update(sql,new Object[]{addAward,userId});
	}
	
	public Integer updateUserBasePetrolatum(Integer userId ,BigDecimal addBase) {
		String sql = "update user set basepetrolatum = basepetrolatum + ? where id = ?";
		Integer i = jdbcTemplate.update(sql,addBase, userId);
		return i;
	}
	
	public Integer updateUDAcheivement(Integer userId ,Integer deviceId,BigDecimal addAchieve) {
		String sql = "update user_device set achievement = achievement + ? where userid = ? and deviceid = ?";
		Integer i = jdbcTemplate.update(sql,addAchieve, userId,deviceId);
		return i;
	}
	
	public Integer additionInfo(Integer userId ,String realname,String phoneNum) {
		String sql = "update user set phonenum = ?,realname= ? ,isauth = 2 WHERE id = ?";
		Integer i = jdbcTemplate.update(sql,new Object[]{phoneNum,realname,userId});
		return i;
	}
	
	public Integer userIsAuth(Integer userId) {
		String sql = "select isauth from user WHERE id = ?";
		Integer isAuth = jdbcTemplate.queryForObject(sql,Integer.class,new Object[]{userId});
		return isAuth;
	}
	
	public Integer getParentUid(Integer userId) {
		try {
			String sql = "SELECT id from user where own = (SELECT parent from user where id = ?)";
			Integer pUid = jdbcTemplate.queryForObject(sql,Integer.class,new Object[]{userId});
			return pUid;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Document> find(String dbName, String collectionName, Integer userId) {
		MongoCollection<Document> collection = getCollection(dbName, collectionName);
		Document filter = new Document();
		filter.append("userid", userId);
		List<Document> results = new ArrayList<Document>();
		FindIterable<Document> iterables = collection.find(filter);
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			results.add(cursor.next());
		}
		return results;
	}
}
