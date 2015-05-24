package myvote.dao;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.MongoClient;

import myvote.controllers.JSONObj;
import myvote.models.Moderator;
import myvote.models.Polls;

public class ModeratorDAOImpl implements ModeratorDAO{
	private static MongoOperations mongoOps;
    private static final String MODERATOR_COLLECTION = "moderator";
    
    public ModeratorDAOImpl(){
    	if(mongoOps == null){
	    	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringMongoBean.class);
	    	mongoOps = (MongoOperations)context.getBean("mongoTemplate");
    	}
    }
    
	@Override
	public void create(Moderator moderator) {
		Query query = new Query().limit(1);
		query.with(new Sort(Sort.Direction.DESC, "id"));
		Moderator newmd = mongoOps.findOne(query, Moderator.class, MODERATOR_COLLECTION);
		if(newmd == null)
			moderator.setID(11111);
		else{
			int newmdId = newmd.getID()+1;
			System.out.println("new mod id = " + newmdId);
			moderator.setID(newmdId);
		}
		mongoOps.insert(moderator, MODERATOR_COLLECTION);
	}

	@Override
	public Moderator readById(int id) {
		Query query = new Query(Criteria.where("_id").is(id));
		Moderator moderator = mongoOps.findOne(query, Moderator.class, MODERATOR_COLLECTION);
		return moderator;
	}

	@Override
	public void update(Moderator moderator, int id) {
		Query query = new Query(Criteria.where("_id").is(id));
		Update update = new Update();
		
		if(moderator.getName() != null)
    		update.set("name", moderator.getName());
    	if(moderator.getEmail() != null)
    		update.set("email", moderator.getEmail());
    	if(moderator.getPassword() != null)
    		update.set("password", moderator.getPassword());
    	
		mongoOps.findAndModify(query, update, Moderator.class);
	}
	
	@Override
	public void addPoll(String poll_id, int moderator_id){
		Query query = new Query(Criteria.where("_id").is(moderator_id));
		Update update = new Update();
		update.push("polls", poll_id);
		mongoOps.findAndModify(query, update, Moderator.class);
	}

	@Override
	public void removePoll(String poll_id, int moderator_id) {
		Query query = new Query(Criteria.where("_id").is(moderator_id));
		Update update = new Update();
		update.pull("polls", poll_id);
		mongoOps.findAndModify(query, update, Moderator.class);
	}

}
