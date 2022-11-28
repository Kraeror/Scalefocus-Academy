package com.example.banksystem.models.requsts;

import com.example.banksystem.models.validation.UniqueEmail;
import com.example.banksystem.models.validation.UniqueUsername;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegisterRequest {

    @NotBlank(message = "Client's name cannot be blank")
    @Size(min = 5,max = 50, message = "Name's length should be between 5 and 50 symbols")
    private String fullName;

    @NotBlank(message = "Username's name cannot be blank")
    @UniqueUsername
    @Size(min = 5,max = 25, message = "Name's length should be between 5 and 25 symbols")
    private String username;

    @NotBlank(message = "Email's name cannot be blank")
    @Email(message = "Email must be in valid format")
    @UniqueEmail
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 5,max = 30, message = "Password's length should be between 5 and 30 symbols")
    private String password;

    @NotBlank(message = "Confirm password cannot be blank")
    @Size(min = 5,max = 30, message = "Confirm password length should be between 5 and 30 symbols")
    private String confirmPassword;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^0[89][7-9]\\d{7}|(\\+359[89][7-9]\\d{7})$",message = "Please enter the number in valid format")
    private String phoneNumber;

    public UserRegisterRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public UserRegisterRequest setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserRegisterRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegisterRequest setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserRegisterRequest setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}
