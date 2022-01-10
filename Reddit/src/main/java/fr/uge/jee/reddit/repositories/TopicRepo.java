package fr.uge.jee.reddit.repositories;

import fr.uge.jee.reddit.models.Topic;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;


public interface TopicRepo extends CrudRepository<Topic, Long> {

    Iterable<Topic> findAll(Sort sort);

}
