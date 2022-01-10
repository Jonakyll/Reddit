package fr.uge.jee.reddit.services;

import fr.uge.jee.reddit.models.Comment;
import fr.uge.jee.reddit.repositories.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepo repository;

    public List<Comment> findAll() {
        List<Comment> comments = (List<Comment>) repository.findAll();
        return comments;
    }

    public List<Comment> findAllChild(Long parentId) {
        List<Comment> comments = (List<Comment>) repository.findAllChild(parentId);
        return comments;

    }

    public void save(Comment comment) {
        repository.save(comment);
    }

    public Optional<Comment> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Comment comment) {
        repository.delete(comment);
    }

    public void addSubComment(Long commentId, Comment subComment) {
        Optional<Comment> optionalComment = repository.findById(commentId);
        if (!optionalComment.isPresent()) {
            throw new NoSuchElementException("Error, no such comment ID !");
        }
        subComment.setParentId(commentId);
        optionalComment.get().getAnswers().add(subComment);
    }

    public void deleteSubComment(Long commentId, Comment subComment) {
        Optional<Comment> optionalComment = repository.findById(commentId);
        if (!optionalComment.isPresent()) {
            throw new NoSuchElementException("Error, no such comment ID !");
        }
        List<Comment> comments = optionalComment.get().getAnswers();
        comments.remove(subComment);
    }
}
