package com.example.banksystem.controllers;

import com.example.banksystem.models.requsts.UserRegisterRequest;
import com.example.banksystem.models.responses.UserResponse;
import com.example.banksystem.services.interfaces.RegisterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class RegisterController {

    private final RegisterService registerService;
    private final Logger logger = LogManager.getLogger(RegisterController.class);

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    /**
     *  Registration of a new client.
     *
     * @param userRegisterRequest an entity in which the data for register a new client is inserted.
     * @param errors stores and exposes information about data-binding and validation errors for a user.
     * @return an {@link UserResponse} with the data of the registered client.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerClient(
            @Valid @RequestBody UserRegisterRequest userRegisterRequest, Errors errors){

        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getFieldErrors().get(0).getDefaultMessage());
        }

        UserResponse model = registerService.register(userRegisterRequest);
        logger.info("User with username {} has been successfully registered", userRegisterRequest.getUsername());
        return ResponseEntity.created(URI.create("/users/" + model.getId())).body(model);
    }
}
