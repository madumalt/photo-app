package com.hmlet.photoapp.repository;

import com.hmlet.photoapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    User findByName(String name);
}
