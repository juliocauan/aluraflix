package br.com.juliocauan.aluraflix.config.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.juliocauan.aluraflix.infrastructure.model.auth.UserEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.auth.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> user = userRepository.findByEmail(username);
		if (user.isPresent()) return user.get();
		throw new UsernameNotFoundException("Dados inválidos!");
    }
    
}
