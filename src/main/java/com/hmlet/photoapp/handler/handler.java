package com.hmlet.photoapp.handler;

import com.hmlet.photoapp.exception.LinkedPhotoExistException;
import com.hmlet.photoapp.exception.UserAlreadyExistsException;
import com.hmlet.photoapp.exception.UserDoesNotExistsException;
import com.hmlet.photoapp.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class handler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleRuntimeErrors(Exception ex) {

        return new ResponseEntity<>(
                ErrorResponse.builder().errorMessage("An expected server side error occurred!").build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(LinkedPhotoExistException.class)
    public ResponseEntity<ErrorResponse> handleLinkedPhotoExistsOnUserDelete(LinkedPhotoExistException ex,
                                                                             WebRequest request) {

        ErrorResponse error = new ErrorResponse(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsOnUserAdd(UserAlreadyExistsException ex,
                                                                          WebRequest request) {

        ErrorResponse error = new ErrorResponse(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserDoesNotExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserDoesNotExistsOnUserGet(UserDoesNotExistsException ex,
                                                                          WebRequest request) {

        ErrorResponse error = new ErrorResponse(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
