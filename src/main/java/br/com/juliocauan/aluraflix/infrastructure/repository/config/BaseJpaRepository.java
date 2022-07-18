package br.com.juliocauan.aluraflix.infrastructure.repository.config;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;

@NoRepositoryBean
public interface BaseJpaRepository<E, ID> extends BaseRepository<E, ID>, JpaRepository<E, ID>, JpaSpecificationExecutor<E>{

    @Override
    default void remove(ID id) {
        delete(findOneOrNotFound(id));
    }

    @Override
    default E findOneOrBadRequest(ID id) {
        E entity = findOneOrNull(id);
        if(entity == null)
            throw new ValidationException(String.format("POST/PUT method: Unable to find %s with id %s",
                getClassName(), id));
        return entity;
    }

    @Override
    default E findOneOrNotFound(ID id) {
        E entity = findOneOrNull(id);
        if(entity == null)
            throw new EntityNotFoundException(String.format("GET/DELETE method: Unable to find %s with id %s",
                getClassName(), id));
        return entity;
    }

    @Override
    default E findOneOrNull(ID id) {
        return id == null ? null : findById(id).orElse(null);
    }

}
