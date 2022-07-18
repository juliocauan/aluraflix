package br.com.juliocauan.aluraflix.domain.repository.auth;

import java.util.Optional;

import br.com.juliocauan.aluraflix.domain.model.auth.User;

public interface UserRepositoryDomain<E, ID> {
    Optional<? extends User> findByEmail(String username);
}
