package br.com.juliocauan.aluraflix.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.openapi.api.AuthApi;
import br.com.juliocauan.openapi.model.LoginForm;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthenticationController implements AuthApi {

    private final AuthenticationManager authManager;

    @Override
    public ResponseEntity<Void> _authenticate(@Valid LoginForm loginForm) {
        // TODO Auto-generated method stub
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
}
