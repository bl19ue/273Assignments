package myvote.controllers;


import org.json.simple.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.*;

import javax.validation.Valid;

import myvote.models.Moderator;
import myvote.models.Polls;
import myvote.dao.*;
import myvote.exceptions.MyExceptions.*;

//@ImportResource({ "classpath:web.xml" })
/**
 * @author root
 *
 */
@RequestMapping(value="/api/v1")
@RestController
public class ModeratorController {
	private JSONObj jsonObj = new JSONObj();
	private ModeratorDAO moderatorDAO = new ModeratorDAOImpl();
	private PollsDAO pollsDAO = new PollsDAOImpl();
	Map<Integer, Moderator> moderatorList = new HashMap<Integer, Moderator>();
	Map<String, Polls> 							 pollList = new HashMap<String, Polls>();
	
	
	//Getting one moderator
    @RequestMapping(value="/moderators/{id}", method=RequestMethod.GET)
    @ResponseBody
    public JSONObject getModerator(@PathVariable("id") int id){
    	Moderator moderator = moderatorDAO.readById(id);
    	return jsonObj.getModeratorJSON(moderator);
    	//return new ResponseEntity<JSONObject>(moderatorList.get(id).getModerator(), HttpStatus.OK);
    }
    
    //Inserting one moderator
    @RequestMapping(value="/moderators", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JSONObject> postModerator(@Valid @RequestBody Moderator moderator, @RequestHeader(value="Authorization", required=false) String myHeader, BindingResult error){

    	checkValidModerator(moderator);
    	
    	System.out.println("Creating moderator");
    	Moderator md = moderator;
    	//md.setID(Moderator.getNewID());
    	md.setCreatedAt();
    	
    	moderatorDAO.create(moderator);
    	
    	return new ResponseEntity<JSONObject>(jsonObj.getModeratorJSON(md), HttpStatus.CREATED);
	
    }
    
    //Updating one moderator
    @RequestMapping(value="/moderators/{id}", method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<JSONObject> putModerator(@RequestBody Moderator moderator, @PathVariable("id") int id){
    
    	moderatorDAO.update(moderator, id);
    	Moderator mod = moderatorDAO.readById(id);
        return new ResponseEntity<JSONObject>(jsonObj.getModeratorJSON(mod), HttpStatus.OK);
    }
    
    //==================================== For POLLS ======================================
    
    //Getting one poll
    @RequestMapping(value="/polls/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JSONObject> getPoll(@PathVariable("id") String id){
    	Polls poll = pollsDAO.readById(id);
    	return new ResponseEntity<JSONObject>(jsonObj.getPollJSON(poll, true), HttpStatus.OK);
    }
    
    //Getting all moderator's poll with results
    @RequestMapping(value="moderators/{moderator_id}/polls", method=RequestMethod.GET)
    @ResponseBody
    public JSONArray getAllPolls(@PathVariable("moderator_id") int moderator_id){
    	Moderator moderator = moderatorDAO.readById(moderator_id);
    	JSONArray jsonArray = new JSONArray();
    	
    	for(String poll_id : moderator.getAllPolls()){
    		jsonArray.add(jsonObj.getPollJSON(pollsDAO.readById(poll_id), false));
    	}
    	
    	return jsonArray;
    }
    
    //Getting a particular poll under a moderator
    @RequestMapping(value="moderators/{moderator_id}/polls/{poll_id}", method=RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<JSONObject> getPollUnderModerator(@PathVariable("moderator_id") int moderator_id, @PathVariable("poll_id") String poll_id){
	    	JSONObject jsonObject = null;
    	
	    	Moderator moderator = moderatorDAO.readById(moderator_id);
	    	if(moderator.getAllPolls().contains(poll_id)){
	    		jsonObject = jsonObj.getPollJSON(pollsDAO.readById(poll_id), false); 
	    	}
	    	
	    	return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
	    	
    }
	    
    
    
    //Inserting a poll under a moderator
    @RequestMapping(value="/moderators/{moderator_id}/polls", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JSONObject> postPoll(@RequestBody Polls poll, @PathVariable("moderator_id") int moderator_id){

    	checkValidPoll(poll);
    	System.out.println("Poll creating.");
    	Polls pl = poll;
    	int size = pl.getChoices().size();
    	
    	ArrayList<Integer> results = new ArrayList<Integer>();
    	for(int i=0;i<size;i++){
    		results.add(0);
    	}
    	
    	pl.setResults(results);
    	pl.setMessageSent(false);
    	
    	pollsDAO.create(pl, moderator_id);
    	
        return new ResponseEntity<JSONObject>(jsonObj.getPollJSON(pl, true), HttpStatus.CREATED);
    }
    
    //Deleting a poll
    @RequestMapping(value="/moderators/{moderator_id}/polls/{poll_id}", method=RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deletePoll(@PathVariable("moderator_id") int moderator_id, @PathVariable("poll_id") String poll_id){

    	Moderator moderator = moderatorDAO.readById(moderator_id);
    	if(moderator.getAllPolls().contains(poll_id)){
    		pollsDAO.removePoll(poll_id, moderator_id);
    	}
    	
    	return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
    
    //Voting for a poll
    @RequestMapping(value="/polls/{poll_id}", method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> putPoll(@PathVariable("poll_id") String id, @RequestParam("choice") String choice_val){
    	Polls poll = pollsDAO.readById(id);
    	poll.vote(Integer.parseInt(choice_val));
    	
    	pollsDAO.update(poll);
    	
    	return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
    public void checkValidModerator(Moderator moderator){
    	if(moderator.getEmail() == null || moderator.getPassword() == null || moderator.getName() == null){
    		throw new MyInvalidParameterException();
    	}
    }
    
    public void checkValidPoll(Polls poll){
    	
    	if(poll.getQuestion() == null || poll.getChoices() == null || poll.getStartedAt() == null || poll.getExpiredAt() == null){
    		throw new MyInvalidPollException();
    	}
    	
    }
}

