/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.experisit.my_processmining.storage;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 *
 * @author youssef.hissou
 */
public class MongoUtils {
    public static DB connect(String nameDb){
        MongoClient mongo = new MongoClient( "192.168.99.100" , 32768);
        DB db = mongo.getDB(nameDb);
        return db;
    }
    
    public static DBCollection getCollection(DB mongo, String nameCollection){
        return mongo.getCollection(nameCollection);
    }
}
