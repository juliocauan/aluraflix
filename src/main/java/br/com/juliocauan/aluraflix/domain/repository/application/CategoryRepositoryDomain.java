package br.com.juliocauan.aluraflix.domain.repository.application;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;

public interface CategoryRepositoryDomain<E, ID> extends BaseRepository<E, ID> {

    ID getDefaultId();

    default E findOneOrGetDefaultId(ID id){
        return (id == null ? findOneOrNotFound(getDefaultId()) : findOneOrBadRequest(id));
    }

}
