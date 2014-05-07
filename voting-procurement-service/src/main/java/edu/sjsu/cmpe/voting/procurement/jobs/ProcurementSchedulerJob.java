package edu.sjsu.cmpe.voting.procurement.jobs;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.Every;
import edu.sjsu.cmpe.voting.procurement.ProcurementService;
import edu.sjsu.cmpe.voting.procurement.domain.PollEmail;
import edu.sjsu.cmpe.voting.procurement.jobs.pubsub.DBDateCheck;
import edu.sjsu.cmpe.voting.procurement.jobs.pubsub.Publisher;


/**
 * This job will run at every 5 second.
 */
//@On("0 0 12 * * ?")
 @Every("30s")
//@Every("24h")
public class ProcurementSchedulerJob extends Job {
    private final Logger log = LoggerFactory.getLogger(getClass()); 

    @Override
    public void doJob() {
    	System.out.println("getting the job");
    	List<PollEmail> response = new ArrayList<PollEmail>();
//		String strResponse = ProcurementService.jerseyClient.resource(
//			"http://ip.jsontest.com/").get(String.class);
//		log.debug("Response from jsontest.com: {}", strResponse);
		try
		{
			/**
			 * Do job to consume the book orders from the Apollo queue
			 */
			DBDateCheck dateCheck = new DBDateCheck();
			response = dateCheck.checkEndDate();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try {
			if(response != null)
				Publisher.publishEmailPoll(response);
			}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
    
}
