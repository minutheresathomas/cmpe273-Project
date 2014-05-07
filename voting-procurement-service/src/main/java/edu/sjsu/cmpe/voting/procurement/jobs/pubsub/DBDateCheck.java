package edu.sjsu.cmpe.voting.procurement.jobs.pubsub;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import edu.sjsu.cmpe.voting.procurement.domain.Poll;
import edu.sjsu.cmpe.voting.procurement.domain.PollEmail;


public class DBDateCheck {
	MongoClient mongoClient;
	DB db;
	DBCollection collection;
	DBCollection collection1;
	
	public DBDateCheck() throws UnknownHostException
	{
		mongoClient = new MongoClient();
		db = mongoClient.getDB("sms-voting");
		collection = db.getCollection("polls");
		collection1 = db.getCollection("users");
	}
	
	public List<PollEmail> checkEndDate()
	{
		System.out.println("Checking DB : " +db);
		List<PollEmail> pollEmails = new ArrayList<PollEmail>();
		DBCursor cursor = collection.find();
	    try {
		    while(cursor.hasNext())
		    {
		    	DBObject pollObject = cursor.next();
		    	Poll copyPoll = new Poll();
		    	Date expirationDate = (Date) pollObject.get("EndDate");
		    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    	Date today = new Date();
		    	String expDate = sdf.format(expirationDate);
		    	String todayDate = sdf.format(today);
		    	System.out.println("expiration : "+sdf.format(expirationDate));
		    	System.out.println("today : "+sdf.format(today));
		    	if(todayDate.equals(expDate))
		    	{
		    		PollEmail pollEmail = new PollEmail();
		    		String expiredpoll = (String) pollObject.get("_id");
		    		pollEmail.setPollId(expiredpoll);
		    		System.out.println("poll id : "+expiredpoll);
		    		BasicDBObject query = new BasicDBObject("role", "Admin");
		    		DBCursor cur = collection1.find(query);
		    		try {
		    			while(cur.hasNext())
		    			{
		    				DBObject obj = cur.next();
		    				BasicDBList hisPolls =  (BasicDBList) obj.get("createdPolls");
		    				for(int i=0 ; i<hisPolls.size() ; i++)
		    				{
		    					if((hisPolls.get(i)).equals(expiredpoll))
		    					{
		    						String uId = (String) obj.get("user_id");
		    						pollEmail.setUserId(uId);
		    						pollEmails.add(pollEmail);
		    						System.out.println("Email id : "+uId);
		    						break;
		    					}
		    				}
		    			}
		    		} finally
		    		{
		    			cur.close();
		    		}
		    	}
		    }
		    } 
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    } finally
		    {
		    	cursor.close();
		    }
		return pollEmails;
	}
}
