package myvote.dao;

import java.util.List;

import myvote.models.Polls;

public interface PollsDAO {
	
	public void create(Polls poll, int moderator_id);
    
    public Polls readById(String id);
     
    public void update(Polls poll);
     
    public void removePoll(String poll_id, int moderator_id);

    public List<Polls> findExpiredPolls();
}
