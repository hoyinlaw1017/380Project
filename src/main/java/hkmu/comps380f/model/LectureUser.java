package hkmu.comps380f.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LectureUser implements Serializable {

    private String username;
    private String fullname;
    private String phonenum;
    private String address;
    private String password;
    private List<String> roles = new ArrayList<>();

    public LectureUser() {
    }

    public LectureUser(String username, String fullname, String phonenum, String address, String password, String[] roles) {
        this.username = username;
        this.fullname = fullname;
        this.phonenum = phonenum;
        this.address = address;
        this.password = "{noop}" + password;
        this.roles = new ArrayList<>(Arrays.asList(roles));
    }

    // getters and setters of all properties
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
