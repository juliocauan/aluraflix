package br.com.juliocauan.aluraflix.domain.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public interface BaseRepository<E, ID> {
    
    List<E> findList();
    //TODO Revisar Specification
    List<E> findList(Specification<E> spec);
    E findOne(ID id);
    E post(E entity);
    void remove(E entity);

}
