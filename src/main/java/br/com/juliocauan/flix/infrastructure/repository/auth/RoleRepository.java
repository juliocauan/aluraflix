package br.com.juliocauan.flix.infrastructure.repository.auth;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.flix.domain.repository.auth.RoleRepositoryDomain;
import br.com.juliocauan.flix.infrastructure.model.auth.RoleEntity;
import br.com.juliocauan.flix.infrastructure.repository.BaseJpaRepository;

@Repository
public interface RoleRepository extends BaseJpaRepository<RoleEntity, Short>, RoleRepositoryDomain<RoleEntity, Short>{

    @Override
    default String getClassName() {
        return RoleEntity.class.getName();
    }
    
}
