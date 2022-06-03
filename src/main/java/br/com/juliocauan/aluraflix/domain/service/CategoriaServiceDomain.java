package br.com.juliocauan.aluraflix.domain.service;

import br.com.juliocauan.aluraflix.domain.service.config.BaseService;

public abstract class CategoriaServiceDomain<E, ID> extends BaseService<E, ID> {

    protected abstract ID getDefaultId();
    
    public final E findOneOrDefault(ID id){
        return (id == null ? findOneOrNotFound(getDefaultId()) : findOneOrNotFound(id));
    }

}
