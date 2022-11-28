package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.PasswordNotMatchingException;
import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.models.requsts.PasswordChangeRequest;
import com.example.banksystem.models.requsts.UserUpdateRequest;
import com.example.banksystem.models.responses.UserResponse;
import com.example.banksystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private UserServiceImpl userService;

	UserEntity user;
	UserResponse userResponse;

	@BeforeEach
	public void setup() {
		user = new UserEntity();
		user.setId(1L);
		user.setUsername("Joro");
		user.setPassword("12345");
		user.setEmail("joro@gmail.com");
		user.setFullName("Georgi Stefanov");
		user.setPhoneNumber("0898123456");
		userResponse = new UserResponse();
		userResponse.setId(1L);
		userResponse.setUsername("Joro");
		userResponse.setEmail("joro@gmail.com");
		userResponse.setFullName("Georgi Stefanov");
		userResponse.setPhoneNumber("0898123456");
	}

	@Test
	public void getUserEntityById_existingTable_ok() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);

		UserResponse userResponse1 = userService.getUserById(1L,1L);

		assertThat(userResponse1).isNotNull();
	}

	@Test
	public void getUserEntityById_throws() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> userService.getUserEntityById(1L));
	}

	@Test
	public void getAllUser_existingTable_ok() {
		List<UserEntity> userList = new ArrayList<>();
		userList.add(user);

		when(userRepository.findAll()).thenReturn(userList);
		when(modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);

		List<UserResponse> userResponseList = userService.getAllUser();

		assertThat(userResponseList).isNotNull();
		assertThat(userResponseList.size()).isEqualTo(1);
	}

	@Test
	public void getUserByUsername_existingTable_ok() {
		when(userRepository.findByUsername("Joro")).thenReturn(Optional.of(user));
		when(modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);

		UserResponse userResponse1 = userService.getUserByUsername("Joro");

		assertThat(userResponse1).isNotNull();
	}

	@Test
	public void getUserByUsername_throw() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername(anyString()));
	}

	@Test
	public void updateUser_noChanges_ok() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);
		when(userRepository.save(user)).thenReturn(user);

		assertEquals(userService.updateUser(new UserUpdateRequest(), 1L,1L), userResponse);
	}

	@Test
	public void updateUser_existingTable_ok() {
		UserUpdateRequest userUpdateRequest =
						new UserUpdateRequest().setPhoneNumber("0898234567").setEmail("ivancho@gmail.com")
										.setFullName("Ivan Petrov").setUsername("ivancho");
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);

		user.setPhoneNumber("0898234567").setEmail("ivancho@gmail.com")
						.setFullName("Ivan Petrov").setUsername("ivancho").setPassword("12345");

		when(userRepository.save(user)).thenReturn(user);

		assertEquals(userService.updateUser(userUpdateRequest, 1L,1L), userResponse);
	}

	@Test
	public void updateUser_throw() {
		UserUpdateRequest userUpdateRequest =
						new UserUpdateRequest().setPhoneNumber("0898234567").setEmail("ivancho@gmail.com")
										.setFullName("Ivan Petrov").setUsername("ivancho");

		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userUpdateRequest, 4L,1L));
	}

	@Test
	public void changePassword_validInput_ok() {
		PasswordChangeRequest validRequest =
						new PasswordChangeRequest().setCurrentPassword("12345").setNewPassword("00000")
										.setConfirmNewPassword("00000");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		when(userRepository.save(any())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
		assertTrue(userService.changePassword(1L, validRequest));
	}

	@Test
	public void changePassword_WrongCurrentPassword_ThrowsException() {
		PasswordChangeRequest invalidRequest =
						new PasswordChangeRequest().setCurrentPassword("12675").setNewPassword("00000")
										.setConfirmNewPassword("00000");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		assertThrows(PasswordNotMatchingException.class, () -> userService.changePassword(1L, invalidRequest));
	}

	@Test
	public void changePassword_WrongPasswordConfirmation_ThrowsException() {
		PasswordChangeRequest invalidRequest =
						new PasswordChangeRequest().setCurrentPassword("12345").setNewPassword("00000")
										.setConfirmNewPassword("11111");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		assertThrows(PasswordNotMatchingException.class, () -> userService.changePassword(1L, invalidRequest));
	}

	@Test
	void getUserByAccountId_throw(){
		when(userRepository.findByCheckingAccounts_id(1L)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class,
				()-> userService.getUserByAccountId(1L));
	}


	@Test
	void getUserByAccountId_okay(){
		when(userRepository.findByCheckingAccounts_id(1L)).thenReturn(Optional.of(user));
		assertEquals(userService.getUserByAccountId(1L),user);
	}

	@Test
	void applyForLoan_throw(){
		when(userRepository.findById(1L)).thenReturn(Optional.of(user.setHasLoan(true)));
		assertThrows(IllegalArgumentException.class,
				()-> userService.applyForLoan(1L));
	}

	@Test
	void applyForLoan_okay(){
		when(userRepository.findById(1L)).thenReturn(Optional.of(user.setHasLoan(false)));
		assertEquals(userService.applyForLoan(1L),user.setHasLoan(false));
	}
}
