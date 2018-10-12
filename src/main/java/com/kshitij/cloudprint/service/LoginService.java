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

    public boolean userNameExist(String username) {
        List<User> users = repository.findByUsername(username);
        return !users.isEmpty();
    }

    public User loginWith(String username, String password) {
        List<User> users = repository.findByUsernameAndAndPassword(username, password);
        if (users.isEmpty())
            return null;
        else
            return users.get(0);
    }
}
