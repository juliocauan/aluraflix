package br.com.juliocauan.aluraflix.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.juliocauan.aluraflix.infrastructure.model.auth.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

    @Value("${flix.jwt.expiration}")
    private String expiration;

    @Value("${flix.jwt.secret}")
    private String secret;

    public String generateToken(Authentication auth) {
        UserEntity user = (UserEntity) auth.getPrincipal();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
            .setIssuer("Flix API")
            .setSubject(user.getId().toString())
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

}