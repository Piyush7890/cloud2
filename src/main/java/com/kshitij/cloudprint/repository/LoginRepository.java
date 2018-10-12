package com.kshitij.cloudprint.repository;

import com.kshitij.cloudprint.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LoginRepository extends MongoRepository<User, String> {
    List<User> findByUsername(String username);

    List<User> findByUsernameAndAndPassword(String username, String password);
}
