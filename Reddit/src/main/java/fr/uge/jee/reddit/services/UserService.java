package fr.uge.jee.reddit.services;

import fr.uge.jee.reddit.models.User;
import fr.uge.jee.reddit.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo repository;

    public List<User> findAll() {
        List<User> users = (List<User>) repository.findAll();
        return users;
    }

    public void save(User user) {
        repository.save(user);
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<User> findByName(String name) { return Optional.ofNullable(repository.findByName(name)); }

    public void delete(User user) {
        repository.delete(user);
    }
}
