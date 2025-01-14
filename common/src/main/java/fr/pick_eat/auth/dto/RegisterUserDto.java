package fr.pick_eat.auth.dto;

import lombok.Getter;

@Getter
public class RegisterUserDto {
    private String email;
    
    private String password;
    
    private String firstName;

    private String lastName;

    public RegisterUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public RegisterUserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public RegisterUserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}