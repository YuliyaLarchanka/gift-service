package com.epam.esm.web.controller;

import com.epam.esm.service.exception.TagNotFoundException;
import com.epam.esm.web.dto.ErrorDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ExceptionHandlingController {
    private static final Logger logger = LogManager.getLogger();

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDto serverErrorHandler(Exception e) {
        logger.error(e);
        return new ErrorDto("Something happened. We are already looking at this");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> responseStatusExceptionHandler(ResponseStatusException e) {
        logger.error(e);
        ErrorDto errorDto = new ErrorDto(e.getReason());
        return ResponseEntity.status(e.getStatus()).body(errorDto);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto dtoValidationHandler(MethodArgumentNotValidException e) {
        logger.error(e);
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return new ErrorDto("Validation error", fieldErrors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorDto paramValidationHandler(MethodArgumentTypeMismatchException e) {
        logger.error(e);
        return new ErrorDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorDto paramValidationHandler(ConstraintViolationException e) {
        logger.error(e);
        ErrorDto errorDto = new ErrorDto("Request parameter validation error");
        List<String> violations = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        errorDto.setParameterErrors(violations);
        return errorDto;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TagNotFoundException.class)
    public ErrorDto TagNotFoundHandler(TagNotFoundException e) {
        logger.error(e);
        return new ErrorDto(e.getMessage());
    }
}
