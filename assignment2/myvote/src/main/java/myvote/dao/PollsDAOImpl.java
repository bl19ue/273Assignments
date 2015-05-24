package myvote.dao;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;

import myvote.controllers.DateParser;
import myvote.models.Moderator;
import myvote.models.Polls;


public class PollsDAOImpl implements PollsDAO{
	private static ModeratorDAO moderatorDAO;
	private static MongoOperations mongoOps;
    private static final String POLLS_COLLECTION = "polls";
    private static DateParser dateParser;
    
    public PollsDAOImpl(){
    	if(mongoOps == null){
	    	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringMongoBean.class);
	    	mongoOps = (MongoOperations)context.getBean("mongoTemplate");
	    	moderatorDAO = new ModeratorDAOImpl();
	    	dateParser = new DateParser();
    	}
    }
	
	@Override
	public void create(Polls poll, int moderator_id) {
		Query query = new Query().limit(1);
		query.with(new Sort(Sort.Direction.DESC, "id"));
		
		Polls newPoll = mongoOps.findOne(query, Polls.class, POLLS_COLLECTION);
		if(newPoll == null){
			poll.setID(Polls.getNewId(9223372036L));
		}
		else{
			Long newpollId = Long.valueOf(newPoll.getID(), 36) + 1;
			System.out.println("new poll id = " + newpollId);
			poll.setID(Long.toString(newpollId, 36));
		}
		
		poll.setModeratorId(moderator_id);
		
		mongoOps.insert(poll, POLLS_COLLECTION);
		moderatorDAO.addPoll(poll.getID(), moderator_id);
		
	}

	@Override
	public Polls readById(String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		Polls poll = mongoOps.findOne(query, Polls.class, POLLS_COLLECTION);
		return poll;
	}

	@Override
	public void update(Polls poll) {
		Query query = new Query(Criteria.where("_id").is(poll.getID()));
		Update update = new Update();
		
		update.set("results", poll.getResults());
		
		mongoOps.findAndModify(query, update, Polls.class);

	}

	@Override
	public void removePoll(String poll_id, int moderator_id) {
		Query query = new Query(Criteria.where("_id").is(poll_id));
		mongoOps.remove(query, Polls.class);
		moderatorDAO.removePoll(poll_id, moderator_id);
	}

	@Override
	public List<Polls> findExpiredPolls() {
		Query query = new Query(Criteria.where("message_sent").is(false));
		Update update = new Update();
		
		List<Polls> expiredPolls = new ArrayList<Polls>();
		List<Polls> polls = mongoOps.find(query, Polls.class, POLLS_COLLECTION);
		try{
			for(Polls poll : polls){
				if(dateParser.getDateTime(poll.getExpiredAt()).before(new Date()))
				{
					expiredPolls.add(poll);
					update.set("message_sent", true);
					mongoOps.findAndModify(query, update, Polls.class);
					
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return expiredPolls;
	}

}
