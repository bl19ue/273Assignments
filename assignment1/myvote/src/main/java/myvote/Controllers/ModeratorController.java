package myvote.Controllers;


import java.util.concurrent.atomic.AtomicLong;

import org.json.simple.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.*;

import java.util.*;

import javax.validation.Valid;

import myvote.Models.Moderator;
import myvote.Models.Polls;
import myvote.exceptions.MyExceptions.*;

//@ImportResource({ "classpath:web.xml" })
@RequestMapping(value="/api/v1")
@RestController
public class ModeratorController {
	Map<Integer, Moderator> moderatorList = new HashMap<Integer, Moderator>();
	Map<String, Polls> 							 pollList = new HashMap<String, Polls>();
	
	
	//Getting one moderator
    @RequestMapping(value="/moderators/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JSONObject> getModerator(@PathVariable("id") int id, @RequestHeader(value="Authorization", required=false) String myHeader){
    	
    	checkAuthorization(myHeader);
        return new ResponseEntity<JSONObject>(moderatorList.get(id).getModerator(), HttpStatus.OK);
    }
    
    //Inserting one moderator
    @RequestMapping(value="/moderators", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JSONObject> postModerator(@Valid @RequestBody Moderator moderator, @RequestHeader(value="Authorization", required=false) String myHeader, BindingResult error){

    	checkValidModerator(moderator);
    	//checkAuthorization(myHeader);
    	
    	System.out.println("Moderator creating.");
    	Moderator md = moderator;
    	md.setID(Moderator.getNewID());
    	md.setCreatedAt();
    	
        moderatorList.put(md.getID(), md);
        return new ResponseEntity<JSONObject>(md.getModerator(), HttpStatus.CREATED);
	
    }
    
    //Updating one moderator
    @RequestMapping(value="/moderators/{id}", method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<JSONObject> putModerator(@RequestBody Moderator moderator, @PathVariable("id") int id, @RequestHeader(value="Authorization", required=false) String myHeader){
    	
    	//checkValidModerator(moderator);
    	checkAuthorization(myHeader);
    	if(moderator.getName() != null)
    		moderatorList.get(id).setName(moderator.getName());
    	if(moderator.getEmail() != null)
    		moderatorList.get(id).setEmail(moderator.getEmail());
    	if(moderator.getPassword() != null)
    		moderatorList.get(id).setPassword(moderator.getPassword());
        
        return new ResponseEntity<JSONObject>(moderatorList.get(id).getModerator(), HttpStatus.OK);
    }
    
    /*==================================== For POLLS ======================================*/
    
    //Getting one poll
    @RequestMapping(value="/polls/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JSONObject> getPoll(@PathVariable("id") String id){
	if(pollList.containsKey(id)){
 		return new ResponseEntity<JSONObject>(pollList.get(id).getPoll(), HttpStatus.OK);
	}
	else{
		return new ResponseEntity<JSONObject>(pollList.get(0).getPoll(), HttpStatus.OK);
	}
    }
    
    //Getting all moderator's poll with results
    @RequestMapping(value="moderators/{moderator_id}/polls", method=RequestMethod.GET)
    @ResponseBody
    public JSONArray getAllPolls(@PathVariable("moderator_id") int moderator_id, @RequestHeader(value="Authorization", required=false) String myHeader){
    	
    	checkAuthorization(myHeader);
    	
    	System.out.println(moderatorList.get(moderator_id));
    	ArrayList<Polls> poll_try = moderatorList.get(moderator_id).getAllPolls();
    	JSONArray jsonArray = new JSONArray();
    	
    	Polls myPoll;
    	
    	for(Polls poll : poll_try){
    		if(pollList.containsKey(poll.getID())){
    			myPoll = pollList.get(poll.getID());
    			jsonArray.add(myPoll.getPollComplete());
    		}
    	}
    	
    	//Returns Array of JSONObjects
    	return jsonArray;
        //return new ResponseEntity<ArrayList<Polls>>(moderatorList.get(moderator_id).getAllPolls(), HttpStatus.OK);
    }
    
    //Getting a particular poll under a moderator
    @RequestMapping(value="moderators/{moderator_id}/polls/{poll_id}", method=RequestMethod.GET)
    @ResponseBody
	public ResponseEntity<JSONObject> getPollUnderModerator(@PathVariable("moderator_id") int moderator_id, @PathVariable("poll_id") String poll_id, @RequestHeader(value="Authorization", required=false) String myHeader){
	    	
	    	checkAuthorization(myHeader);
	    	
	    	System.out.println(moderatorList.get(moderator_id));
	    	ArrayList<Polls> poll_try = moderatorList.get(moderator_id).getAllPolls();
	    	
	    	Polls myPoll;
	    	JSONObject jsonObject = null;
	    	
	    	for(Polls poll : poll_try){
	    		if(poll.getID().equals(poll_id)){
	    			jsonObject = pollList.get(poll_id).getPollComplete();
	    		}
	    	}
	    	
	    	return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.OK);
	        //return new ResponseEntity<ArrayList<Polls>>(moderatorList.get(moderator_id).getAllPolls(), HttpStatus.OK);
	    }
	    
    
    
    //Inserting a poll under a moderator
    @RequestMapping(value="/moderators/{moderator_id}/polls", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JSONObject> postPoll(@RequestBody Polls poll, @PathVariable("moderator_id") int moderator_id, @RequestHeader(value="Authorization", required=false) String myHeader){

    	checkValidPoll(poll);
    	checkAuthorization(myHeader);
    	
    	System.out.println("Poll creating.");
    	Polls pl = poll;
    	pl.setID(Polls.getNewId());
    	int size = pl.getChoices().size();
    	
    	ArrayList<Integer> results = new ArrayList<Integer>();
    	for(int i=0;i<size;i++){
    		results.add(0);
    	}
    	
    	pl.setResults(results);
    	
    	pollList.put(pl.getID(), pl);
    	moderatorList.get(moderator_id).addPoll(pl);
    	
        return new ResponseEntity<JSONObject>(pl.getPoll(), HttpStatus.CREATED);
    }
    
    //Deleting a poll
    @RequestMapping(value="/moderators/{moderator_id}/polls/{poll_id}", method=RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deletePoll(@PathVariable("moderator_id") int moderator_id, @PathVariable("poll_id") String poll_id, @RequestHeader(value="Authorization", required=false) String myHeader){

    	checkAuthorization(myHeader);
    	
    	if(pollList.containsKey(poll_id) && moderatorList.containsKey(moderator_id)){
	    	System.out.println("Poll deleting.");
	    	
	    	Polls poll = pollList.get(poll_id);
	    	
	    	moderatorList.get(moderator_id).deletePoll(poll);
	    	pollList.remove(poll_id);
    	}
    	
    	return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
    
    //Voting for a poll
    @RequestMapping(value="/polls/{poll_id}", method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> putPoll(@PathVariable("poll_id") String id, @RequestParam("choice") String choice_val){
    	
    	pollList.get(id).vote(Integer.parseInt(choice_val));
    	
    	return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
    
    public void checkAuthorization(String myHeader){
    	if(myHeader == null || (!myHeader.equals("Basic Zm9vOmJhcg=="))){
    		throw new MyAuthenticationException();
    	}
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
