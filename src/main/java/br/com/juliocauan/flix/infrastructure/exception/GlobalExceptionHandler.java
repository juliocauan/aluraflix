package br.com.juliocauan.flix.infrastructure.exception;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.juliocauan.openapi.model.Error;
import br.com.juliocauan.openapi.model.ErrorField;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private Error responseError;

    private Error init(int code, Exception ex){
        Error error = new Error();
        error.setCode(code);
        error.setTrace(stackTraceString(ex.getStackTrace()));
        error.setMessage(ex.getMessage());
        return error;
    }

    private String stackTraceString(StackTraceElement[] elements){
        return org.apache.commons.lang3.StringUtils.join(elements, "\n");
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> entityNotFoundError(EntityNotFoundException ex){
        responseError = init(101, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }
    
    //OPENAPI VALIDATION ERROR
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        responseError = init(201, ex);
        ex.getFieldErrors().forEach(error -> {
            ErrorField e = new ErrorField();
            e.setField(error.getObjectName() + "." + error.getField());
            e.setMessage(error.getDefaultMessage());
            e.setCode(error.getCode());
            responseError.addFieldListItem(e);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    //POST/PUT VALIDATION INTEGRITY ERROR SOLVED BY MAPPER
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> validationExceptionError(ValidationException ex){
        responseError = init(202, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    //POST/PUT VALIDATION INTEGRITY ERROR NOT SOLVED BY MAPPER
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> dataIntegrityError(DataIntegrityViolationException ex){
        responseError = init(301, ex);
        responseError.setMessage(ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> loginError(BadCredentialsException ex){
        responseError = init(401, ex);
        responseError.message("Invalid User or Password!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseError);
    }

}
