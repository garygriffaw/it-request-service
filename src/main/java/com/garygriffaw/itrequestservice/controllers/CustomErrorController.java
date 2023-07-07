package com.garygriffaw.itrequestservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindErrors(MethodArgumentNotValidException exception) {

        List errorList = exception.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    ResponseEntity handleUsernameAlreadyExistsException(UsernameAlreadyExistsException exception) {
        List<String> messages = new ArrayList<>();
        messages.add(exception.getMessage());

        List errorList = messages.stream()
                .map(message -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("username", message);
                    return errorMap;
                }).collect(Collectors.toList());

        return ResponseEntity.unprocessableEntity().body(errorList);
    }
}
