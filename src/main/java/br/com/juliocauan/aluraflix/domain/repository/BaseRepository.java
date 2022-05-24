package br.com.juliocauan.aluraflix.domain.repository;

import java.util.List;

public interface BaseRepository<E, ID> {
    
    List<E> findList();
    E findOne(ID id);
    E post(E entity);
    void remove(ID id);

}
