package fr.uge.jee.reddit.controllers;

import fr.uge.jee.reddit.models.Comment;
import fr.uge.jee.reddit.models.Topic;
import fr.uge.jee.reddit.models.User;
import fr.uge.jee.reddit.security.CustomUserDetails;
import fr.uge.jee.reddit.services.TopicService;
import fr.uge.jee.reddit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

@Controller
public class TopicController {

    @Autowired
    private TopicService service;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getAllTopics(Model model) {
        model.addAttribute("topics", service.findAll());
        return "topics/allTopics";
    }

    @GetMapping("/topics/addTopic")
    public String addTopic(Model model){
        model.addAttribute("topic", new Topic());
        return "topics/addTopic";
    }

    @PostMapping("/topics/addedTopic")
    public String submitAddTopic(@ModelAttribute("topic")Topic topic,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "error";
        }

        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        topic.setAuthor(userService.findByName(user.getUsername()).get());
        topic.setDate(System.currentTimeMillis());
        service.save(topic);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String topicView(Model model, @PathVariable("id") long id){
        Optional<Topic> topic = service.findById(id);
        if (topic.isPresent()){
            model.addAttribute("mytopic",topic.get());
            model.addAttribute("comment", new Comment());
            model.addAttribute("comments", topic.get().getAnswers());
            return "topics/topicView";
        }else{
            return "error";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteTopic(Model model, @PathVariable("id") long id){
        Optional<Topic> topicOptional = service.findById(id);
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (topicOptional.isPresent()){
            service.delete(topicOptional.get());
            return "redirect:/";
        }else{
            return "error";
        }
    }
}

