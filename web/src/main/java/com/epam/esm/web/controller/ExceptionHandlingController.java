package com.epam.esm.web.controller;

import com.epam.esm.service.exception.*;
import com.epam.esm.web.dto.ErrorDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.web.util.ErrorCodes.*;

@RestControllerAdvice
public class ExceptionHandlingController {
    private static final Logger logger = LogManager.getLogger();

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDto serverErrorHandler(Exception e) {
        logger.error(e);
        return new ErrorDto(INTERNAL_SERVER_ERROR_CODE, "Something happened. We are already looking at this");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorDto messageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        logger.error(e);
        return new ErrorDto(BAD_REQUEST_CODE, "Request sent can't be parsed");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto dtoValidationHandler(MethodArgumentNotValidException e) {
        logger.error(e);
        BindingResult result = e.getBindingResult();
        Optional<String> message = result
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .reduce(String::concat);
        return new ErrorDto(BAD_REQUEST_CODE, message.orElse("Field error"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorDto paramValidationHandler(MethodArgumentTypeMismatchException e) {
        logger.error(e);
        return new ErrorDto(BAD_REQUEST_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyUpdateParameterException.class)
    public ErrorDto paramValidationHandler(EmptyUpdateParameterException e) {
        logger.error(e);
        return new ErrorDto(BAD_REQUEST_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongPriceValueParameter.class)
    public ErrorDto paramValidationHandler(WrongPriceValueParameter e) {
        logger.error(e);
        return new ErrorDto(BAD_REQUEST_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongFilterOrderException.class)
    public ErrorDto paramValidationHandler(WrongFilterOrderException e) {
        logger.error(e);
        return new ErrorDto(BAD_REQUEST_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorDto paramValidationHandler(ConstraintViolationException e) {
        logger.error(e);
        ErrorDto errorDto = new ErrorDto(BAD_REQUEST_CODE, "Request parameter validation error");
        List<String> violations = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        errorDto.setParameterErrors(violations);
        return errorDto;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorDto handlerNotFoundHandler(NoHandlerFoundException e) {
        logger.error(e);
        return new ErrorDto(NOT_FOUND_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorDto resourceNotFoundHandler(EntityNotFoundException e) {
        logger.error(e);
        return new ErrorDto(NOT_FOUND_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorDto authenticationValidationHandler(AuthenticationException e) {
        logger.error(e);
        return new ErrorDto(UNAUTHORIZED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateEntityException.class)
    public ErrorDto paramValidationHandler(DuplicateEntityException e) {
        logger.error(e);
        return new ErrorDto(CONFLICT_CODE, e.getMessage());
    }
}
