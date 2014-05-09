package edu.sjsu.cmpe.voting.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class User {
	@JsonProperty
	private String _id;
	@JsonProperty
	private String user_id;
	@JsonProperty
	private Date creationDate;
	@JsonProperty
	private String Country;
	@JsonProperty
	private String name;
	@JsonProperty
	private List<String> votedPollIds;
	@JsonProperty
	private String password;
	@JsonProperty
	private String role;
	@JsonProperty
	private List<String> createdPollIds;
	
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate() {
		Date date = new Date();
		this.creationDate = date;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getVotedPollIds() {
		return votedPollIds;
	}
	public void setVotedPollIds(List<String> votedPollIds) {
		this.votedPollIds = votedPollIds;
	}
	public List<String> getCreatedPollIds() {
		return createdPollIds;
	}
	public void setCreatedPollIds(List<String> createdPollIds) {
		this.createdPollIds = createdPollIds;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		HashFunction hash = Hashing.md5();
		if(password != null)
			this.password = hash.hashString(password, Charsets.UTF_8).toString();
		else
			this.password = hash.hashString("password", Charsets.UTF_8).toString();
	}
	public String getRole() {
		return role;
	}
	public void setRole() {
		this.role = "User";
	}
	
}
