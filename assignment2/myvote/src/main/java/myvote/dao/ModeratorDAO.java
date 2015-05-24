package myvote.dao;

import myvote.models.Moderator;

public interface ModeratorDAO {
	public void create(Moderator moderator);
    
    public Moderator readById(int id);
     
    public void update(Moderator moderator, int id);
     
    public void addPoll(String poll_id, int moderator_id);
    public void removePoll(String poll_id, int moderator_id);
    //public int deleteById(String id);
}
