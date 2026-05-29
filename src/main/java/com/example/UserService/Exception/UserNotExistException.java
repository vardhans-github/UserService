package com.example.UserService.Exception;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException(String s) {
        super(s);
    }
}
