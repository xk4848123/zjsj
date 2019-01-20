package com.zhongjian.dao;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zhongjian.common.mogoClientSingleFactory;

/*
 * mongodb数据库链接池
 */
public class MongoDBDaoBase
{
    protected MongoClient mongoClient = mogoClientSingleFactory.mongoClient;

    protected MongoDatabase getDb(String dbName)
    {
        return mongoClient.getDatabase(dbName);
    }

    protected MongoCollection<Document> getCollection(String dbName, String collectionName)
    {
    	MongoDatabase db = mongoClient.getDatabase(dbName);
    	MongoCollection<Document> collection = db.getCollection(collectionName);
    	return collection;
    }

 
}