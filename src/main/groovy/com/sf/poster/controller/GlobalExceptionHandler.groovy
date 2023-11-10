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
    public ResponseEntity<AppError> catchResourceNotFoundException(MethodArgumentNotValidException e) {
        String errorsStr = e.getFieldErrors()        
        .stream()
        .map{"${it.getField()} - ${it.getDefaultMessage()}"}
        .sort()
        .join(", ");
        new ResponseEntity<>(new AppError(statusCode: HttpStatus.BAD_REQUEST.value(), message: errorsStr), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler
    public ResponseEntity<AppError> catchIllegalArgumentException(IllegalArgumentException e) {
        new ResponseEntity<>(new AppError(statusCode: HttpStatus.NOT_FOUND.value(), message: e.message), HttpStatus.NOT_FOUND);
    }
}

