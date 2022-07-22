package br.com.juliocauan.aluraflix.infrastructure.mapper.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.juliocauan.aluraflix.infrastructure.mapper.BaseMapStruct;
import br.com.juliocauan.aluraflix.infrastructure.mapper.MapperConfiguration;
import br.com.juliocauan.aluraflix.infrastructure.model.application.CategoryEntity;
import br.com.juliocauan.openapi.model.CategoryGet;
import br.com.juliocauan.openapi.model.CategoryPost;
import br.com.juliocauan.openapi.model.CategoryPut;

@Mapper(config = MapperConfiguration.class)
public interface CategoriaMapper extends BaseMapStruct<CategoryEntity, CategoryGet, CategoryPost, CategoryPut> {

    @Override
    @Mapping(target = "id", ignore = true)
    void update(CategoryEntity newEntity, @MappingTarget CategoryEntity oldEntity);

}
