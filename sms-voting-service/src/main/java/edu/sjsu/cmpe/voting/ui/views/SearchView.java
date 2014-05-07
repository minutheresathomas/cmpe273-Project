package edu.sjsu.cmpe.voting.ui.views;

import java.util.List;

import com.yammer.dropwizard.views.View;

import edu.sjsu.cmpe.voting.api.Poll;

public class SearchView extends View{
	private final List<Poll> polls;
	
    public SearchView(List<Poll> polls) {
	super("searchresults.mustache");
    this.polls = polls;
    }

    public List<Poll> getPoll() {
	return polls;
    }
}
