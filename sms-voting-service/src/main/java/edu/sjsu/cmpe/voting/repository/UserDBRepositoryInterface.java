package edu.sjsu.cmpe.voting.repository;

import java.util.List;

import edu.sjsu.cmpe.voting.api.User;
import edu.sjsu.cmpe.voting.api.Poll;

public interface UserDBRepositoryInterface {
	
	User saveUser(User newUser) throws Exception;
	boolean updateUserPolls(String userId, String pollId); 
	void updateUserRole(String userId);
	String authenticateUser(String user_id);
	void updateCreatedPoll(String user_id, String pollId);
	List<Poll> getUserSpecificPolls(String user_id);
}
