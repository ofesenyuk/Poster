package com.sf.poster.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.MethodArgumentNotValidException
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus


/**
 *
 * @author OFeseniuk
 */
@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    static ResponseEntity<AppError> catchResourceNotFoundException(MethodArgumentNotValidException e) {
        String errorsStr = e.getFieldErrors()        
        .collect{"${it.getField()} - ${it.getDefaultMessage()}"}
        .sort()
        .join(", ")
        new ResponseEntity<>(new AppError(statusCode: HttpStatus.BAD_REQUEST.value(), message: errorsStr), HttpStatus.BAD_REQUEST)
    }
    
    @ExceptionHandler
    static ResponseEntity<AppError> catchIllegalArgumentException(IllegalArgumentException e) {
        new ResponseEntity<>(new AppError(statusCode: HttpStatus.NOT_FOUND.value(), message: e.message), HttpStatus.NOT_FOUND)
    }
    
    @ExceptionHandler
    static ResponseEntity<AppError> catchIllegalAccessException(IllegalAccessException e) {
        new ResponseEntity<>(new AppError(statusCode: HttpStatus.FORBIDDEN.value(), message: e.message), HttpStatus.FORBIDDEN)
    }
}

