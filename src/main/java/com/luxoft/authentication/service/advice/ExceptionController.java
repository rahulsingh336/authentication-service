package com.luxoft.authentication.service.advice;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public @ResponseBody ErrorDTO handleExpiredJWTException(ExpiredJwtException ex, HttpServletResponse response){
        return new ErrorDTO (401, "Token is Expired, Please re-login");
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public @ResponseBody ErrorDTO handleBadCredentialsException(BadCredentialsException ex, HttpServletResponse response){
        return new ErrorDTO (401, "Please enter correct credentials");
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public @ResponseBody ErrorDTO handleBadCredentialsException(InsufficientAuthenticationException ex, HttpServletResponse response){
        return new ErrorDTO (401, "Token is not valid or expired");
    }
}
