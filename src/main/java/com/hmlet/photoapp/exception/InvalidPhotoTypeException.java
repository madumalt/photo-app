package com.hmlet.photoapp.exception;

public class InvalidPhotoTypeException extends RuntimeException {
    public InvalidPhotoTypeException(String invalidPhotoType) {

        super("Given photo type (" + invalidPhotoType + ") is in the request parameter is wrong! Must be one of " +
                "all/published/draft");
    }
}
