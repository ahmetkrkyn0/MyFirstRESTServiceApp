package net.javaguides.springboot_first_app.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationRequest {
    // Getters ve Setters
    private String username;
    private String password;

}