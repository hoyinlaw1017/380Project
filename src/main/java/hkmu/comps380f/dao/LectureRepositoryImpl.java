package hkmu.comps380f.dao;

import hkmu.comps380f.model.Attachment;
import hkmu.comps380f.model.Lecture;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

    private final JdbcOperations jdbcOp;

    @Autowired
    public LectureRepositoryImpl(DataSource dataSource) {
        jdbcOp = new JdbcTemplate(dataSource);
    }

    private static final class LectureExtractor implements ResultSetExtractor<List<Lecture>> {

        @Override
        public List<Lecture> extractData(ResultSet rs)
        throws SQLException, DataAccessException {
            Map<Long, Lecture> map = new HashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                Lecture lecture = map.get(id);
                if (lecture == null) {
                    lecture = new Lecture();
                    lecture.setId(id);
                    lecture.setuserName(rs.getString("name"));
                    lecture.setSubject(rs.getString("subject"));
                    lecture.setBody(rs.getString("body"));
                    map.put(id, lecture);
                }
                String filename = rs.getString("filename");
                if (filename != null) {
                    Attachment attachment = new Attachment();
                    attachment.setName(rs.getString("filename"));
                    attachment.setMimeContentType(rs.getString("content_type"));
                    attachment.setLectureId(id);
                    lecture.addAttachment(attachment);
                }
            }
            return new ArrayList<>(map.values());
        }
    }

    @Override
    @Transactional
    public long createLecture(final String userName, final String subject,
            final String body, List<MultipartFile> attachments)
            throws IOException {
        final String SQL_INSERT_TICKET
                = "insert into lecture (name, subject, body) values (?, ?, ?)";
        final String SQL_INSERT_ATTACHMENT
                = "insert into attachment (filename, content, content_type,"
                + " lecture_id) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOp.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT_TICKET,
                new String[]{"id"});
                ps.setString(1, userName);
                ps.setString(2, subject);
                ps.setString(3, body);
                return ps;
            }
        }, keyHolder);

        Long lecture_id = keyHolder.getKey().longValue();
        System.out.println("Lecture " + lecture_id + " inserted");

        for (MultipartFile filePart : attachments) {
            if (filePart.getOriginalFilename() != null && filePart.getSize() > 0) {
                LobHandler handler = new DefaultLobHandler();
                jdbcOp.update(SQL_INSERT_ATTACHMENT,
                        new Object[]{filePart.getOriginalFilename(),
                            new SqlLobValue(filePart.getInputStream(),
                                    (int) filePart.getSize(), handler),
                            filePart.getContentType(),
                            lecture_id},
                        new int[]{Types.VARCHAR, Types.BLOB, Types.VARCHAR, Types.INTEGER});
                System.out.println("Attachment " + filePart.getOriginalFilename()
                        + " of Lecture " + lecture_id + " inserted");
            }
        }
        return lecture_id;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lecture> getLectures() {
        final String SQL_SELECT_TICKETS = "select t.*, a.filename, a.content_type,"
                + " a.content from lecture as t left join attachment as a"
                + " on t.id = a.lecture_id";
        return jdbcOp.query(SQL_SELECT_TICKETS, new LectureExtractor());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lecture> getLecture(long id) {
        final String SQL_SELECT_TICKET = "select t.*, a.filename, a.content_type,"
                + " a.content from lecture as t left join attachment as a"
                + " on t.id = a.lecture_id where t.id = ?";
        return jdbcOp.query(SQL_SELECT_TICKET, new LectureExtractor(), id);
    }

    @Override
    @Transactional
    public void updateLecture(long lecture_id, String subject, String body,
            List<MultipartFile> attachments) throws IOException {
        final String SQL_UPDATE_TICKET = "update lecture set subject=?, body=? where id=?";
        final String SQL_INSERT_ATTACHMENT
                = "insert into attachment (filename,content,content_type,lecture_id) values (?,?,?,?)";

        jdbcOp.update(SQL_UPDATE_TICKET, subject, body, lecture_id);
        System.out.println("Lecture " + lecture_id + " updated");

        for (MultipartFile filePart : attachments) {
            if (filePart.getOriginalFilename() != null && filePart.getSize() > 0) {
                LobHandler handler = new DefaultLobHandler();
                jdbcOp.update(SQL_INSERT_ATTACHMENT,
                        new Object[]{filePart.getOriginalFilename(),
                            new SqlLobValue(filePart.getInputStream(),
                                    (int) filePart.getSize(), handler),
                            filePart.getContentType(),
                            lecture_id},
                        new int[]{Types.VARCHAR, Types.BLOB, Types.VARCHAR, Types.INTEGER});
                System.out.println("Attachment " + filePart.getOriginalFilename()
                        + " of Lecture " + lecture_id + " inserted");
            }
        }
    }

    @Override
    @Transactional
    public void deleteLecture(long id) {
        final String SQL_DELETE_TICKET = "delete from lecture where id=?";
        final String SQL_DELETE_ATTACHMENTS = "delete from attachment where lecture_id=?";
        jdbcOp.update(SQL_DELETE_ATTACHMENTS, id);
        jdbcOp.update(SQL_DELETE_TICKET, id);
        System.out.println("Lecture " + id + " deleted");
    }

    @Override
    @Transactional
    public void deleteAttachment(long lectureId, String name) {
        final String SQL_DELETE_ATTACHMENT
                = "delete from attachment where lecture_id=? and filename=?";
        jdbcOp.update(SQL_DELETE_ATTACHMENT, lectureId, name);
        System.out.println("Attachment " + name + " of Lecture " + lectureId + " deleted");
    }

    private static final class AttachmentRowMapper implements RowMapper<Attachment> {

        @Override
        public Attachment mapRow(ResultSet rs, int i) throws SQLException {
            Attachment entry = new Attachment();
            entry.setName(rs.getString("filename"));
            entry.setMimeContentType(rs.getString("content_type"));
            Blob blob = rs.getBlob("content");
            byte[] bytes = blob.getBytes(1l, (int) blob.length());
            entry.setContents(bytes);
            entry.setLectureId(rs.getInt("lecture_id"));
            return entry;
        }
    }

    @Override
    @Transactional
    public Attachment getAttachment(long lectureId, String name) {
        final String SQL_SELECT_ATTACHMENT = "select * from attachment where lecture_id=? and filename=?";
        return jdbcOp.queryForObject(SQL_SELECT_ATTACHMENT, new AttachmentRowMapper(), lectureId, name);
    }

}
