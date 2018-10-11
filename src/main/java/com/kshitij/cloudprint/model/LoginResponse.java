package com.kshitij.cloudprint.model;

import lombok.Data;

@Data
public class LoginResponse {
    String token;
    String message;
    boolean success;

    public LoginResponse(String token, String message, boolean success) {
        this.token = token;
        this.message = message;
        this.success = success;
    }
}
