package br.com.juliocauan.flix.domain.repository.application;

import br.com.juliocauan.flix.domain.repository.BaseRepository;

public interface CategoryRepositoryDomain<E, ID> extends BaseRepository<E, ID> {

    ID getDefaultId();

    default E findOneOrGetDefaultId(ID id){
        return (id == null ? findOneOrNotFound(getDefaultId()) : findOneOrBadRequest(id));
    }

}
