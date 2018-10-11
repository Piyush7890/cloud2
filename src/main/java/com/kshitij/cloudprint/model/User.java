package com.kshitij.cloudprint.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
    @Id
    String id;
    String username;
    String email;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
