package edu.sjsu.cmpe.voting.procurement.jobs.pubsub;

import java.util.List;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.cmpe.voting.procurement.ApolloConfig;
import edu.sjsu.cmpe.voting.procurement.domain.PollEmail;

public class Publisher {
	private final static Logger log = LoggerFactory.getLogger(Publisher.class);
	private static boolean flag = false;
	
	public static void publishEmailPoll(List<PollEmail> polls) throws JMSException
	{
		ApolloConfig serviceConfig = new ApolloConfig();
		String user = env("APOLLO_USER", serviceConfig.getUser());
		String password = env("APOLLO_PASSWORD", serviceConfig.getPassword());
		String host = env("APOLLO_HOST", serviceConfig.getHost());
		int port = Integer.parseInt(env("APOLLO_PORT", serviceConfig.getPort()));
		// Get Topic Destination
		System.out.println("serviceConfig.getTopic() : "+serviceConfig.getTopic());
		
		
		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://" + host + ":" + port);
		Connection connection = factory.createConnection(user, password);
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		/**
		 * Generate the message for each topic
		 *  	
		 */
	
		//generate the message to be delivered
		for (int i=0 ;i<polls.size();i++) {
				String destinationB = serviceConfig.getTopic();
				
				System.out.println("Topic destination : "+destinationB);
				Destination dest = new StompJmsDestination(destinationB);
				MessageProducer producer = session.createProducer(dest);
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				StringBuilder sb = new StringBuilder();
				
				sb.append(String.valueOf(polls.get(i).getUserId()));
				sb.append(":").append(polls.get(i).getPollId());
				String message = sb.toString();
				
				TextMessage msg = session.createTextMessage(message);
				msg.setLongProperty("id", System.currentTimeMillis());
				System.out.println("#######################################");
				System.out.println("Message is : "+ message);
				System.out.println("TO topic is : "+ destinationB);
//				log.debug("Text message : " , msg.toString());
//				log.debug("Message is : ", message);
				if(!flag)
				{
					producer.send(msg);
					flag = true;
				}
			}
		connection.close();
	}
		private static String env(String key, String defaultValue)
		{
			String val = System.getenv(key);
			if(val == null)
				return defaultValue;
			return val;
		}
}
