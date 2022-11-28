package com.example.banksystem.models.requsts;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserLoginRequest {

    @NotBlank(message = "Username's name cannot be blank")
    @Size(min = 5,max = 25, message = "Name's length should be between 5 and 25 symbols")
    private String username;

    @NotBlank(message = "Password's name cannot be blank")
    @Size(min = 5,max = 30, message = "Password's length should be between 5 and 30 symbols")
    private String password;

    public UserLoginRequest() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
