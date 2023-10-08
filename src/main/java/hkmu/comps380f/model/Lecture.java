package hkmu.comps380f.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Lecture implements Serializable {

    private long id;
    private String userName;
    private String subject;
    private String body;
    private final Map<String, Attachment> attachments = new HashMap<>();

    public void addAttachment(Attachment attachment) {
        this.attachments.put(attachment.getName(), attachment);
    }

    public Collection<Attachment> getAttachments() {
        return this.attachments.values();
    }

    public int getNumberOfAttachments() {
        return this.attachments.size();
    }

    // getters and setters of all properties except attachments
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
