package com.kpl.libraryManagement.demo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                            WebRequest webRequest) {
            ErrorDetails errorDetails = new ErrorDetails(
                    LocalDateTime.now(),
                    exception.getMessage(),
                    webRequest.getDescription(false),
                    "RESOURCE_NOT_FOUND"
            );
            return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(ResourceAlreadyExistsException.class)
        public ResponseEntity<ErrorDetails> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception,
                                                                                 WebRequest webRequest) {
            ErrorDetails errorDetails = new ErrorDetails(
                    LocalDateTime.now(),
                    exception.getMessage(),
                    webRequest.getDescription(false),
                    "RESOURCE_ALREADY_EXISTS"
            );
            return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                                  WebRequest webRequest) {
            ErrorDetails errorDetails = new ErrorDetails(
                    LocalDateTime.now(),
                    exception.getMessage(),
                    webRequest.getDescription(false),
                    "INTERNAL_SERVER_ERROR"
            );
            return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                      HttpHeaders headers,
                                                                      HttpStatus status,
                                                                      WebRequest request) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                errors.put(fieldName, message);
            });

            ValidationErrorDetails errorDetails = new ValidationErrorDetails(
                    LocalDateTime.now(),
                    "Validation Failed",
                    request.getDescription(false),
                    "VALIDATION_ERROR",
                    errors
            );

            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

}
