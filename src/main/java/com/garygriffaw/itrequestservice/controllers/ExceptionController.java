package com.garygriffaw.itrequestservice.controllers;

import com.garygriffaw.itrequestservice.exceptions.ForbiddenException;
import com.garygriffaw.itrequestservice.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity handleForbiddenException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
