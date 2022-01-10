package fr.uge.jee.reddit.controllers;

import fr.uge.jee.reddit.models.Comment;
import fr.uge.jee.reddit.models.PasswordChanger;
import fr.uge.jee.reddit.models.Topic;
import fr.uge.jee.reddit.models.User;
import fr.uge.jee.reddit.security.CustomUserDetails;
import fr.uge.jee.reddit.services.CommentService;
import fr.uge.jee.reddit.services.TopicService;
import fr.uge.jee.reddit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private TopicService topicService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/users/all")
    public String getAllUsers(Model model) {
        //service.save(new User("Fabien", User.Privilege.VISITOR));
        model.addAttribute("users", service.findAll());
        return "users/allUsers";
    }

    @GetMapping("/users/signUp")
    public String createAccount(Model model) {
        model.addAttribute("user", new User());
        return "users/signUp";
    }

    @PostMapping("/users/accountCreated")
    public String accountCreated(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "error";
        }

        Optional<User> optUser = service.findByName(user.getName());
        if (user.getName().equals("admin") || optUser.isPresent()) {
            return "users/signUp";
        }
        user.setRole(User.Privilege.AUTHENTIFIED);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        service.save(user);
        return "redirect:/users/login";
    }

    @GetMapping("/users/login")
    public String loginForm(Model model) {
        return "users/login";
    }

    @GetMapping("/users/login_success")
    public String loginSuccess(Model model) {
        return "users/login_success";
    }

    @GetMapping("/users/profile/{id}")
    public String profile(Model model, @PathVariable("id") long id) {
        Optional<User> user = service.findById(id);
        if (user.isPresent()) {

            List<Topic> topics = topicService
                    .findAll()
                    .stream()
                    .filter(topic -> topic.getAuthor().getName().equals(user.get().getName()))
                    .collect(Collectors.toList());
            List<Comment> comments = commentService
                    .findAll()
                    .stream()
                    .filter(comment -> comment.getAuthor().getName().equals(user.get().getName()))
                    .sorted((o1, o2) -> -Long.compare(o1.getDate(), o2.getDate()))
                    .collect(Collectors.toList());
            if (comments.size() >= 5) {
                comments = comments.subList(0, 5);
            }

            model.addAttribute("user", user.get());
            model.addAttribute("topics", topics);
            model.addAttribute("comments", comments);
            return "users/profile";
        } else {
            return "error";
        }
    }

    @GetMapping("/users/profile")
    public String myProfile(Model model) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Topic> topics = topicService
                .findAll()
                .stream()
                .filter(topic -> topic.getAuthor().getName().equals(user.getUsername()))
                .collect(Collectors.toList());
        List<Comment> comments = commentService
                .findAll()
                .stream()
                .filter(comment -> comment.getAuthor().getName().equals(user.getUsername()))
                .sorted((o1, o2) -> -Long.compare(o1.getDate(), o2.getDate()))
                .collect(Collectors.toList());
        if (comments.size() >= 5) {
            comments = comments.subList(0, 5);
        }

        model.addAttribute("user", service.findByName(user.getUsername()).get());
        model.addAttribute("topics", topics);
        model.addAttribute("comments", comments);
        return "users/profile";

    }

    @GetMapping("/users/changePassword/{id}")
    public String changePassword(Model model,
                                 @PathVariable("id") long id) {
        Optional<User> optUser = service.findById(id);

        if (optUser.isPresent()) {
            model.addAttribute("user", optUser.get());
            model.addAttribute("passwordChanger", new PasswordChanger());
            return "users/changePassword";
        }
        return "error";
    }

    @PostMapping("/users/processChangePassword/{id}")
    public String processChangePassword(Model model,
                                        @ModelAttribute("passwordChanger") PasswordChanger passwordChanger,
                                        @PathVariable("id") long id) {
        Optional<User> optUser = service.findById(id);

        if (optUser.isPresent()) {
            User user = optUser.get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (encoder.matches(passwordChanger.getOldPassword(), user.getPassword())) {
                user.setPassword(encoder.encode(passwordChanger.getNewPassword()));
                service.save(user);
                return "redirect:/";
            }
            model.addAttribute("user", user);
            model.addAttribute("passwordChanger", new PasswordChanger());
            return "/users/changePassword";
        }
        return "error";
    }

}

