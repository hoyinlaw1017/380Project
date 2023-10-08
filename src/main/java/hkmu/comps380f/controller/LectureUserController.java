package hkmu.comps380f.controller;

import hkmu.comps380f.dao.LectureUserRepository;
import hkmu.comps380f.model.LectureUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import javax.swing.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/user")
public class LectureUserController {

    @Resource
    private LectureUserRepository lectureUserRepo;

    @GetMapping({"", "/list"})
    public String list(ModelMap model) {
        model.addAttribute("lectureUsers", lectureUserRepo.findAll());
        return "listUser";
    }

    public static class Form {

        private String username;
        private String fullname;
        private String phonenum;
        private String address;
        private String password;
        private String[] roles;

        // ... getters and setters for each of the properties
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

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }
    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("addUser", "lectureUser", new Form());
    }

    @PostMapping("/create")
    public View create(Form form) throws IOException {
        LectureUser user = new LectureUser(form.getUsername(), form.getFullname(), form.getPhonenum(), form.getAddress(),
                form.getPassword(), form.getRoles()
        );
        lectureUserRepo.save(user);
        return new RedirectView("/", true);
    }
    
    //convert obj to a list
    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
}
    
    @GetMapping("/updateUser/{username}")
    public ModelAndView update(@PathVariable("username") String username) {
        LectureUser user =lectureUserRepo.findById(username).get(0);
        
        //LectureUser to list
        List<String> userList = Arrays.asList(user.getUsername(),user.getFullname(),user.getPhonenum(),user.getAddress(),user.getPassword().substring(6));
        
        ModelAndView model = new ModelAndView("updateUser", "lectureUser", new Form());
        model.addObject("userList",userList);
        return model;
    }

    @PostMapping("/updateUser/{username}")
    public View update(@PathVariable("username") String username,Form form) throws IOException {
        //find if db have same name
        if(lectureUserRepo.findById(form.getUsername()).isEmpty()||(username.equals(form.getUsername()))){
            lectureUserRepo.delete(username);
            LectureUser user = new LectureUser(form.getUsername(), form.getFullname(), form.getPhonenum(), form.getAddress(),
                    form.getPassword(), form.getRoles()
            );
            lectureUserRepo.save(user);
        }else{
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "The user name exist already.");
        }
        return new RedirectView("/", true);
    }
    

    @GetMapping("/delete/{username}")
    public View deleteLecture(@PathVariable("username") String username) {
        lectureUserRepo.delete(username);
        return new RedirectView("/user/list", true);
    }
}
