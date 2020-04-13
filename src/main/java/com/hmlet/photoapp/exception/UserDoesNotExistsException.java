package com.hmlet.photoapp.exception;

public class UserDoesNotExistsException extends RuntimeException {

    public UserDoesNotExistsException(String userName) {
        super("User: [" + userName + "] does not exists!");
    }
}
