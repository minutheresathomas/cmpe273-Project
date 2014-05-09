package edu.sjsu.cmpe.voting;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class SmsVotingServiceConfiguration extends Configuration {
	
    @NotEmpty
    @JsonProperty
    private String stompTopicPrefix;

    @NotEmpty
    @JsonProperty
    private String apolloUser;
    
    @NotEmpty
    @JsonProperty
    private String apolloPassword;
    
    @NotEmpty
    @JsonProperty
    private String apolloHost;
    
    @NotEmpty
    @JsonProperty
    private String apolloPort;



	public String getStompTopicPrefix() {
		return stompTopicPrefix;
	}

	public void setStompTopicPrefix(String stompTopicPrefix) {
		this.stompTopicPrefix = stompTopicPrefix;
	}

	/**
	 * @return the apolloUser
	 */
	public String getApolloUser() {
		return apolloUser;
	}

	/**
	 * @param apolloUser the apolloUser to set
	 */
	public void setApolloUser(String apolloUser) {
		this.apolloUser = apolloUser;
	}

	/**
	 * @return the apolloPassword
	 */
	public String getApolloPassword() {
		return apolloPassword;
	}

	/**
	 * @param apolloPassword the apolloPassword to set
	 */
	public void setApolloPassword(String apolloPassword) {
		this.apolloPassword = apolloPassword;
	}

	/**
	 * @return the apolloHost
	 */
	public String getApolloHost() {
		return apolloHost;
	}

	/**
	 * @param apolloHost the apolloHost to set
	 */
	public void setApolloHost(String apolloHost) {
		this.apolloHost = apolloHost;
	}

	/**
	 * @return the apolloPort
	 */
	public String getApolloPort() {
		return apolloPort;
	}

	/**
	 * @param apolloPort the apolloPort to set
	 */
	public void setApolloPort(String apolloPort) {
		this.apolloPort = apolloPort;
	}
}
