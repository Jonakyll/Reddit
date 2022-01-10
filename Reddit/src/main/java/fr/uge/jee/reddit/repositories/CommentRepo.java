package fr.uge.jee.reddit.repositories;

import fr.uge.jee.reddit.models.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepo extends CrudRepository<Comment, Long> {

    @Query("select c from Comment c where c.parentId = ?1 and c.id <> c.parentId order by c.value desc")
    Iterable<Comment> findAllChild(Long parentId);

}
