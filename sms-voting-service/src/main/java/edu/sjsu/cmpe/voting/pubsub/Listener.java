package edu.sjsu.cmpe.voting.pubsub;

import java.net.MalformedURLException;
import java.net.URL;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.fusesource.stomp.jms.message.StompJmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.cmpe.voting.SmsVotingServiceConfiguration;
import edu.sjsu.cmpe.voting.api.Poll;
import edu.sjsu.cmpe.voting.repository.PollsRepositoryInterface;
import edu.sjsu.cmpe.voting.repository.UserDBRepositoryInterface;
import edu.sjsu.cmpe.voting.util.AmazonSESSample;

public class Listener {
	private SmsVotingServiceConfiguration serviceConfig;
	private PollsRepositoryInterface pollsRepository;
	private UserDBRepositoryInterface usersRepository;
	
	public Listener(SmsVotingServiceConfiguration serviceConfig, 
			PollsRepositoryInterface pollsRepository, 
			UserDBRepositoryInterface usersRepository)
	{
		this.serviceConfig = serviceConfig;
		this.pollsRepository = pollsRepository;
		this.usersRepository =usersRepository;
	}
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public void listenFromTopic() throws JMSException, MalformedURLException
	{
		String user = env("APOLLO_USER", serviceConfig.getApolloUser());
		String password = env("APOLLO_PASSWORD", serviceConfig.getApolloPassword());
		String host = env("APOLLO_HOST", serviceConfig.getApolloHost());
		System.out.println("Service config port is : "+serviceConfig.getApolloPort());
		int port = Integer.parseInt(serviceConfig.getApolloPort());
		
		String topic = serviceConfig.getStompTopicPrefix();
		System.out.println("topic is : "+topic);
		String destination = topic;
		System.out.println("destination is : "+ destination);
		
		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://" + host + ":" + port);
		Connection connection = factory.createConnection(user, password);
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination dest = new StompJmsDestination(destination);
		MessageConsumer consumer = session.createConsumer(dest);
		while(true) {
			Message msg = consumer.receive();
		    if( msg instanceof  TextMessage || msg instanceof StompJmsMessage) {
		           String body = ((TextMessage) msg).getText();
		           System.out.println("------------------------------------------------");
		           System.out.println("Received message = " + body);
		           if(!"SHUTDOWN".equals(body)) {
			           log.debug("Received message is : ", body);
			           // Process the received message and add it to the hashMap
			           processMessage(body);
			           System.out.println("===========================");
			           System.out.println("from topic : "+topic);
			           System.out.println("===========================");
			           System.out.println("------------------------------------------------");
		           }
		           else
		           {
		        	   System.out.println("exiting from the loop..");
		        	   break;
		           }
		    } else {
		         System.out.println("Unexpected message type: " + msg.getClass());
		    }
		} // end while loop
		connection.close();
		System.out.println("Done");
	}
	
	private String env(String key, String defaultValue)
	{
		String val = System.getenv(key);
		if(val == null)
			return defaultValue;
		return val;
	}
	
	private void processMessage(String body) throws MalformedURLException
	{
		String[] segments = body.split(":", 2);
		Poll newPoll = new Poll();
		

     	   System.out.println("Segment: " + segments);
     	   // Iterate through hash map to check the isbn alreay exists or not
     	   if((!segments[0].isEmpty()) && (!segments[1].isEmpty())) 
     			   
     	   {
     		   String userId = segments[0];
     		   String pollId = segments[1];
     		   Poll poll = pollsRepository.getPollById(pollId);
     		   if((poll != null))
     		   {
     			   	URL url = new URL("localhost:8080/sms-voting/web/polls/index");
     			  	StringBuilder sb = new StringBuilder();
     			  	sb.append("Poll with poll Id "+ pollId + "has been expired." + "Please check the statistics in the following URL");
     			  	sb.append(url);
     			  	String email_body = sb.toString();
     			  	String email_subject = "Alert on Polls";
     			  	try {
     			  	AmazonSESSample aws = new AmazonSESSample();
//     			  		AWSEmail aws = new AWSEmail();
     			  	aws.sendEmail("shivadeepthitoopran@gmail.com", userId, email_subject, email_body);
     			  	}
     			  	catch(Exception e)
     			  	{
     			  		e.printStackTrace();
     			  	}
     		   }
     		   
     	   }
    }
}
