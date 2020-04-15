package com.hmlet.photoapp.exception;

public class PhotoDoesNotExistsException extends RuntimeException {

    public PhotoDoesNotExistsException(Long Id) {
        super("Photo: [Id: " + Id.toString() + "] does not exists! " +
                "If you want to publish new photo do not specify the photo Id");
    }
}
