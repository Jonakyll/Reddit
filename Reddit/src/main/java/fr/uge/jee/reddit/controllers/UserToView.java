package fr.uge.jee.reddit.controllers;

import fr.uge.jee.reddit.models.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserToView {

    @ModelAttribute
    public User addUserToModel(Model model) {
        User user = new User("Ailton","ea" ,User.Privilege.AUTHENTIFIED);
        model.addAttribute("user", user);
        return user;
    }
}
