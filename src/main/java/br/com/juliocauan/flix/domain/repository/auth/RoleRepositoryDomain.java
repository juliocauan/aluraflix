package br.com.juliocauan.flix.domain.repository.auth;

import br.com.juliocauan.flix.domain.repository.BaseRepository;
import br.com.juliocauan.openapi.model.RoleType;

public interface RoleRepositoryDomain<E, ID> extends BaseRepository<E, ID>{
    E findByValue(RoleType value);
}
