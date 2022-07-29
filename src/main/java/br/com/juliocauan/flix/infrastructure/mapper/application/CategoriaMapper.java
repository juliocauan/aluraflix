package br.com.juliocauan.flix.infrastructure.mapper.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.juliocauan.flix.infrastructure.mapper.BaseMapStruct;
import br.com.juliocauan.flix.infrastructure.mapper.MapperConfiguration;
import br.com.juliocauan.flix.infrastructure.model.application.CategoryEntity;
import br.com.juliocauan.openapi.model.CategoryGet;
import br.com.juliocauan.openapi.model.CategoryPost;
import br.com.juliocauan.openapi.model.CategoryPut;

@Mapper(config = MapperConfiguration.class)
public interface CategoriaMapper extends BaseMapStruct<CategoryEntity, CategoryGet, CategoryPost, CategoryPut> {

    @Override
    @Mapping(target = "id", ignore = true)
    void update(CategoryEntity newEntity, @MappingTarget CategoryEntity oldEntity);

}
