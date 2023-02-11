package com.accenture.codingtest.springbootcodingtest.entity;

import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author Tan Oliver, GfK
 */
@Entity
@Table
public class User {

    @Id
    @GeneratedValue
    UUID id;

    @NonNull
    String username;

    @NonNull
    String password;

    public User() {

    }

    public void replace(User newUser) {
        this.username = newUser.username;
        this.password = newUser.password;
    }

    public User(UUID id, @NonNull String username, @NonNull String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
