package br.com.juliocauan.aluraflix.infrastructure.repository.auth;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.infrastructure.model.auth.ProfileEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.config.BaseJpaRepository;

@Repository
public interface ProfileRepository extends BaseJpaRepository<ProfileEntity, Short> {

    @Override
    default String getClassName() {
        return ProfileEntity.class.getName();
    }
    
}
