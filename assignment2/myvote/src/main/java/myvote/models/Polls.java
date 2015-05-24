package myvote.models;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonView;

public class Polls {
private static long new_id = 9223372036L;
	
	@Id
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
	
	@JsonView(Boolean.class)
	private boolean message_sent;
	
	@JsonView(Integer.class)
	private int moderatorId;
	
	public static String getNewId(Long thisNewId){
		return Long.toString(thisNewId, 36);
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
	
	public ArrayList<Integer> getResults(){
		return results;
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
	
	
	public void setMessageSent(boolean yesNo){
		this.message_sent = yesNo;
	}
	
	public Boolean getMessageSent(){
		return this.message_sent;
	}

	public int getModeratorId() {
		return moderatorId;
	}

	public void setModeratorId(int moderatorId) {
		this.moderatorId = moderatorId;
	}
	
}
