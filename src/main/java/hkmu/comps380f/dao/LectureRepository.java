package hkmu.comps380f.dao;

import hkmu.comps380f.model.Attachment;
import hkmu.comps380f.model.Lecture;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface LectureRepository {

    public long createLecture(String userName, String subject, String body,
            List<MultipartFile> attachments) throws IOException;

    public List<Lecture> getLectures();

    public List<Lecture> getLecture(long id);

    public void updateLecture(long lecture_id, String subject, String body,
            List<MultipartFile> attachments) throws IOException;

    public void deleteLecture(long id);

    public void deleteAttachment(long lectureId, String name);

    public Attachment getAttachment(long lectureId, String name);
}
