package com.moa.moa3.controller;

import com.moa.moa3.exception.jwt.InvalidTokenRequestException;
import com.moa.moa3.exception.oauth.DuplicateLoginFailureException;
import com.moa.moa3.exception.oauth.NotFoundProviderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.pattern.PatternParseException;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 400
    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            NotFoundProviderException.class,
            ResponseStatusException.class,
            MissingServletRequestParameterException.class,
    })
    @ResponseBody
    public ResponseEntity<Object> handleBadRequestException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 401
    @ExceptionHandler({
        BadCredentialsException.class,
        InvalidTokenRequestException.class,
    })
    @ResponseBody
    public ResponseEntity<Object> handleUnAuthorizedException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

//     409 -> 이미 가입된 계정이 있을 경우.
    @ExceptionHandler({
            DuplicateLoginFailureException.class,
    })
    @ResponseBody
    public ResponseEntity<Object> handleCustomException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    // 500
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleIllegalArgumentException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
