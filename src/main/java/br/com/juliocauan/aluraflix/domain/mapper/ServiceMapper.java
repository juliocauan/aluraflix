package br.com.juliocauan.aluraflix.domain.mapper;

public interface ServiceMapper<E> {
    void update(E newEntity, E oldEntity);   
}
