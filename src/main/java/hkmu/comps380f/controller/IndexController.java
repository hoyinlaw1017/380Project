package hkmu.comps380f.controller;

import hkmu.comps380f.dao.LectureRepository;
import hkmu.comps380f.dao.PollRepository;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Resource
    private PollRepository pollRepo;
    @Resource
    private LectureRepository lectureRepo;
    

    @GetMapping
    public String index() {
        return "redirect:/lecture/list";
    }

    @GetMapping("/")
    public String view(ModelMap model) {
        model.addAttribute("lectureDatabase", lectureRepo.getLectures());
        model.addAttribute("pollDatabase", pollRepo.getPolls());
        return "list";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
