package com.example.banksystem.models.requsts;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordChangeRequest {

    @NotBlank(message = "Current password cannot be blank!")
    @Size(min = 5,max = 30, message = "Password's length should be between 5 and 30 symbols")
    private String currentPassword;

    @NotBlank(message = "New password cannot be blank!")
    @Size(min = 5,max = 30, message = "Password's length should be between 5 and 30 symbols")
    private String newPassword;

    @NotBlank(message = "New password cannot be blank!")
    @Size(min = 5,max = 30, message = "Password's length should be between 5 and 30 symbols")
    private String confirmNewPassword;

    public PasswordChangeRequest() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public PasswordChangeRequest setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public PasswordChangeRequest setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public PasswordChangeRequest setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
        return this;
    }
}
