package com.example.banksystem.models.requsts;

import com.example.banksystem.models.validation.UniqueEmail;
import com.example.banksystem.models.validation.UniqueUsername;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserUpdateRequest {

    @UniqueUsername
    @Size(min = 3,max = 25, message = "Name's length should be between 3 and 25 symbols")
    private String username;


    @Email(message = "Email must be in valid format")
    @UniqueEmail
    private String email;


    @Size(min = 5,max = 50, message = "Name's length should be between 5 and 50 symbols")
    private String fullName;


    @Pattern(regexp = "^0[89][7-9]\\d{7}|(\\+359[89][7-9]\\d{7})$",message = "Please enter the number in valid format")
    private String phoneNumber;

    public String getUsername() {
        return username;
    }

    public UserUpdateRequest setUsername(String username) {
        this.username = username;
        return this;
    }
    

    public String getEmail() {
        return email;
    }

    public UserUpdateRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserUpdateRequest setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserUpdateRequest setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}
