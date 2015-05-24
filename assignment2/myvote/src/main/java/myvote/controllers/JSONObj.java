package myvote.controllers;

import myvote.models.Moderator;
import myvote.models.Polls;

import org.json.simple.*;

public class JSONObj {
	
	public JSONObject getModeratorJSON(Moderator moderator){
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("id", moderator.getID());
		jsonObj.put("name", moderator.getName());
		jsonObj.put("email", moderator.getEmail());
		jsonObj.put("password", moderator.getPassword());
		jsonObj.put("created_at", moderator.getCreatedAt());
		
		return jsonObj;
	}
	
	
	
	public JSONObject getPollJSON(Polls poll, boolean removeResults){
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("id", poll.getID());
		jsonObj.put("question", poll.getQuestion());
		jsonObj.put("started_at", poll.getStartedAt());
		jsonObj.put("expired_at", poll.getExpiredAt());
		jsonObj.put("choices", poll.getChoices());
		
		if(!removeResults)
			jsonObj.put("results", poll.getResults());
		
		return jsonObj;
	}
}
