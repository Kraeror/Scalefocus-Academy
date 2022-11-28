package com.example.banksystem.controllers;

import com.example.banksystem.models.requsts.UserLoginRequest;
import com.example.banksystem.models.responses.UserResponse;
import com.example.banksystem.services.interfaces.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class LoginController {

    private final LoginService loginService;
    private final Logger logger = LogManager.getLogger(LoginController.class);

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Authentication of an existing user.
     *
     * @param userLoginRequest an entity in which the data for login is inserted.
     * @param errors                stores and exposes information about data-binding and validation errors for a user.
     * @return ok if the user has authentication.
     * @throws IllegalArgumentException if illegal or unsuitable argument passed to a method.
     */
    @PostMapping(value = "/login")
    public ResponseEntity<UserResponse> authenticateUser(
            HttpServletRequest request, @Valid @RequestBody UserLoginRequest userLoginRequest,
            Errors errors) throws IllegalArgumentException {

        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getFieldErrors().get(0).getDefaultMessage());
        }

        UserResponse model = loginService.authenticateUser(request, userLoginRequest);
        logger.info("User with username {} successfully logged in", userLoginRequest.getUsername());
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
