package br.com.juliocauan.aluraflix.domain.repository.auth;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.openapi.model.ProfileType;

public interface ProfileRepositoryDomain<E, ID> extends BaseRepository<E, ID>{
    E findByValue(ProfileType value);
}
