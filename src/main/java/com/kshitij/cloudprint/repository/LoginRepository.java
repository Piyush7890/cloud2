package com.kshitij.cloudprint.repository;

import com.kshitij.cloudprint.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginRepository extends MongoRepository<User, String> {
}
