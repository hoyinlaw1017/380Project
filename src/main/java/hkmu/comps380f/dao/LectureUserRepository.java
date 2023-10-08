package hkmu.comps380f.dao;

import hkmu.comps380f.model.LectureUser;
import java.util.List;

public interface LectureUserRepository {

    public void save(LectureUser user);

    public List<LectureUser> findAll();

    public List<LectureUser> findById(String username);

    public void delete(String username);
}
