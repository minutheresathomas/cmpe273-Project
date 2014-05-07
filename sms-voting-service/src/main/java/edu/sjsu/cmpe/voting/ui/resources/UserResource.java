package edu.sjsu.cmpe.voting.ui.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.sjsu.cmpe.voting.api.Poll;
import edu.sjsu.cmpe.voting.repository.PollsRepositoryInterface;
import edu.sjsu.cmpe.voting.repository.UserDBRepositoryInterface;
import edu.sjsu.cmpe.voting.ui.views.IndexView;
import edu.sjsu.cmpe.voting.ui.views.LoginView;
import edu.sjsu.cmpe.voting.ui.views.CreatePollView;
import edu.sjsu.cmpe.voting.ui.views.PollView;
import edu.sjsu.cmpe.voting.ui.views.RegisterView;
import edu.sjsu.cmpe.voting.ui.views.RenewPollView;
import edu.sjsu.cmpe.voting.ui.views.SearchView;
import edu.sjsu.cmpe.voting.ui.views.UserView;

@Path("/web/polls/")
@Produces(MediaType.TEXT_HTML)
public class UserResource {
	private final PollsRepositoryInterface pollRepository;
	private final UserDBRepositoryInterface usersRepository;

    public UserResource(PollsRepositoryInterface pollRepository,
    		UserDBRepositoryInterface usersRepository) {
	this.pollRepository = pollRepository;
	this.usersRepository = usersRepository;
    }
    
    @GET
    @Path("index")
    public IndexView getUserIndex() {
		return new IndexView();
    }

    @GET
    @Path("login")
    public LoginView getUserLogin() {
		return new LoginView();
    }
    
    @GET
    @Path("register")
    public RegisterView getUserRegister() {
		return new RegisterView();
    }
    
    @GET
    @Path("{user_id}")
    public UserView getUserHome() {
	//return new HomeView(bookRepository.getBookByISBN(1L));
    	System.out.println("getting the polls!...");
    	return new UserView(pollRepository.getPolls());
    }
    
    @GET
    @Path("{user_id}/{id}")
    public PollView getIndividualPoll(@PathParam("id") String id) {
    	System.out.println("getting the poll based on id!...");
    	Poll myPoll = pollRepository.getPollById(id);
    	return new PollView(myPoll);
    }
    
    @GET
    @Path("{user_id}/createPoll")
    public CreatePollView getCreatePoll() {
    	CreatePollView a = new CreatePollView();
    	return a;
    }
    
    @GET
    @Path("{user_id}/renewPolls")
    public RenewPollView getRenewPollsPage(@PathParam("user_id") String user_id) {
		System.out.println("getting the polls!...");
    	return new RenewPollView(usersRepository.getUserSpecificPolls(user_id));
    }
    
    /**
     * Search for polls by question sub string
     * @param que
     * @return
     */
    @GET
    @Path("{user_id}/searchPolls")
    public SearchView getSearchPoll(@QueryParam("question") String que) {
    	System.out.println("getting the poll based on search with question sub string!...");
    	List<Poll> myPolls = pollRepository.getPollByQue(que);
    	return new SearchView(myPolls);
    }
}
