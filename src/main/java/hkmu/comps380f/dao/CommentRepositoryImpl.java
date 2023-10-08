package hkmu.comps380f.dao;

import hkmu.comps380f.model.Comment;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcOperations jdbcOp;

    @Autowired
    public CommentRepositoryImpl(DataSource dataSource) {
        jdbcOp = new JdbcTemplate(dataSource);
    }

    private static final class CommentExtractor implements ResultSetExtractor<List<Comment>> {

        @Override
        public List<Comment> extractData(ResultSet rs)
                throws SQLException, DataAccessException {
            Map<Long, Comment> map = new HashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                Comment Comment = map.get(id);
                if (Comment == null) {
                    Comment = new Comment();
                    Comment.setId(id);
                    Comment.setUsername(rs.getString("username"));
                    Comment.setBody(rs.getString("body"));
                    map.put(id, Comment);
                }
            }
            return new ArrayList<>(map.values());
        }
    }

    @Override
    @Transactional
    public long createComment(final String username, final String body)
            throws IOException {
        final String SQL_INSERT_COMMENT
                = "insert into comment (username, body) values (?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOp.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT_COMMENT,
                        new String[]{"id"});
                ps.setString(1, username);
                ps.setString(2, body);
                return ps;
            }
        }, keyHolder);

        Long comment_id = keyHolder.getKey().longValue();
        System.out.println("Comment " + comment_id + " inserted");

        return comment_id;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getComments() {
        final String SQL_SELECT_COMMENTS = "select *"
                + " from comment";
        return jdbcOp.query(SQL_SELECT_COMMENTS, new CommentExtractor());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getComment(long id) {
        final String SQL_SELECT_COMMENT = "select *"
                + " from comment"
                + " where comment.id = ?";
        return jdbcOp.query(SQL_SELECT_COMMENT, new CommentExtractor(), id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByUsername(String username) {
        final String SQL_SELECT_COMMENT = "select *"
                + " from comment"
                + " where comment.username = ?";
        return jdbcOp.query(SQL_SELECT_COMMENT, new CommentExtractor(), username);
    }

    @Override
    @Transactional
    public void deleteComment(long id) {
        final String SQL_DELETE_COMMENT = "delete from comment where id=?";
        jdbcOp.update(SQL_DELETE_COMMENT, id);
        System.out.println("Comment " + id + " deleted");
    }

}
