package hkmu.comps380f.dao;

import hkmu.comps380f.model.Poll;
import hkmu.comps380f.model.PollAnswer;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface PollRepository {
    
    public long createPoll(final String body, final String opt1, final String opt2,
				final String opt3, final String opt4) throws IOException;
    
    public List<Poll> getPolls();
    
    public List<Poll> getPoll(long id);
    
    public void deletePoll (long id);
    
    public long createPollAnswer(final long id, final String username, final String answer)
            throws IOException;
    
    public void save(PollAnswer ans);
    
    public List<PollAnswer> getPollAnswer(long id, String username);
    
    public void deletePollAnswer(long id, String username);
    
    public int voteCount(long id, String answer);
}
