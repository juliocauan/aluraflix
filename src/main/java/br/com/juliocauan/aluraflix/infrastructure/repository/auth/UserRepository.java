package br.com.juliocauan.aluraflix.infrastructure.repository.auth;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.infrastructure.model.auth.UserEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.config.BaseJpaRepository;

@Repository
public interface UserRepository extends BaseJpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String username);
}
