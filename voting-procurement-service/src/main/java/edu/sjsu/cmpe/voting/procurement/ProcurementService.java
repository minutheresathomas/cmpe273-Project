package edu.sjsu.cmpe.voting.procurement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.client.JerseyClientBuilder;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import de.spinscale.dropwizard.jobs.JobsBundle;
import edu.sjsu.cmpe.voting.procurement.api.resources.RootResource;
import edu.sjsu.cmpe.voting.procurement.config.ProcurementServiceConfiguration;

public class ProcurementService extends Service<ProcurementServiceConfiguration> {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    String apolloUser = null;
    String apolloPassword = null;
    String apolloHost = null;
    String apolloPort = null;

    /**
     * FIXME: THIS IS A HACK!
     */
    public static Client jerseyClient;

    public static void main(String[] args) throws Exception {
	new ProcurementService().run(args);
    }

    @Override
    public void initialize(Bootstrap<ProcurementServiceConfiguration> bootstrap) {
	bootstrap.setName("procurement-service");
	/**
	 * NOTE: All jobs must be placed under edu.sjsu.cmpe.procurement.jobs
	 * package
	 */
	bootstrap.addBundle(new JobsBundle("edu.sjsu.cmpe.voting.procurement.jobs"));
    }

    @Override
    public void run(ProcurementServiceConfiguration configuration,
	    Environment environment) throws Exception {
	jerseyClient = new JerseyClientBuilder()
	.using(configuration.getJerseyClientConfiguration())
	.using(environment).build();

	/**
	 * Root API - Without RootResource, Dropwizard will throw this
	 * exception:
	 * 
	 * ERROR [2013-10-31 23:01:24,489]
	 * com.sun.jersey.server.impl.application.RootResourceUriRules: The
	 * ResourceConfig instance does not contain any root resource classes.
	 */
	environment.addResource(RootResource.class);

	String topicName = configuration.getStompTopicPrefix();
	// TODO: Apollo STOMP Broker URL and login
	apolloUser = configuration.getApolloUser();
	apolloPassword = configuration.getApolloPassword();
	apolloHost = configuration.getApolloHost();
	apolloPort = configuration.getApolloPort();
//	log.debug("{} - Topic name is {}. User name is {}. Host is {}. Port is {}",
//		 topicName, apolloUser, apolloHost, apolloPort);
	 System.out.println("topic: " +topicName+ " user : "+ apolloUser + " host : " +apolloHost + " port : " + apolloPort);
	ApolloConfig configure = new ApolloConfig(apolloUser, apolloPassword, 
			apolloHost, apolloPort, topicName);
    }
}
