package com.nava.client.controller;

import com.nava.client.config.event.RegistrationCompleteEvent;
import com.nava.client.entity.User;
import com.nava.client.entity.VerificationToken;
import com.nava.client.model.PasswordModel;
import com.nava.client.model.UserModel;
import com.nava.client.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher publisher; // learn about this


    @PostMapping("/clientRegister")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) // here we are sending client data to the userModel object, that's why it is created
    {
        User user = userService.registerUser(userModel);

        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));

        return "success";

    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "user verified sucessfully";
        } else {
            return "Bad User";
        }
    }


    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {

        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);

        User user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrl(request), verificationToken); // calling method
        return "Verification Link Sent";

    }

    @GetMapping("/resendVerifyToken")
    private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();


        log.info("Click the link to verify your account : {}", url);

    }


    // Rest  reset password method, we will reset password, based on the token that we created

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);

        }
        return url;

    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) {
        String result = userService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "invalid token";
        }

        // if my token is valid, i need to reset my password

        Optional<User> user = userService.getUserByPasswordResetToken(token);

        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return "Password Reset Sucessfully";
        }
        else
        {
            return "Invalid Token ";
        }


    }


    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel)
    {
         User user = userService.findUserByEmail(passwordModel.getEmail());
         if(!userService.checkIfValidOldPassword(user,passwordModel.getOldPassword()))
         {
             return "Invalid Old Password";
         }
         else {

             // save new password functionality
             userService.changePassword(user, passwordModel.getNewPassword());
             return "Password Changed Sucessfully";

         }
    }


    private String passwordResetTokenMail(User user, String applicationUrl, String token) {

        String url = applicationUrl + "/savePassword?token=" + token;


        log.info("Click the link to Reset Your Password : {}", url);

        return url;

    }


    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

    }



}
