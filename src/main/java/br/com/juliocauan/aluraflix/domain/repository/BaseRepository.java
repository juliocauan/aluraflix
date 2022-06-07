package br.com.juliocauan.aluraflix.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface BaseRepository<E, ID> {
    
    //TODO Revisar Specification
    Page<E> getPage(Specification<E> spec, Pageable pageable);
    E findOne(ID id);
    E post(E entity);
    void remove(E entity);

}
