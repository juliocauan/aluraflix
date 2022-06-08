package br.com.juliocauan.aluraflix.domain.service;

import br.com.juliocauan.aluraflix.domain.service.config.BaseService;

public abstract class CategoriaServiceDomain<E, ID> extends BaseService<E, ID> {

    protected abstract ID getDefaultId();
    
    public final E findOneOrGetDefaultId(ID id){
        return (id == null ? findOneOrNotFound(getDefaultId()) : findOneOrBadRequest(id));
    }

}
