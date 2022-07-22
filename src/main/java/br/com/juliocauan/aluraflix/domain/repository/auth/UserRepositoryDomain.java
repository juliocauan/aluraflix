package br.com.juliocauan.aluraflix.domain.repository.auth;

import java.util.Optional;

public interface UserRepositoryDomain<E, ID> {
    Optional<E> findByEmail(String username);
}
