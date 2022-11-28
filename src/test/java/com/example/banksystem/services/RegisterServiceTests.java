package com.example.banksystem.services;

import com.example.banksystem.models.entities.RoleEntity;
import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.models.requsts.UserRegisterRequest;
import com.example.banksystem.models.responses.UserResponse;
import com.example.banksystem.repositories.RolesRepository;
import com.example.banksystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTests {
	@Mock
	ModelMapper mapper;
	@Mock
	UserRepository userRepository;
	@Mock
	RolesRepository rolesRepository;
	@Mock
	BCryptPasswordEncoder encoder;
	@InjectMocks
	RegisterServiceImpl registerService;
	@Captor
	private ArgumentCaptor<UserEntity> anyUserEntity;

	UserRegisterRequest userBindingModel;
	UserEntity userEntity;
	UserResponse userResponse;
	RoleEntity role;

	@BeforeEach
	void init() {
		userBindingModel = new UserRegisterRequest();
		userBindingModel.setFullName("Test User")
						.setUsername("test_username")
						.setEmail("test_email@gamil.com")
						.setPassword("123456")
						.setConfirmPassword("123456")
						.setPhoneNumber("0888123456");
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFullName("Test User")
						.setUsername("test_username")
						.setEmail("test_email@gamil.com")
						.setPhoneNumber("0888123456");
		role = new RoleEntity("USER");
		userResponse = new UserResponse()
				.setId(1L)
				.setFullName("Test User")
				.setUsername("test_username")
				.setEmail("test_email@gamil.com")
				.setPhoneNumber("0888123456");
	}

	@Test
	void register_shouldRegisterNewUser_ok() {
		when(mapper.map(userBindingModel, UserEntity.class)).thenReturn(userEntity);

		when(encoder.encode(userBindingModel.getPassword())).thenReturn("123456");
		userEntity.setPassword(userBindingModel.getPassword());
		when(rolesRepository.findByRole("USER")).thenReturn(Optional.ofNullable(role));

		userEntity.addRole(role);
		when(userRepository.save(userEntity)).thenReturn(userEntity);
		when(mapper.map(userEntity, UserResponse.class)).thenReturn(userResponse);

		UserResponse model = registerService.register(userBindingModel);

		verify(userRepository).save(anyUserEntity.capture());

		assertThat(anyUserEntity.getValue().getId()).isEqualTo(model.getId());
		assertThat(anyUserEntity.getValue().getFullName()).isEqualTo(userBindingModel.getFullName());
		assertThat(anyUserEntity.getValue().getUsername()).isEqualTo(userBindingModel.getUsername());
		assertThat(anyUserEntity.getValue().getEmail()).isEqualTo(userBindingModel.getEmail());
		assertThat(anyUserEntity.getValue().getPassword()).isEqualTo(userBindingModel.getPassword());
		assertThat(anyUserEntity.getValue().getPhoneNumber()).isEqualTo(userBindingModel.getPhoneNumber());
		assertThat(anyUserEntity.getValue().getRoles().stream().findFirst().get().getRole()).isEqualTo("USER");
	}
}
