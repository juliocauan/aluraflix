package br.com.juliocauan.aluraflix.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import br.com.juliocauan.aluraflix.infrastructure.mapper.config.BaseMapStruct;
import br.com.juliocauan.aluraflix.infrastructure.model.CategoryEntity;
import br.com.juliocauan.openapi.model.CategoryGet;
import br.com.juliocauan.openapi.model.CategoryPost;
import br.com.juliocauan.openapi.model.CategoryPut;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoriaMapper extends BaseMapStruct<CategoryEntity, CategoryGet, CategoryPost, CategoryPut> {

    @Override
    @Mapping(target = "id", ignore = true)
    void update(CategoryEntity newEntity, @MappingTarget CategoryEntity oldEntity);

}
