package com.hmlet.photoapp.service;

import com.hmlet.photoapp.exception.UserAlreadyExistsException;
import com.hmlet.photoapp.exception.UserDoesNotExistsException;
import com.hmlet.photoapp.model.User;
import com.hmlet.photoapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public User addUser(User user) {
        User existingUser = this.userRepository.findByName(user.getName());
        if (existingUser == null) {
            return save(user);
        }
        throw new UserAlreadyExistsException(existingUser.getId(), existingUser.getName());
    }

    public User getUser(String username) {
        User user = this.userRepository.findByName(username);
        if (user == null) {
            throw new UserDoesNotExistsException(username);
        }
        return user;
    }
}
