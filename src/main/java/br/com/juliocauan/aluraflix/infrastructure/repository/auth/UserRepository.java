package br.com.juliocauan.aluraflix.infrastructure.repository.auth;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.domain.repository.auth.UserRepositoryDomain;
import br.com.juliocauan.aluraflix.infrastructure.model.auth.UserEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.BaseJpaRepository;

@Repository
public interface UserRepository extends BaseJpaRepository<UserEntity, Long>, UserRepositoryDomain<UserEntity, Long> {

    @Override
    default String getClassName() {
        return UserEntity.class.getName();
    }
    
}
