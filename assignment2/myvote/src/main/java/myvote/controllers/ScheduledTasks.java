package myvote.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import myvote.dao.ModeratorDAO;
import myvote.dao.ModeratorDAOImpl;
import myvote.dao.PollsDAO;
import myvote.dao.PollsDAOImpl;
import myvote.kafka.PollKafkaConsumer;
import myvote.kafka.PollKafkaProducer;
import myvote.models.Polls;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
	private PollsDAO pollsDAO = new PollsDAOImpl();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private PollKafkaProducer pollKafkaProducer = new PollKafkaProducer();
    private PollKafkaConsumer pollKafkaConsumer;
    private ModeratorDAO moderatorDAO = new ModeratorDAOImpl();
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {
//        System.out.println("The time is now " + dateFormat.format(new Date()));
//    }
    
    @Scheduled(fixedRate = 100000)
    public void reportExpiredPolls(){
    	String pollResult;
    	List<Polls> expiredPoll = pollsDAO.findExpiredPolls();
    	if(!expiredPoll.isEmpty()){
    		for(Polls poll : expiredPoll){
    			//E.g: moderator@gmail.com:001234567:Poll Result [Android=100,iPhone=200]
    			pollResult = "[";
    			String email = moderatorDAO.readById(poll.getModeratorId()).getEmail();
    			
    			for(int i=0;i<poll.getResults().size();i++){
    				
    				pollResult += poll.getChoices().get(i);
    				
    				pollResult += "=";
    				
    				pollResult += poll.getResultForChoice(i);
    				
    				if(i+1 != poll.getResults().size()){
    					pollResult += ",";
    				}
    			
    			}
    			
    			pollResult += "]";
        		System.out.println(email +":"+ "010008609" +":"+ "Poll Result " + pollResult);
        		pollResult = email +":"+ "010008609" +":"+ "Poll Result " + pollResult;
            	pollKafkaProducer.produceKafkaStream(pollResult);
        		pollResult = "";
        	}
//        	pollKafkaConsumer = new PollKafkaConsumer();
//        	pollKafkaConsumer.start();
//        	
        	
    	}
    	
    }
}