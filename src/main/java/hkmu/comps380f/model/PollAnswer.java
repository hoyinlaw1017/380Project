package hkmu.comps380f.model;

import java.io.Serializable;

public class PollAnswer implements Serializable {
    
    private long id;
    private String username;
    private String answer;

    public PollAnswer(){
    }
    
    public PollAnswer(long id, String username, String answer){
        this.id = id;
        this.username = username;
        this.answer = answer;
    }
    
    //Getters and Setters

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    

}