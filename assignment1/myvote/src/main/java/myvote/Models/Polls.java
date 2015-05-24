package myvote.Models;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonView;
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;

public class Polls {
	private static long new_id = 9223372036L;
	
	@JsonView(String.class)
	private String id;
	
	@JsonView(String.class)
	private String question;
	
	@JsonView(String.class)
	private String started_at;
	
	@JsonView(String.class)
	private String expired_at;
	
	@JsonView(ArrayList.class)
	private ArrayList<String> choice;
	
	@JsonView(ArrayList.class)
	private ArrayList<Integer> results;
	
	public static String getNewId(){
		return Long.toString(++new_id, 36);
	}
	
	public String getID(){
		return id;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public String getStartedAt(){
		return started_at;
	}
	
	public String getExpiredAt(){
		return expired_at;
	}
	
	public ArrayList<String> getChoices(){
		return choice;
	}
	
	public int getResultForChoice(int index){
		return results.get(index);
	}
	
	public void setID(String id){
		this.id = id.toUpperCase();
	}
	
	public void setQuestion(String question){
		this.question = question;
	}
	
	public void setCreatedAt(String started_at){
		//TODO
		this.started_at = started_at;
	}
	
	public void setExpiredAt(String expired_at){
		//TODO
		this.expired_at = expired_at;
	}
	
	public void setChoices(ArrayList<String> choice){
		this.choice = choice;
	}
	
	public void setResults(ArrayList<Integer> results){
		this.results = results;
	}
	
	public void vote(int index){
		int val = results.get(index);
		results.set(index, ++val);
	}
	
	public JSONObject getPoll(){
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("id", this.id);
		jsonObj.put("question", this.question);
		jsonObj.put("started_at", this.started_at);
		jsonObj.put("expired_at", this.expired_at);
		jsonObj.put("choice", this.choice);
		
		return jsonObj;
		//return jsonObj;
	}
	
	public JSONObject getPollComplete(){
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("id", this.id);
		jsonObj.put("question", this.question);
		jsonObj.put("started_at", this.started_at);
		jsonObj.put("expired_at", this.started_at);
		jsonObj.put("choice", this.choice);
		jsonObj.put("results", this.results);
		
		return jsonObj;
		//return jsonObj;
	}
}
