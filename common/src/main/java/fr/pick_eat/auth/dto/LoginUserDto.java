package fr.pick_eat.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {
    private String email;

    private String password;

    public LoginUserDto() {
    }

    public LoginUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String toString() {
        return "LoginUserDto(email=" + this.getEmail() + ", password=" + this.getPassword() + ")";
    }
}