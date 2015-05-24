package myvote.Models;

import java.awt.List;
import java.util.Date;
import java.util.ArrayList;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import myvote.Controllers.DateParser;

import org.springframework.http.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.simple.*;

import com.fasterxml.jackson.annotation.JsonView;

public class Moderator {
	private static int new_id = 11110;
	
	@JsonView(Integer.class)
	private int id;
	
	@JsonView(String.class)
    private String name;
    
	//@NotEmpty
	//@Valid
    @JsonView(String.class)
    private String email;
    
	//@NotEmpty
	//@Valid
    @JsonView(String.class)
    private String password;
	
    private String created_at;
    
    @JsonView(ArrayList.class)
    private ArrayList<Polls> pollList = new ArrayList<Polls>();
    
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

	public void addPoll(Polls poll){
		pollList.add(poll);
	}
	
	public void deletePoll(Polls poll){
		pollList.remove(poll);
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
	
	public ArrayList<Polls> getAllPolls(){
		return pollList;
	}
	
	public JSONObject getModerator(){
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("id", this.id);
		jsonObj.put("name", this.name);
		jsonObj.put("email", this.email);
		jsonObj.put("password", this.password);
		jsonObj.put("created_at", this.created_at);
		
		return jsonObj;
		//return jsonObj;
	}
}
