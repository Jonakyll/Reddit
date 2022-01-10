package fr.uge.jee.reddit.services;

import fr.uge.jee.reddit.models.Comment;
import fr.uge.jee.reddit.models.Topic;
import fr.uge.jee.reddit.repositories.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicRepo repository;

    public List<Topic> findAll() {
        List<Topic> topics = (List<Topic>) repository.findAll(Sort.by(Sort.Direction.DESC, "value"));
        return topics;
    }


    public void save(Topic topic) {
        repository.findById(topic.getId()).map(
                t -> {
                    t.setUpvotes(topic.getUpvotes());
                    t.setDownvotes(topic.getDownvotes());
                    t.setAnswers(topic.getAnswers());
                    repository.save(t);
                    return t;
                }
        ).orElseGet(() -> {
            repository.save(topic);
            return topic;
        });
    }

    public Optional<Topic> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Topic topic) {
        repository.delete(topic);
    }

    public void addCommentToTopic(Long id, Comment comment) {
        Optional<Topic> optionalTopic = repository.findById(id);
        if (!optionalTopic.isPresent()) {
            throw new NoSuchElementException("Error, no such topic ID !");
        }
        optionalTopic.get().getAnswers().add(comment);
    }

    public void deleteCommentFromTopic(Long id, Comment comment) {
        Optional<Topic> optionalTopic = repository.findById(id);
        if (!optionalTopic.isPresent()) {
            throw new NoSuchElementException("Error, no such topic ID !");
        }
        List<Comment> comments = optionalTopic.get().getAnswers();
        comments.remove(comment);
    }
}
