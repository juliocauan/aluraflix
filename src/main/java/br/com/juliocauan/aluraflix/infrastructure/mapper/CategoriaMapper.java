package br.com.juliocauan.aluraflix.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.juliocauan.aluraflix.infrastructure.mapper.config.BaseMapStruct;
import br.com.juliocauan.aluraflix.infrastructure.model.CategoriaEntity;
import br.com.juliocauan.openapi.model.CategoriaGet;
import br.com.juliocauan.openapi.model.CategoriaPost;
import br.com.juliocauan.openapi.model.CategoriaPut;

@Mapper(componentModel = "spring")
public interface CategoriaMapper extends BaseMapStruct<CategoriaEntity, CategoriaGet, CategoriaPost, CategoriaPut> {

    @Override
    @Mapping(target = "id", ignore = true)
    void update(CategoriaEntity newEntity, @MappingTarget CategoriaEntity oldEntity);

}
