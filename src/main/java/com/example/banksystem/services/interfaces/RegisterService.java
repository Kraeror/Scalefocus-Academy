package com.example.banksystem.services.interfaces;

import com.example.banksystem.models.requsts.UserRegisterRequest;
import com.example.banksystem.models.responses.UserResponse;

public interface RegisterService {
	UserResponse register(UserRegisterRequest userBingingModel);
}
