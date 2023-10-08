package hkmu.comps380f.model;

import java.io.Serializable;
import java.security.Principal;

public class Comment implements Serializable {
    
    private long id;
    private String username;
    private String body;
    
    //Getter and Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
  
}


