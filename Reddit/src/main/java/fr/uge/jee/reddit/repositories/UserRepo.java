package fr.uge.jee.reddit.repositories;

import fr.uge.jee.reddit.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.name = ?1")
    User findByName(String name);
}
