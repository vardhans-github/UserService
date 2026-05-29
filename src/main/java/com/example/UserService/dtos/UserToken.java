package com.example.UserService.dtos;

import com.example.UserService.Models.User;

public class UserToken {
    private User user;
    private String token;

    public UserToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}