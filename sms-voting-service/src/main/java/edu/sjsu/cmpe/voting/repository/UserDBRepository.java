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
import edu.sjsu.cmpe.voting.api.Poll;
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
			//Generate New Unique id for the User
			String key = newUser.getUser_id();
			newUser.set_id(key);
			newUser.setRole();
			newUser.setCreationDate();
			// add the new user to the DB
		
			BasicDBObject userDoc = new BasicDBObject();
			userDoc.put("_id", newUser.get_id());
			userDoc.put("user_id", newUser.getUser_id());
			userDoc.put("creationDate", newUser.getCreationDate());
			userDoc.put("country", newUser.getCountry());
			userDoc.put("name", newUser.getName());
			userDoc.put("password", newUser.getPassword());
			userDoc.put("votedPolls", newUser.getVotedPollIds());
			userDoc.put("createdPolls", newUser.getCreatedPollIds());
			userDoc.put("role", newUser.getRole());
			userDoc.put("creationDate", newUser.getCreationDate());
			collection1.insert(userDoc);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return newUser;
	}
	
	public String authenticateUser(String user_id)
	{
			String DBemail=null;
			BasicDBObject query=new BasicDBObject("user_id",user_id);
			DBCursor cursor=collection1.find(query);
			try{
				while(cursor.hasNext()){
					DBObject obj=cursor.next();
					DBemail=(String) obj.get("user_id");
					if(user_id.equals(DBemail)){
					System.out.print("user authenticated");
					}		 
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return DBemail;
	}
	
	public boolean updateUserPolls(String userId, String pollId)
	{
		boolean flag= false;
			BasicDBObject query = new BasicDBObject("user_id", userId);
			DBCursor cursor = collection1.find(query);
			try {
				if(cursor.hasNext())
				{
					DBObject userDoc = cursor.next();
					BasicDBList pollIds = (BasicDBList) userDoc.get("votedPolls");
					if(pollIds != null)
					{
						for(int i=0; i<pollIds.size(); i++)
						{
							if(pollId == pollIds.get(i))
								flag = true;
						}
						if(!flag)
							pollIds.add(pollId);
						else
							return false;
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
			return true;
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
	
	public void updateCreatedPoll(String user_id, String pollId)
	{
		BasicDBObject query = new BasicDBObject("user_id", user_id);
		DBCursor cursor = collection1.find(query);
		try
		{
			if(cursor.hasNext())
			{
				DBObject userDoc = cursor.next();
				BasicDBList pollIds = (BasicDBList) userDoc.get("createdPolls");
				System.out.println("created poll !");
				if(pollIds != null)
				{
					pollIds.add(pollId);
				}
				else
				{
					pollIds = new BasicDBList();
					pollIds.add(pollId);
				}
				userDoc.put("createdPolls", pollIds);
				collection1.save(userDoc);
			}
		}
		finally
		{
			cursor.close();
		}
	}
	
	public List<Poll> getUserSpecificPolls(String user_id)
	{
		ArrayList<String> array=new ArrayList<String>();
	    BasicDBObject query = new BasicDBObject("user_id",user_id).append("role","Admin" );
	    DBCursor cursor = collection1.find(query);
	    try {
	        while(cursor.hasNext())
	        {
	            DBObject userDoc = cursor.next();
	            array= (ArrayList<String>) userDoc.get("createdPolls");
	        }
	    }
	   finally
	    {
	        cursor.close();
	    }
	    List<Poll> hisPolls = new ArrayList<Poll>();
	    for(int i=0;i<array.size();i++)
	    {
	        String arrayElement=array.get(i).trim();
	        System.out.println("poll id-----------: "+i+"::"+arrayElement);
	        BasicDBObject query1 = new BasicDBObject("_id",arrayElement);
	        DBCursor cursor1 =collection.find(query1);
	        try {
	            while(cursor1.hasNext())
	            {
	            	Poll newPoll = new Poll();
	                DBObject userDoc = cursor1.next();
	                newPoll.setId((String) userDoc.get("_id"));
	                newPoll.setQuestion((String) userDoc.get("question"));
	                newPoll.setChoices((List<Choice>) userDoc.get("Choices"));
	                newPoll.setStartDate((Date) userDoc.get("StartDate"));
	                newPoll.setEndDate((Date) userDoc.get("EndDate"));
	                hisPolls.add(newPoll);
	           }
	        }finally
	        {
	            cursor.close();
	        }
	    }
	    return hisPolls;
	}
}
