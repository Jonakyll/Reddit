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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

@Controller
public class VoteController {
    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    private RedirectView redirectPage(String url) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;

    }

    private void addOrRemoveVote(User connectUser, Set<User> users, Set<User> oppositeUsers) {
        if (oppositeUsers.contains(connectUser)) {
            oppositeUsers.remove(connectUser);
            users.add(connectUser);
        } else if (users.contains(connectUser)) {
            users.remove(connectUser);
        } else {
            users.add(connectUser);
        }
    }

    private void updateValueTopic(Topic topic){
        topic.setValue(topic.getUpvotes().size());
    }

    private RedirectView voteTopic(Long id, String url, BiConsumer<Set<User>, Set<User>> consumer) {
        Optional<Topic> topicOptional = topicService.findById(id);

        if (topicOptional.isPresent()){
            Topic topic = topicOptional.get();
            consumer.accept(topic.getUpvotes(), topic.getDownvotes());
            updateValueTopic(topic);
            topicService.save(topic);
            return redirectPage(url);
        }else{

            return redirectPage("error");
        }
    }

    private RedirectView voteComment(Long id, String url, BiConsumer<Set<User>, Set<User>> consumer) {
        Optional<Comment> commentOptional = commentService.findById(id);

        if (commentOptional.isPresent()){
            Comment comment = commentOptional.get();
            consumer.accept(comment.getUpvotes(), comment.getDownvotes());
            commentService.save(comment);
            return redirectPage(url);
        }else{
            return redirectPage("error");
        }
    }

    @PostMapping(path = {"/{idComment}/upVoteComment", "/{idTopic}/upVoteTopic"})
    public RedirectView upVote(@PathVariable("idTopic") Optional<Long> idTopic,
                               @PathVariable("idComment") Optional<Long> idComment,
                               @RequestParam("url") String url){
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(obj instanceof CustomUserDetails)) {
            return redirectPage("/users/login");
        }
        CustomUserDetails user = (CustomUserDetails) obj;
        User connectUser = userService.findByName(user.getUsername()).get();

        if (idTopic.isPresent()) {
            return voteTopic(idTopic.get(), url, (up, down) -> addOrRemoveVote(connectUser, up, down));
        } else if (idComment.isPresent()) {
            return voteComment(idComment.get(), url, (up, down) -> addOrRemoveVote(connectUser, up, down));
        } else {
            return redirectPage("error");
        }
    }

    @PostMapping(path = {"/{idComment}/downVoteComment", "/{idTopic}/downVoteTopic"})
    public RedirectView downVote(@PathVariable("idTopic") Optional<Long> idTopic,
                                 @PathVariable("idComment") Optional<Long> idComment,
                                 @RequestParam("url") String url) {

        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(obj instanceof CustomUserDetails)) {
            return redirectPage("/users/login");
        }
        CustomUserDetails user = (CustomUserDetails) obj;
        User connectUser = userService.findByName(user.getUsername()).get();

        if (idTopic.isPresent()) {
            return voteTopic(idTopic.get(), url, (up, down) -> addOrRemoveVote(connectUser, down, up));
        } else if (idComment.isPresent()) {
            return voteComment(idComment.get(), url, (up, down) -> addOrRemoveVote(connectUser, down, up));
        } else {
            return redirectPage("error");
        }
    }
}
