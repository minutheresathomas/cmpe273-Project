package edu.sjsu.cmpe.voting.resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;




//import edu.sjsu.cmpe.voting.api.Choice;
import edu.sjsu.cmpe.voting.api.Poll;
import edu.sjsu.cmpe.voting.api.User;
import edu.sjsu.cmpe.voting.dto.PollDto;
import edu.sjsu.cmpe.voting.dto.PollsDto;
import edu.sjsu.cmpe.voting.repository.PollsRepositoryInterface;
import edu.sjsu.cmpe.voting.repository.UserDBRepositoryInterface;

@Path("/v1/polls")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserPollResource {
	
	private final PollsRepositoryInterface pollsRepository;
	private final UserDBRepositoryInterface usersRepository;

	/**
	 * User Polls resource
	 * @param pollsRepository
	 */
	public UserPollResource(PollsRepositoryInterface pollsRepository, 
			UserDBRepositoryInterface usersRepository) {
		this.pollsRepository = pollsRepository;
		this.usersRepository = usersRepository;
	}
	
	/**
	 * 1. Create New Poll
	 * 		Resource : POST - /polls
	 * 		Description : add a new Poll by adding questions and the choices
	 * 
	 */
	@POST
	@Path("/{user_id}/createPoll")
	@Timed(name= "create-poll")
	public HashMap<String, Object> createNewPoll(@PathParam("user_id") String user_id,
									@Valid Poll request) throws Exception
	{
		HashMap<String, Object> myMap = new HashMap<String, Object>();
		try {
		Poll poll = pollsRepository.savePoll(request);
		usersRepository.updateUserRole(user_id);
		System.out.println("Poll is : "+poll.getQuestion() + " : Option is :"+poll.getChoices());
		List<PollDto> pollLink = new ArrayList<PollDto>();
		pollLink.add(new PollDto("view-all-polls", "/polls" , "GET"));
		pollLink.add(new PollDto("view-poll", "/polls/" + poll.getId() , "GET"));
		myMap.put("polls", pollLink);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return myMap;
	}
	
	/**
	 * 1.1 Create New User
	 * 		Resource : POST - /user
	 * 		Description : add a new User
	 * 
	 */
	@POST
	@Path("/createUser")
	@Timed(name= "create-poll")
	public HashMap<String, Object> createNewUser(@Valid User request) throws Exception
	{
		HashMap<String, Object> myMap = new HashMap<String, Object>();
		try {
		User user = usersRepository.saveUser(request);
		System.out.println("email id is : "+user.getEmail_id());
		List<PollDto> userLink = new ArrayList<PollDto>();
		userLink.add(new PollDto("view-all-users", "/users" , "GET"));
		userLink.add(new PollDto("view-user", "/users/" + user.getUser_id() , "GET"));
		myMap.put("users", userLink);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return myMap;
	}
	
	/**
	 * 2. Get the Poll by Id
	 * 		Resource : GET - /polls/id
	 * 		Description : get an existing Poll from the repository
	 * 
	 */
	@GET
	@Timed(name="view-poll")
	@Path("/{user_id}/{id}")
	public HashMap<String, Object> viewPollById(@PathParam("id") String id) 
	{
		Poll pollById = pollsRepository.getPollById(id);
		HashMap<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("Poll", pollById);
		return myMap;
	}
	
	/**
	 * 3. Get the Polls
	 * 		Resource : GET - /polls/id
	 * 		Description : get an existing Poll from the repository
	 * 
	 */
	@GET
	@Path("/{user_id}/viewPolls")
	@Timed(name="view-all-polls")	
	public HashMap<String, Object> viewAllPolls()
	{
		List<Poll> allPolls = new ArrayList<Poll>();
		allPolls = pollsRepository.getPolls();
		HashMap<String, Object> pollsMap = new HashMap<String, Object>();
		List<PollDto> pollLinks = new ArrayList<PollDto>();
		for(Poll p : allPolls)
		{
			pollsMap.put(p.getId(), p.getQuestion());
			pollLinks.add(new PollDto("view-poll", "/polls/"+p.getId(), "GET"));
		}
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("Questions", pollsMap);
		responseMap.put("Links", pollLinks);
		return responseMap;
	}
	
	/**
	 * 4. Delete the Poll by id
	 * 		Resource : GET - /polls/id
	 * 		Description : get an existing Poll from the repository
	 * 
	 */
	@DELETE
    @Path("/{user_id}/{id}")
    @Timed(name = "delete-poll")
    public Response deletePollById(@PathParam("id") String id)
    {
    	try {
    		pollsRepository.removePoll(id);
	    	PollsDto links = new PollsDto();
	    	links.addLink(new PollDto("create-poll", "/polls", "POST"));
	    	return Response.ok(links).build();
    	}
    	catch(WebApplicationException e) {
    		e.printStackTrace();
			return Response.noContent().build();
    	}
    }
	
	/**
	 * 
	 * 5. Update the creation date if the poll
	 * @throws ParseException 
	 */
	
	@PUT
    @Path("/{user_id}/{id}")
    @Timed(name = "update-poll")
    public Response updateDatePollById(@PathParam("id") String id,
    		@QueryParam("endDate") String dateString) throws ParseException
    {
		try {
	    	DateFormat formatter ; 
	    	Date date ; 
	    	   formatter = new SimpleDateFormat("yyyy-MM-dd");
	    	   date = formatter.parse(dateString);
	    	System.out.println("Date input is : "+date);
	        pollsRepository.updatePollDate(id,date);
	    	PollsDto links = new PollsDto();
	    	links.addLink(new PollDto("view-poll", "/polls", "GET"));
	    	return Response.ok(links).build();
    	}
    	catch(WebApplicationException e) {
    		e.printStackTrace();
			return Response.noContent().build();
    	}
    }
	
	/**
	 * 6. choose an option
	 * 		Resource : PUT - /polls/{id}/?option={option}
	 */
	@PUT
	@Timed(name="participate-poll")
	@Path("/{user_id}/participate/{id}")
	public HashMap<String, Object> participatePoll(@PathParam("user_id") String user_id,
							@PathParam("id") String id, 
							@QueryParam("option") String option)
	{
		pollsRepository.setCountForOption(id, option);
		usersRepository.updateUserPolls(user_id, id);
		Poll pollById = pollsRepository.getPollById(id);
		HashMap<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("Poll", pollById);
		return myMap;
	}
	
	/**
	 * 7. Search with sub string
	 * 		Resource : GET - /polls/?question={que}
	 * 		Param - question sub-string
	 */
	@GET
	@Timed(name="search-poll")
	@Path("/{user_id}/searchPolls")
	public HashMap<String, Object> searchPoll(@QueryParam("question") String que)
	{
		List<Poll> polls = pollsRepository.getPollByQue(que);
		HashMap<String, Object> myMap = new HashMap<String, Object>();
		List<PollDto> pollLinks = new ArrayList<PollDto>();
		for(Poll p : polls)
		{
			myMap.put(p.getId(), p.getQuestion());
			pollLinks.add(new PollDto("view-poll", "/polls/"+p.getId(), "GET"));
		}
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("Questions", myMap);
		responseMap.put("Links", pollLinks);
		return responseMap;
	}
}
