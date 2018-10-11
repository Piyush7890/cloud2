package com.kshitij.cloudprint.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResponse {
    String token;
    String message;
    boolean success;
}
