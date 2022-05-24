package com.nava.client.service;

import com.nava.client.entity.PasswordResetToken;
import com.nava.client.entity.User;
import com.nava.client.entity.VerificationToken;
import com.nava.client.model.UserModel;
import com.nava.client.repository.PasswordResetTokenRepository;
import com.nava.client.repository.UserRepository;
import com.nava.client.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository passwordResetToken;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;



    @Override
    public User registerUser(UserModel userModel) {  // client data of userModel object is collected to user object
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword())); // here we need to encode the password before it is stored in DB

         userRepository.save(user);
         return user;

    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {

        VerificationToken verificationToken = new VerificationToken(user,token);

        passwordResetToken.save(verificationToken);
    }

    // this is all about what ever token that we are getting is existed in database or not and expiration time

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = passwordResetToken.findByToken(token);
        if(verificationToken==null)
        {
            return "invalid token";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if(verificationToken.getExpirationTime().getTime()-cal.getTime().getTime()<=0)
        {
            passwordResetToken.delete(verificationToken);
            return "expired";

        }

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";

    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {

        VerificationToken verificationToken =
                passwordResetToken.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.save(verificationToken);

        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);

    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user,token);
        passwordResetTokenRepository.save(passwordResetToken);

    }

    @Override
    public String validatePasswordResetToken(String token) {

        //below code is similar to above method ValidateVerificationToken, so copied

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken==null)
        {
            return "invalid token";
        }

        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();

        if(passwordResetToken.getExpirationTime().getTime()-cal.getTime().getTime()<=0)
        {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";

        }

        return "valid";

    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);


    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}
