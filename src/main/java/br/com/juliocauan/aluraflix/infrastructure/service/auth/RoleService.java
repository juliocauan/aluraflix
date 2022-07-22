package br.com.juliocauan.aluraflix.infrastructure.service.auth;

import org.springframework.stereotype.Service;

import br.com.juliocauan.aluraflix.domain.repository.auth.RoleRepositoryDomain;
import br.com.juliocauan.aluraflix.domain.service.auth.RoleServiceDomain;
import br.com.juliocauan.aluraflix.infrastructure.model.auth.RoleEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.auth.RoleRepository;
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
