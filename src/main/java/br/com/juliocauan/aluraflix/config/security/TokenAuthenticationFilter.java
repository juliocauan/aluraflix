package br.com.juliocauan.aluraflix.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.juliocauan.aluraflix.infrastructure.model.auth.UserEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter{

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getToken(request);
        if(tokenService.isTokenValid(token)) authenticateUser(token);
        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String token) {
        UserEntity user = userRepository.findById(tokenService.getUserId(token)).get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) return null;
        return token.substring(7);
    }
    
}
