package edu.sjsu.cmpe.voting;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;

import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;

import edu.sjsu.cmpe.voting.SmsVotingServiceConfiguration;
import edu.sjsu.cmpe.voting.api.Poll;
import edu.sjsu.cmpe.voting.pubsub.Listener;
import edu.sjsu.cmpe.voting.repository.PollsDBRepository;
import edu.sjsu.cmpe.voting.repository.PollsRepositoryInterface;
import edu.sjsu.cmpe.voting.repository.UserDBRepository;
import edu.sjsu.cmpe.voting.repository.UserDBRepositoryInterface;
import edu.sjsu.cmpe.voting.resources.UserPollResource;
import edu.sjsu.cmpe.voting.ui.resources.UserResource;

public class SmsVotingService extends Service<SmsVotingServiceConfiguration>{
	
	String apolloUser = null;
    String apolloPassword = null;
    String apolloHost = null;
    String apolloPort = null;
	public static void main(String Args[]) throws Exception
	{
		new SmsVotingService().run(Args);
	}
	
	@Override
	public void initialize(Bootstrap<SmsVotingServiceConfiguration> bootstrap)
	{
		bootstrap.setName("sms-voting-service");
		bootstrap.addBundle(new ViewBundle());
		bootstrap.addBundle(new AssetsBundle());
	}
	
	@Override
	public void run(SmsVotingServiceConfiguration config,
					Environment environment) throws UnknownHostException
	{
		environment.setJerseyProperty(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, 
				LoggingFilter.class.getName());
    	environment.setJerseyProperty(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, 
    			LoggingFilter.class.getName());
    	
    	String topicName = config.getStompTopicPrefix();
    	// TODO: Apollo STOMP Broker URL and login
    	apolloUser = config.getApolloUser();
    	apolloPassword = config.getApolloPassword();
    	apolloHost = config.getApolloHost();
    	apolloPort = config.getApolloPort();
//    	log.debug("{} - Topic name is {}. User name is {}. Host is {}. Port is {}",
//    		 topicName, apolloUser, apolloHost, apolloPort);
    	 System.out.println("topic: " +topicName+ " user : "+ apolloUser + " host : " +apolloHost + " port : " + apolloPort);
    	
    	/** Sms-Voting Moderator APIs */
//    	PollsRepositoryInterface pollsRepository = new PollsRepository(
//    		new ConcurrentHashMap<String, Poll>());
    	PollsRepositoryInterface pollsRepository = new PollsDBRepository();
    	UserDBRepositoryInterface usersRepository = new UserDBRepository();
//    	environment.addResource(new ModeratorPollResource(pollsRepository));
    	backGroundThread(config, pollsRepository, usersRepository);
    	/** Sms-Voting User APIs */
    	environment.addResource(new UserPollResource(pollsRepository, usersRepository));
    	
    	/** Sms-Voting User UI */
    	environment.addResource(new UserResource(pollsRepository, usersRepository));
//    	environment.addResource(new AdminResource(pollsRepository));
	}
	
	public void backGroundThread(final SmsVotingServiceConfiguration configuration, 
   		 final PollsRepositoryInterface bookRepository, 
   		 final UserDBRepositoryInterface usersRepository)
   {
   	int numThreads = 1;
	    ExecutorService executor = Executors.newFixedThreadPool(numThreads);

	    Runnable backgroundTask = new Runnable() {

   	    @Override
   	    public void run() {
   	    	Listener listener = new Listener(configuration, bookRepository, usersRepository);
   	    	try {
   	    		System.out.println("****#####*****#####");
					listener.listenFromTopic();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
   		System.out.println("Hello World");
   	    }
	    };
	    System.out.println("About to submit the background task");
	    try {
	    	executor.execute(backgroundTask);
	    	System.out.println("Submitted the background task");
	    }
	    catch(Exception e) {
	    	executor.shutdown();
	    	System.out.println("Finished the background task");
	    	e.printStackTrace();
	    }
   }
}
