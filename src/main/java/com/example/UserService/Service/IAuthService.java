package com.example.UserService.Service;

import com.example.UserService.Exception.UserAlreadyExistException;
import com.example.UserService.Models.User;

import com.example.UserService.dtos.UserToken;


public interface IAuthService {
    User signup(String name, String email, String password) throws UserAlreadyExistException;

    UserToken login(String email, String password);

    Boolean validateToken(String token);

    User getUserById(Long Id);
}