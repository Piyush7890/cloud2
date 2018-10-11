package com.kshitij.cloudprint.service;

import com.kshitij.cloudprint.model.User;
import com.kshitij.cloudprint.repository.LoginRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
    @Autowired
    LoginRepository repository;

    public User create(User user) {
        user.setId(ObjectId.get().toString());
        return repository.save(user);
    }

    public boolean userExist(String username) {
        List<User> users = repository.findByUsername(username);
        return !users.isEmpty();
    }
}
