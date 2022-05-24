package com.nava.client.config.event;

import com.nava.client.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {  // this class is for linking

    private User user;
    private String applicationUrl;  // this is for user, that the user should click to that link to activate

    public RegistrationCompleteEvent(User user, String applicationUrl) {  // constructor
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;

    }
}
