package hkmu.comps380f.controller;

import hkmu.comps380f.dao.CommentRepository;
import hkmu.comps380f.dao.PollRepository;
import hkmu.comps380f.model.Poll;
import hkmu.comps380f.model.PollAnswer;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import javax.annotation.Resource;
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
@RequestMapping("/poll")
public class PollController {

    @Resource
    private PollRepository pollRepo;

    @Resource
    private CommentRepository commentRepo;

    public static class CreatePollForm {

        private String body;
        private String opt1;
        private String opt2;
        private String opt3;
        private String opt4;

        // getter and setters
        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getOpt1() {
            return opt1;
        }

        public void setOpt1(String opt1) {
            this.opt1 = opt1;
        }

        public String getOpt2() {
            return opt2;
        }

        public void setOpt2(String opt2) {
            this.opt2 = opt2;
        }

        public String getOpt3() {
            return opt3;
        }

        public void setOpt3(String opt3) {
            this.opt3 = opt3;
        }

        public String getOpt4() {
            return opt4;
        }

        public void setOpt4(String opt4) {
            this.opt4 = opt4;
        }
    }

    public static class VotePollForm {

        private long id;
        private String opt;

        //getters ans setters
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getOpt() {
            return opt;
        }

        public void setOpt(String opt) {
            this.opt = opt;
        }

    }

    @GetMapping({"", "/create"})
    public ModelAndView create() {
        return new ModelAndView("addPoll", "pollForm", new CreatePollForm());
    }

    @PostMapping("/create")
    public String create(CreatePollForm form, Principal principal) throws IOException {
        long pollId = pollRepo.createPoll(
                form.getBody(), form.getOpt1(), form.getOpt2(),
                form.getOpt3(), form.getOpt4());
        return "redirect:/lecture/list";
    }

    @GetMapping("/view/{pollId}")
    public ModelAndView view(@PathVariable("pollId") long pollId, ModelMap model,
            Principal principal) {
        List<Poll> polls = pollRepo.getPoll(pollId);
        List<PollAnswer> ans = pollRepo.getPollAnswer(pollId, principal.getName());
        model.addAttribute("pollId", pollId);
        model.addAttribute("poll", polls.get(0));
        model.addAttribute("commentDatabase", commentRepo.getComments());
        model.addAttribute("opt1Count", pollRepo.voteCount(pollId, "opt1"));
        model.addAttribute("opt2Count", pollRepo.voteCount(pollId, "opt2"));
        model.addAttribute("opt3Count", pollRepo.voteCount(pollId, "opt3"));
        model.addAttribute("opt4Count", pollRepo.voteCount(pollId, "opt4"));
        if (!ans.isEmpty()) {
            model.addAttribute("pollAnswer", ans.get(0));
        }

        return new ModelAndView("viewPoll", "votePollForm", new VotePollForm());
    }

    @PostMapping("/view/{pollId}")
    public View view(@PathVariable("pollId") long pollId, VotePollForm form, Principal principal) throws IOException {
        if (!pollRepo.getPollAnswer(pollId, principal.getName()).isEmpty()) {
            pollRepo.deletePollAnswer(pollId, principal.getName());
        }
        PollAnswer ans = new PollAnswer(form.getId(),
                principal.getName(), form.getOpt());
        pollRepo.save(ans);
        return new RedirectView("/", true);
    }

    @GetMapping("/delete/{pollId}")
    public View deletePoll(@PathVariable("pollId") long pollId, Principal principal) {
        pollRepo.deletePoll(pollId);
        return new RedirectView("/lecture/list", true);
    }
}
