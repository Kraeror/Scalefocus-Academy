package com.example.banksystem.services.interfaces;

import com.example.banksystem.models.requsts.UserLoginRequest;
import com.example.banksystem.models.responses.UserResponse;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

	UserResponse authenticateUser(HttpServletRequest request, UserLoginRequest userLoginRequest);
}
