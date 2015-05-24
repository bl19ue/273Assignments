package myvote.models;

import java.util.ArrayList;

import myvote.controllers.DateParser;

import org.springframework.data.annotation.Id;
import org.json.simple.*;

import com.fasterxml.jackson.annotation.JsonView;


public class Moderator {
private static int new_id = 11110;
	
	@Id
	@JsonView(Integer.class)
	private int id;
	
	@JsonView(String.class)
    private String name;
    
    @JsonView(String.class)
    private String email;
    
	@JsonView(String.class)
    private String password;
	
	@JsonView(String.class)
    private String created_at;
    
    @JsonView(ArrayList.class)
    private ArrayList<String> polls = new ArrayList<String>();
    
    public static int getNewID(){
    	return ++new_id;
    }
    public void setID(int id){
    	this.id = id;
    }
	    
	public void setName(String name){
	    this.name = name;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public void setCreatedAt(){
		DateParser dparser = new DateParser();
		created_at = dparser.getDate();
	}
	
	public void setCreatedAt(String created_at){
		this.created_at = created_at;
	}
	
	public void update(int id){}
	/*
	public void addPoll(Polls poll){
		polls.add(poll);
	}
	*/
	public void deletePoll(Polls poll){
		polls.remove(poll);
	}
	
	public int getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getCreatedAt(){
		return created_at;
	}
	
	public ArrayList<String> getAllPolls(){
		return polls;
	}
	
}
