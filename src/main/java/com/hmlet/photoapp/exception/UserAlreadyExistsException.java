package com.hmlet.photoapp.exception;

public class UserAlreadyExistsException extends RuntimeException  {

    public UserAlreadyExistsException(Long userId, String userName) {
        super("User: [" + userId + ", " + userName + "] already exists!");
    }
}
