package com.example.customerdebtservice.shared.controlleradvices;

import com.example.customerdebtservice.shared.exceptions.ResourceExistsException;
import com.example.customerdebtservice.shared.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class ControllerAdvices {
    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ResourceExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String resourceExistsHandler(ResourceExistsException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder("Error occurred");
        ex.getConstraintViolations().forEach(constraintViolation ->
                errorMessage.append(". ").append(constraintViolation.getMessage()));
        log.error("ConstraintViolationException: " + errorMessage);
        return errorMessage.toString();
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "Error occurred. Cannot deserialize HTTP message";
        log.error("HttpMessageNotReadableException: " + errorMessage);
        return errorMessage;
    }
}
