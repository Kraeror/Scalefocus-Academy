package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.PasswordNotMatchingException;
import com.example.banksystem.exceptions.ResourceNotBelongingToUser;
import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.models.requsts.LoanApplyRequest;
import com.example.banksystem.models.requsts.PasswordChangeRequest;
import com.example.banksystem.models.requsts.UserUpdateRequest;
import com.example.banksystem.models.responses.UserResponse;
import com.example.banksystem.repositories.UserRepository;
import com.example.banksystem.services.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An implementation class for {@link UserService} interface.
 */
@Service
public class UserServiceImpl implements UserService {

	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository,
						   BCryptPasswordEncoder passwordEncoder) {
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param username the username of the selected UserEntity.
	 * @return a {@link UserResponse}
	 */
	@Override
	public UserResponse getUserByUsername(String username) {
		UserEntity user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("There is no user with  username " + username));
		return modelMapper.map(user, UserResponse.class);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param userUpdateRequest an entity holding the parameters about the update of the fields.
	 * @param id                the ID of the UserEntity which will be updated.
	 * @return a {@link UserResponse} with the updated fields.
	 */
	@Override
	public UserResponse updateUser(UserUpdateRequest userUpdateRequest, Long id,Long loggedInUserId) {
		if(isAdmin(loggedInUserId) && loggedInUserId != id){
			throw new ResourceNotBelongingToUser("User Account");
		}

		UserEntity userEntity = this.getUserEntityById(id);

		if (userUpdateRequest.getFullName() != null && !userUpdateRequest.getFullName().isBlank()) {
			userEntity.setFullName(userUpdateRequest.getFullName());
		}

		if (userUpdateRequest.getUsername() != null && !userUpdateRequest.getUsername().isBlank()) {
			userEntity.setUsername(userUpdateRequest.getUsername());
		}

		if (userUpdateRequest.getEmail() != null && !userUpdateRequest.getEmail().isBlank()) {
			userEntity.setEmail(userUpdateRequest.getEmail());
		}

		if (userUpdateRequest.getPhoneNumber() != null && !userUpdateRequest.getPhoneNumber().isBlank()) {
			userEntity.setPhoneNumber(userUpdateRequest.getPhoneNumber());
		}

		return modelMapper.map(userRepository.save(userEntity), UserResponse.class);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param accountId the id of the account
	 * @return a {@link UserEntity}
	 */
	@Override
	public UserEntity getUserByAccountId(Long accountId) {
		return userRepository.findByCheckingAccounts_id(accountId)
				.orElseThrow(() -> new EntityNotFoundException("User Account"));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param id the id of the selected UserEntity.
	 * @return a {@link UserResponse}
	 */
	@Override
	public UserResponse getUserById(Long id,Long loggedInUserId) {
		if(isAdmin(loggedInUserId) && loggedInUserId != id){
			throw new ResourceNotBelongingToUser("User Account");
		}
		return modelMapper.map(this.getUserEntityById(id), UserResponse.class);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return a list of {@link UserResponse}
	 */
	@Override
	public List<UserResponse> getAllUser() {
		return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserResponse.class))
													.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param id the id of the selected UserEntity.
	 * @return a {@link UserEntity}
	 * @throws EntityNotFoundException when a user with the given id is not found.
	 */
	public UserEntity getUserEntityById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User"));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param userId                the id of the user whose password will be changed/
	 * @param passwordChangeRequest a {@link PasswordChangeRequest} with the parameters for the password change.
	 * @return true if operation was successful, false if an error occurred.
	 * @throws PasswordNotMatchingException when currentPassword does not match or
	 *                                      the confirmation of the new password is wrong.
	 */
	@Override
	public boolean changePassword(Long userId, PasswordChangeRequest passwordChangeRequest) {
		UserEntity user = getUserEntityById(userId);
		if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmNewPassword()))
			throw new PasswordNotMatchingException("Wrong confirmation for new password!");
		if (!passwordEncoder.matches(passwordChangeRequest.getCurrentPassword(), user.getPassword()))
			throw new PasswordNotMatchingException("This is not your current password!");

		user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
		userRepository.save(user);
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param userId       the id of the currently logged in user.
	 */
	@Override
	public UserEntity applyForLoan(Long userId) {
		UserEntity user = this.getUserEntityById(userId);

		if (user.hasLoan()) {
			throw new IllegalArgumentException("User already has a loan.");
		}

		return user;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param isApproved is the loan approved
	 * @param user       the user who apply for the loan
	 */
	@Override
	public void updateHasLoanStatus(boolean isApproved, UserEntity user) {
		user.setHasLoan(isApproved);
		userRepository.save(user);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param userEntity the user who apply for the loan
	 */
	@Override
	public void saveUser(UserEntity userEntity) {
		userRepository.save(userEntity);
	}

	private boolean isAdmin(Long usId) {
		return this.getUserEntityById(usId)
				.getRoles().stream().noneMatch(role -> role.getRole().equals("ADMIN"));
	}
}
