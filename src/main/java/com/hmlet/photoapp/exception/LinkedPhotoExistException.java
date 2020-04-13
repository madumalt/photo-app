package com.hmlet.photoapp.exception;

public class LinkedPhotoExistException extends RuntimeException {

    public LinkedPhotoExistException(Long userId, String userName) {
        super("Cannot delete user: [" + userId + ", " + userName + "] due to existing associated photos.");
    }
}
