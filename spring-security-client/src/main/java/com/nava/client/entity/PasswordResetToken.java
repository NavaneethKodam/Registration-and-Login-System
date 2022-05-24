package com.nava.client.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {

    // below code is copied from VerficationToken class ...beacause here also we need to verify the tokens
    // expiration time is 10 minutes
    private static final int EXPIRATION_TIME =10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))

    private User user;

    // let's create constructor

    public PasswordResetToken(User user,String token)
    {
        super();
        this.user = user;
        this.token=token;
        this.expirationTime = calculateExpirationData(EXPIRATION_TIME);
    }

    public PasswordResetToken(String token)
    {
        super();
        this.token = token;
        this.expirationTime = calculateExpirationData(EXPIRATION_TIME);

    }

    public static PasswordResetToken findByToken(String token) {
        return null;
    }

    private Date calculateExpirationData(int expirationTime)
    {
        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,expirationTime);
        return new Date(calendar.getTime().getTime());

    }


}


