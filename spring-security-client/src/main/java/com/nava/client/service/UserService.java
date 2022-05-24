package com.nava.client.service;

import com.nava.client.entity.User;
import com.nava.client.entity.VerificationToken;
import com.nava.client.model.UserModel;

import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(User user, String token);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

   public User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean checkIfValidOldPassword(User user, String oldPassword);
}
