package hkmu.comps380f.dao;

import hkmu.comps380f.model.Comment;
import java.io.IOException;
import java.util.List;

public interface CommentRepository {
      
    public long createComment(String username, String body) throws IOException;

    public List<Comment> getComments();

    public List<Comment> getComment(long id);
    
    public List<Comment> getCommentsByUsername(String username);

    public void deleteComment(long id);
    
}
