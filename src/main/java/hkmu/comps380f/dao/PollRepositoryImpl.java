package hkmu.comps380f.dao;

import hkmu.comps380f.model.Poll;
import hkmu.comps380f.model.PollAnswer;
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
import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class PollRepositoryImpl implements PollRepository {

    private final JdbcOperations jdbcOp;

    @Autowired
    public PollRepositoryImpl(DataSource dataSource) {
        jdbcOp = new JdbcTemplate(dataSource);
    }

    private static final class PollExtractor implements ResultSetExtractor<List<Poll>> {

        @Override
        public List<Poll> extractData(ResultSet rs)
                throws SQLException, DataAccessException {
            Map<Long, Poll> map = new HashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                Poll poll = map.get(id);
                if (poll == null) {
                    poll = new Poll();
                    poll.setId(id);
                    poll.setBody(rs.getString("body"));
                    poll.setOpt1(rs.getString("opt1"));
                    poll.setOpt2(rs.getString("opt2"));
                    poll.setOpt3(rs.getString("opt3"));
                    poll.setOpt4(rs.getString("opt4"));
                    map.put(id, poll);
                }
            }
            return new ArrayList<>(map.values());
        }
    }

    @Override
    @Transactional
    public long createPoll(final String body, final String opt1, final String opt2,
            final String opt3, final String opt4)
            throws IOException {
        final String SQL_INSERT_POLL
                = "insert into pollquestion (body, opt1, opt2, opt3, opt4) values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOp.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT_POLL,
                        new String[]{"id"});
                ps.setString(1, body);
                ps.setString(2, opt1);
                ps.setString(3, opt2);
                ps.setString(4, opt3);
                ps.setString(5, opt4);
                return ps;
            }
        }, keyHolder);

        Long poll_id = keyHolder.getKey().longValue();
        System.out.println("Poll " + poll_id + " inserted");

        return poll_id;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Poll> getPolls() {
        final String SQL_SELECT_POLLS = "select * from pollquestion";
        return jdbcOp.query(SQL_SELECT_POLLS, new PollExtractor());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Poll> getPoll(long id) {
        final String SQL_SELECT_TICKET = "select * from pollquestion"
                + " where id = ?";
        return jdbcOp.query(SQL_SELECT_TICKET, new PollExtractor(), id);
    }

    @Override
    @Transactional
    public void deletePoll(long id) {
        final String SQL_DELETE_ANSWER = "delete from pollanswer where id=?";
        final String SQL_DELETE_POLL = "delete from pollquestion where id=?";
        jdbcOp.update(SQL_DELETE_ANSWER, id);
        jdbcOp.update(SQL_DELETE_POLL, id);
        System.out.println("Poll " + id + " deleted");
    }

    private static final class PollAnswerExtractor implements ResultSetExtractor<List<PollAnswer>> {

        @Override
        public List<PollAnswer> extractData(ResultSet rs)
                throws SQLException, DataAccessException {
            Map<Long, PollAnswer> map = new HashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                PollAnswer ans = map.get(id);
                if (ans == null) {
                    ans = new PollAnswer();
                    ans.setId(id);
                    ans.setUsername(rs.getString("username"));
                    ans.setAnswer(rs.getString("answer"));
                    map.put(id, ans);
                }
            }
            return new ArrayList<>(map.values());
        }
    }

    @Override
    @Transactional
    public long createPollAnswer(final long id, final String username, final String answer)
            throws IOException {
        final String SQL_INSERT_ANSWER
                = "insert into pollanswer (id, username, answer) values (?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOp.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ANSWER,
                        new String[]{"id"});
                ps.setLong(1, id);
                ps.setString(2, username);
                ps.setString(3, answer);
                return ps;
            }
        }, keyHolder);

        Long question_id = keyHolder.getKey().longValue();
        System.out.println("question " + question_id + " answered");

        return question_id;
    }

    @Override
    @Transactional
    public void save(PollAnswer ans) {
        final String SQL_INSERT_ANSWER
                = "insert into pollanswer (id, username, answer) values (?,?,?)";

        jdbcOp.update(SQL_INSERT_ANSWER, ans.getId(),
                ans.getUsername(), ans.getAnswer());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PollAnswer> getPollAnswer(long id, String username) {
        final String SQL_SELECT_ANSWER
                = "select * from pollanswer"
                + " where pollanswer.id = ?"
                + " and pollanswer.username = ?";
        return jdbcOp.query(SQL_SELECT_ANSWER, new PollAnswerExtractor(), id, username);
    }

    @Override
    @Transactional
    public void deletePollAnswer(long id, String username) {
        final String SQL_DELETE_ANSWER = "delete from pollanswer where id=? and username=?";
        jdbcOp.update(SQL_DELETE_ANSWER, id, username);
    }

    @Override
    @Transactional(readOnly = true)
    public int voteCount(long id, String answer) {
        final String SQL_COUNT_VOTE = "select count(answer) as totalvote from pollanswer where id = ? and answer = ?";
        return jdbcOp.queryForObject(SQL_COUNT_VOTE,new java.lang.Object[]{id,answer},Integer.class);
    }
}
