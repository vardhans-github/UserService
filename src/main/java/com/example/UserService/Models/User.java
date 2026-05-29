package com.example.UserService.Models;
import com.example.UserService.dtos.UserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.security.SecureRandom;
import java.util.List;

@Entity
public class User  extends  BaseModel{

    private String userName;
    private String email;
    private String password;

    @ManyToMany
    List<Role> roles;

    public String getUserName() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return password;
    }

    public void setPasswordHash(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public UserDTO convertToUserDTO(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(this.getId());
        userDTO.setName(this.userName);
        userDTO.setEmail(this.email);
        userDTO.setRoles(this.roles);
        return userDTO;
    }
}
