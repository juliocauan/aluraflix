package br.com.juliocauan.flix.infrastructure.service.auth;

import org.springframework.stereotype.Service;

import br.com.juliocauan.flix.domain.repository.auth.RoleRepositoryDomain;
import br.com.juliocauan.flix.domain.service.auth.RoleServiceDomain;
import br.com.juliocauan.flix.infrastructure.model.auth.RoleEntity;
import br.com.juliocauan.flix.infrastructure.repository.auth.RoleRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleService implements RoleServiceDomain<RoleEntity, Short> {

    private final RoleRepository roleRepository;

    @Override
    public RoleRepositoryDomain<RoleEntity, Short> getRepository() {
        return roleRepository;
    }

    @Override
    public Short getClientId() {
        return 2;
    }
    
}
