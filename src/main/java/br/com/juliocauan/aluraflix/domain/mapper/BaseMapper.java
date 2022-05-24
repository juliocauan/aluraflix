package br.com.juliocauan.aluraflix.domain.mapper;

public interface BaseMapper<E, GET, POST, PUT> {
    GET entityToGetDto(E entity);
    E postDtoToEntity(POST postDto);
    E putDtoToEntity(PUT putDto);
    void update(E newEntity, E oldEntity);    
}
