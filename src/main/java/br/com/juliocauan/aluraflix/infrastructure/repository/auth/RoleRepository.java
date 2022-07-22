package br.com.juliocauan.aluraflix.infrastructure.repository.auth;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.domain.repository.auth.RoleRepositoryDomain;
import br.com.juliocauan.aluraflix.infrastructure.model.auth.RoleEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.BaseJpaRepository;

@Repository
public interface RoleRepository extends BaseJpaRepository<RoleEntity, Short>, RoleRepositoryDomain<RoleEntity, Short>{

    @Override
    default String getClassName() {
        return RoleEntity.class.getName();
    }
    
}
