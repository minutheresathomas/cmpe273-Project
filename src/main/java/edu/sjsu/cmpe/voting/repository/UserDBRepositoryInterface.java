package edu.sjsu.cmpe.voting.repository;

import edu.sjsu.cmpe.voting.api.User;

public interface UserDBRepositoryInterface {
	
	User saveUser(User newUser) throws Exception;
	void updateUserPolls(String userId, String pollId); 
	void updateUserRole(String userId);
}
