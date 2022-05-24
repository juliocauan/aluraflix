package br.com.juliocauan.aluraflix.infrastructere.handler;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.juliocauan.openapi.model.ErrorDto;
import br.com.juliocauan.openapi.model.ErrorFieldDto;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ErrorDto responseError = new ErrorDto();

    private ErrorDto init(int code, Exception ex){
        ErrorDto error = new ErrorDto();
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
        responseError = init(1001, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        responseError = init(2001, ex);
        ex.getFieldErrors().forEach(error -> {
            ErrorFieldDto e = new ErrorFieldDto();
            e.setField(error.getObjectName() + "." + error.getField());
            e.setMessage(error.getDefaultMessage());
            e.setCode(error.getCode());
            responseError.addFieldListItem(e);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

}
