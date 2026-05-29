package com.example.UserService.Models;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Session extends  BaseModel{

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    private User user;


}
