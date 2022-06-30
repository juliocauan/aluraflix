package br.com.juliocauan.aluraflix.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.aluraflix.config.security.TokenService;
import br.com.juliocauan.openapi.api.AuthApi;
import br.com.juliocauan.openapi.model.LoginForm;
import br.com.juliocauan.openapi.model.Token;
import br.com.juliocauan.openapi.model.TokenType;

@RestController
public class AuthenticationController implements AuthApi {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @Override
    public ResponseEntity<Token> _authenticate(@Valid LoginForm loginForm) {
        Authentication auth = authManager.authenticate(parseAsToken(loginForm));
        return ResponseEntity.status(HttpStatus.OK).body(
            new Token()
                .token(tokenService.generateToken(auth))
                .type(TokenType.BEARER));
    }

    private UsernamePasswordAuthenticationToken parseAsToken(@Valid LoginForm loginForm) {
        return new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPswd());
    }
    
}
