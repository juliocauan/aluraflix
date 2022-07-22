package br.com.juliocauan.aluraflix.domain.repository.auth;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.openapi.model.RoleType;

public interface RoleRepositoryDomain<E, ID> extends BaseRepository<E, ID>{
    E findByValue(RoleType value);
}
