package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.models.entities.RoleEntity;
import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.models.requsts.UserRegisterRequest;
import com.example.banksystem.models.responses.UserResponse;
import com.example.banksystem.repositories.RolesRepository;
import com.example.banksystem.repositories.UserRepository;
import com.example.banksystem.services.interfaces.RegisterService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final RolesRepository rolesRepository;
	private final ModelMapper mapper;
	private final EmailServiceImpl emailService;

	public RegisterServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
							   RolesRepository rolesRepository, ModelMapper mapper, EmailServiceImpl emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.rolesRepository = rolesRepository;
		this.mapper = mapper;
		this.emailService = emailService;
	}

	@Override
	public UserResponse register(UserRegisterRequest userBingingModel) {
		UserEntity user = mapper.map(userBingingModel, UserEntity.class);

		String encodedPassword = this.passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		RoleEntity role = this.rolesRepository.findByRole("USER")
						.orElseThrow(()->new EntityNotFoundException("Role"));
		user.addRole(role);

		UserEntity createdUser = this.userRepository.save(user);

//		emailService.sendRegistrationEmail(createdUser.getEmail(),createdUser.getFullName());
		return mapper.map(createdUser, UserResponse.class);
	}
}
