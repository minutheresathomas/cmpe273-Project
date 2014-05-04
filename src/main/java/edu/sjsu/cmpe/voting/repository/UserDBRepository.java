package edu.sjsu.cmpe.voting.repository;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import edu.sjsu.cmpe.voting.api.Choice;
import edu.sjsu.cmpe.voting.api.User;

public class UserDBRepository implements UserDBRepositoryInterface {
		MongoClient mongoClient;
		DB db;
		DBCollection collection;
		DBCollection collection1;
		
		public UserDBRepository() throws UnknownHostException
		{
			mongoClient = new MongoClient();
			db = mongoClient.getDB("sms-voting");
			collection = db.getCollection("polls");
			collection1 = db.getCollection("users");
		}
		
		public static String createID()
		{
			return UUID.randomUUID().toString().substring(0, 4);
		}
	
	@Override
	public User saveUser(User newUser) throws Exception { 
		checkNotNull(newUser,"newUser must not be null to add to PollRepository");
		try {
			//Generate New Unique id for the Poll
			String key = newUser.getUser_id();
			newUser.set_id(key);
			newUser.setRole();
			newUser.setCreationDate();
			// add the new poll to the DB
		
			BasicDBObject userDoc = new BasicDBObject();
			userDoc.put("_id", newUser.get_id());
			userDoc.put("user_id", newUser.getUser_id());
			userDoc.put("email_id", newUser.getEmail_id());
			userDoc.put("creationDate", newUser.getCreationDate());
			userDoc.put("country", newUser.getCountry());
			userDoc.put("name", newUser.getName());
			userDoc.put("password", newUser.getPassword());
			userDoc.put("votedPolls", newUser.getPollIds());
			userDoc.put("role", newUser.getRole());
			userDoc.put("creationDate", newUser.getCreationDate());
			collection1.insert(userDoc);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateUserPolls(String userId, String pollId)
	{
			BasicDBObject query = new BasicDBObject("user_id", userId);
			DBCursor cursor = collection1.find(query);
			try {
				if(cursor.hasNext())
				{
					DBObject userDoc = cursor.next();
					BasicDBList pollIds = (BasicDBList) userDoc.get("votedPolls");
					if(pollIds != null)
					{
						pollIds.add(pollId);
					}
					else
					{
						pollIds = new BasicDBList();
						pollIds.add(pollId);
					}
					userDoc.put("votedPolls", pollIds);
					collection1.save(userDoc);
				}
			} finally {
				cursor.close();
			}
	}
	
	public void updateUserRole(String userId)
	{
		BasicDBObject query = new BasicDBObject("user_id", userId);
		DBCursor cursor = collection1.find(query);
		try
		{
			if(cursor.hasNext())
			{
				DBObject userDoc = cursor.next();
				userDoc.put("role", "Admin");
				collection1.save(userDoc);
			}
		}
		finally
		{
			cursor.close();
		}
	}
}
