package br.com.juliocauan.aluraflix.domain.service.config;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.jpa.domain.Specification;

import br.com.juliocauan.aluraflix.domain.mapper.ServiceMapper;
import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;

public abstract class BaseService<E, ID> {
    
    protected abstract BaseRepository<E, ID> getRepository();
    protected abstract ServiceMapper<E> getMapper();
    protected abstract String getClassName();

    public final List<E> findAll() {
        return getRepository().findList();
    }

    public final List<E> findAll(Specification<E> spec) {
        return getRepository().findList(spec);
    }

    private final E findOneOrNull(ID id){
        return getRepository().findOne(id);
    }

    public final E findOneOrNotFound(ID id) {
        E entity = findOneOrNull(id);
        if(entity == null)
            throw new EntityNotFoundException(String.format("Unable to find %s with id %d",
                getClassName(), id));
        return entity;
    }

    public final E save(E entity) {
        return getRepository().post(entity);
    }

    public final E update(ID id, E newEntity) {
        E oldEntity = findOneOrNotFound(id);
        getMapper().update(newEntity, oldEntity);
        return save(oldEntity);
    }

    public final void delete(ID id) {
        getRepository().remove(findOneOrNotFound(id));
    }

}
