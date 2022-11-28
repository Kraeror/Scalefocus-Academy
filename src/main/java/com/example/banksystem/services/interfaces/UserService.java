package com.example.banksystem.services.interfaces;

import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.models.requsts.LoanApplyRequest;
import com.example.banksystem.models.requsts.PasswordChangeRequest;
import com.example.banksystem.models.requsts.UserUpdateRequest;
import com.example.banksystem.models.responses.UserResponse;
import com.example.banksystem.repositories.UserRepository;

import java.util.List;

/**
 * A Service interface for {@link UserEntity}.
 * <p> Has methods for retrieval of {@link UserEntity} from a
 * database repository.
 */
public interface UserService {

    /**
     * Retrieves and maps a {@link UserResponse} with a given username from {@link UserRepository}.
     *
     * @param username the username of the selected UserEntity.
     * @return a {@link UserResponse}
     */
    UserResponse getUserByUsername(String username);

    /**
     * Retrieves and maps a list of {@link UserResponse} from {@link UserRepository}.
     *
     * @return a list of {@link UserResponse}
     */
    List<UserResponse> getAllUser();

    /**
     * Updates the details of an already existing {@link UserEntity}.
     * <p> Can update a single or multiple fields.
     *
     * @param userUpdateRequest an entity holding the parameters about the update of the fields.
     * @param id                the ID of the UserEntity which will be updated.
     * @return a {@link UserResponse} with the updated fields.
     */
    UserResponse updateUser(UserUpdateRequest userUpdateRequest, Long id, Long loggedInUser);

    /**
     * Retrieves and maps a {@link UserResponse} with a given ID from {@link UserRepository}.
     *
     * @param id the id of the selected UserEntity.
     * @return a {@link UserResponse}
     */
    UserResponse getUserById(Long id, Long loggedInUser);

    /**
     * Retrieves a {@link UserEntity} with a given ID from {@link UserRepository}.
     *
     * @param id the id of the selected UserEntity.
     * @return a {@link UserEntity}
     */
    UserEntity getUserEntityById(Long id);

    /**
     * Gets the currently logged in user who has account with that id.
     * @param accountId the id of the account
     * @return {@link UserEntity}
     */
    UserEntity getUserByAccountId(Long accountId);

    /**
     * Changes the user's password.
     * <p> Gets a {@link UserEntity} with the received userId from the repository, than checks if his password matches
     * the given currentPassword. Then checks if the new password and the confirmation match. If they do,
     * the {@link UserEntity} is saved with the new password.
     *
     * @param userId                the id of the user whose password will be changed/
     * @param passwordChangeRequest a {@link PasswordChangeRequest} with the parameters for the password change.
     * @return true if operation was successful, false if an error occurred.
     * @throws com.example.banksystem.exceptions.PasswordNotMatchingException when currentPassword does not match or
     *                                                                        the confirmation of the new password is wrong.
     */
    boolean changePassword(Long userId, PasswordChangeRequest passwordChangeRequest);

    /**
     * User apply for a loan
     *
     * @param userId the id of the currently logged in user.
     * @return {@link UserEntity} the currently logged in user who apply for loan approval
     * @throws {@link com.example.banksystem.exceptions.EntityNotFoundException} if the user does not exist.
     */
    UserEntity applyForLoan(Long userId);

    /**
     * If the user is approved for the loan his hasLoan status is changed to true
     *
     * @param isApproved is the loan approved
     * @param userEntity the user who apply for the loan
     */
    void updateHasLoanStatus(boolean isApproved, UserEntity userEntity);

    /**
     * Saves {@link UserEntity} to {@link UserRepository}.
     */
    void saveUser(UserEntity userEntity);

}
