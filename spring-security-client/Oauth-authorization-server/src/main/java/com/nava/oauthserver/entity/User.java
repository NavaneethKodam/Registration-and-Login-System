package com.nava.oauthserver.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @Column(length = 60)
    private String password;
    private String role;   // what is the role of a user
    private boolean enabled = false;  // by default the user would be disabled




}
