package hkmu.comps380f.controller;

import java.io.IOException;
import java.security.Principal;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import hkmu.comps380f.dao.CommentRepository;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentRepository commentRepo;

    @GetMapping("/list")
    public String commentlist(ModelMap model, Principal principal) {
        model.addAttribute("commentDatabase", commentRepo.getCommentsByUsername(principal.getName()));
        return "listComment";
    }

    public static class CommentForm {

        private String body;

        // Getters and Setters
        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("addComment", "commentForm", new CommentForm());
    }

    @PostMapping("/create")
    public String create(CommentForm form, Principal principal) throws IOException {
        long commentId = commentRepo.createComment(principal.getName(),
                form.getBody());
        return "redirect:/lecture/list";
    }

    @GetMapping("/delete/{commentId}")
    public String deleteLectureComment(@PathVariable("commentId") long commentId) {
        commentRepo.deleteComment(commentId);
        return "redirect:/lecture/list";
    }
}
