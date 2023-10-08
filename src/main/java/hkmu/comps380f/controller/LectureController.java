package hkmu.comps380f.controller;

import hkmu.comps380f.dao.LectureRepository;
import hkmu.comps380f.model.Attachment;
import hkmu.comps380f.model.Lecture;
import hkmu.comps380f.view.DownloadingView;
import hkmu.comps380f.controller.CommentController;
import hkmu.comps380f.dao.CommentRepository;
import hkmu.comps380f.dao.PollRepository;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/lecture")
public class LectureController {

    @Resource
    private LectureRepository lectureRepo;
    
    @Resource
    private CommentRepository commentRepo;
    
    @Resource
    private PollRepository pollRepo;

    @GetMapping({"", "/list"})
    public String list(ModelMap model) {
        model.addAttribute("lectureDatabase", lectureRepo.getLectures());
        model.addAttribute("pollDatabase", pollRepo.getPolls());
        return "list";
    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("add", "lectureForm", new Form());
    }

    public static class Form {

        private String subject;
        private String body;
        private List<MultipartFile> attachments;

        // Getters and Setters of subject, body, attachments
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

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }
    }

    @PostMapping("/create")
    public String create(Form form, Principal principal) throws IOException {
        long lectureId = lectureRepo.createLecture(principal.getName(),
                form.getSubject(), form.getBody(), form.getAttachments());
        return "redirect:/lecture/view/" + lectureId;
    }

    @GetMapping("/view/{lectureId}")
    public String view(@PathVariable("lectureId") long lectureId, ModelMap model) {
        List<Lecture> lectures = lectureRepo.getLecture(lectureId);
        if (lectures.isEmpty()) {
            return "redirect:/lecture/list";
        }
        model.addAttribute("lectureId", lectureId);
        model.addAttribute("lecture", lectures.get(0));
        model.addAttribute("commentDatabase", commentRepo.getComments());
        return "view";
    }

    @GetMapping("/{lectureId}/attachment/{attachment:.+}")
    public View download(@PathVariable("lectureId") long lectureId,
            @PathVariable("attachment") String name) {
        Attachment attachment = lectureRepo.getAttachment(lectureId, name);
        if (attachment != null) {
            return new DownloadingView(attachment.getName(),
                    attachment.getMimeContentType(), attachment.getContents());
        }
        return new RedirectView("/lecture/list", true);
    }

    @GetMapping("/{lectureId}/delete/{attachment:.+}")
    public String deleteAttachment(@PathVariable("lectureId") long lectureId,
            @PathVariable("attachment") String name) {
        lectureRepo.deleteAttachment(lectureId, name);
        return "redirect:/lecture/edit/" + lectureId;
    }

    @GetMapping("/delete/{lectureId}")
    public String deleteLecture(@PathVariable("lectureId") long lectureId) {
        lectureRepo.deleteLecture(lectureId);
        return "redirect:/lecture/list";
    }

    @GetMapping("/edit/{lectureId}")
    public ModelAndView showEdit(@PathVariable("lectureId") long lectureId,
            Principal principal, HttpServletRequest request) {
        List<Lecture> lectures = lectureRepo.getLecture(lectureId);
        if (lectures.isEmpty()
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(lectures.get(0).getuserName()))) {
            return new ModelAndView(new RedirectView("/lecture/list", true));
        }
        Lecture lecture = lectures.get(0);
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("lectureId", lectureId);
        modelAndView.addObject("lecture", lecture);
        Form lectureForm = new Form();
        lectureForm.setSubject(lecture.getSubject());
        lectureForm.setBody(lecture.getBody());
        modelAndView.addObject("lectureForm", lectureForm);
        return modelAndView;
    }

    @PostMapping("/edit/{lectureId}")
    public String edit(@PathVariable("lectureId") long lectureId, Form form,
            Principal principal, HttpServletRequest request) throws IOException {
        List<Lecture> lectures = lectureRepo.getLecture(lectureId);
        if (lectures.isEmpty()
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(lectures.get(0).getuserName()))) {
            return "redirect:/lecture/list";
        }
        lectureRepo.updateLecture(lectureId, form.getSubject(),
                form.getBody(), form.getAttachments());
        return "redirect:/lecture/view/" + lectureId;
    }
}
