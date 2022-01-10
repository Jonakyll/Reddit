package fr.uge.jee.reddit.controllers;

import fr.uge.jee.reddit.models.Comment;
import fr.uge.jee.reddit.models.Topic;
import fr.uge.jee.reddit.models.User;
import fr.uge.jee.reddit.security.CustomUserDetails;
import fr.uge.jee.reddit.services.CommentService;
import fr.uge.jee.reddit.services.TopicService;
import fr.uge.jee.reddit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@Controller
public class CommentController {

    @Autowired
    private CommentService service;

    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

    @GetMapping("/comments/all")
    public String getAllComments(Model model) {
        model.addAttribute("comments", service.findAll());
        return "comments/allComments";
    }

    @ModelAttribute("comment")
    public Comment newComment() {
        return new Comment();
    }

    @PostMapping(path = {"/comments/addedComment/{topicId}", "/comments/addedSubComment/{commentId}"})
    public String submitAddComment(@ModelAttribute("comment") Comment comment,
                                   @PathVariable("topicId") Optional<Long> topicId,
                                   @PathVariable("commentId") Optional<Long> commentId,
                                   @RequestParam("url") String url,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "error";
        }

        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setDate(System.currentTimeMillis());
        comment.setAuthor(userService.findByName(user.getUsername()).get());

        if (topicId.isPresent()) {
            topicService.addCommentToTopic(topicId.get(), comment);
            service.save(comment);
            comment.setParentId(comment.getId());
            service.save(comment);

            return "redirect:" + url;
        } else if (commentId.isPresent()) {
            service.addSubComment(commentId.get(), comment);
            service.save(comment);

            return "redirect:" + url;
        }
        return "error";
    }


    @PostMapping("/comments/deletedComment/{topicId}/{commentId}")
    public RedirectView deleteComment(@ModelAttribute("comment") Comment comment,
                                @PathVariable("topicId") Optional<Long> topicId,
                                @PathVariable("commentId") Optional<Long> commentId,
                                @RequestParam("url") String url,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            return new RedirectView("error");
        }

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.findByName(userDetails.getUsername());

        if (user.isPresent() && commentId.isPresent()) {

            Optional<Comment> delComment = service.findById(commentId.get());
            if (delComment.isPresent() && (delComment.get().getAuthor() == user.get() || user.get().getRole() == User.Privilege.ADMIN)) {
                service.deleteSubComment(delComment.get().getParentId(), delComment.get());
                if (topicId.isPresent()) {
                    topicService.deleteCommentFromTopic(topicId.get(), delComment.get());
                }
            }

            service.delete(comment);
        }
        return new RedirectView(url);
    }

    private void parentId(Long commentId, HttpServletRequest request) {
        Optional<Comment> comment = service.findById(commentId);
        if (comment.isPresent()) {
            List<Comment> comments = service.findAllChild(comment.get().getId());
            request.setAttribute("commentId" + comment.get().getId(), comments);
            if (comment.get().getParentId() == comment.get().getId()) {
                return;
            } else {
                parentId(comment.get().getParentId(), request);
            }
        }
    }

    @GetMapping("/comments/allFromTopic/{topicId}/{commentId}")
    public ModelAndView getAllCommentsOfTopic(
            @PathVariable("topicId") Long topicId,
            @PathVariable("commentId") Optional<Long> commentId,
            ModelMap modelMap,
            HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("forward:/" + topicId, modelMap);

        if (commentId.isPresent()) {
            parentId(commentId.get(), request);

            return modelAndView;


        }

        return new ModelAndView(new RedirectView("error"));

    }

}
