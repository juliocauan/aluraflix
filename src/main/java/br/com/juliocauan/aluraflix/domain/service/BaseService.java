package br.com.juliocauan.aluraflix.domain.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import br.com.juliocauan.aluraflix.domain.mapper.ServiceMapper;
import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;

public abstract class BaseService<E, ID> {
    
    protected abstract BaseRepository<E, ID> getRepository();
    protected abstract ServiceMapper<E> getMapper();
    protected abstract String getClassName();

    public List<E> findAll() {
        return getRepository().findList();
    }

    public E findById(ID id) {
        E entity = getRepository().findOne(id);
        if(entity == null)
            throw new EntityNotFoundException(String.format("Unable to find %s with id %d",
                getClassName(), id));
        return entity;
    }

    public E save(E entity) {
        try {
            return getRepository().post(entity);
        } catch (Exception e) {
            //TODO: handle exception
        }
        return null;
    }

    public void update(ID id, E newEntity) {
        E oldEntity = findById(id);
        getMapper().update(newEntity, oldEntity);
        save(oldEntity);
    }

    public void delete(ID id) {
        getRepository().remove(findById(id));
    }

}
