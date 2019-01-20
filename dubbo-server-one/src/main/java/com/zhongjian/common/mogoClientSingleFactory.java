package com.zhongjian.common;



import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

/*
 * mongodb数据库链接池
 */
public class mogoClientSingleFactory 
{
	public static MongoClient mongoClient;

	static {
		 MongoClientOptions.Builder buide = new MongoClientOptions.Builder();
         buide.connectionsPerHost(50);// 与目标数据库可以建立的最大链接数
         buide.minConnectionsPerHost(5);
         buide.connectTimeout(1000 * 60 * 20);// 与数据库建立链接的超时时间
         buide.maxWaitTime(5000);// 一个线程成功获取到一个可用数据库之前的最大等待时间
         buide.threadsAllowedToBlockForConnectionMultiplier(100);
         buide.maxConnectionIdleTime(0);
         buide.maxConnectionLifeTime(0);
         buide.threadsAllowedToBlockForConnectionMultiplier(5);
         buide.socketTimeout(0);
         MongoClientOptions myOptions = buide.build();
         mongoClient = new MongoClient(new ServerAddress("192.168.1.253", 27017), myOptions);
	}
}