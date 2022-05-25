package br.com.juliocauan.aluraflix.infrastructere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.juliocauan.aluraflix.infrastructere.mapper.config.BaseMapStruct;
import br.com.juliocauan.aluraflix.infrastructere.model.CategoryEntity;
import br.com.juliocauan.openapi.model.CategoryGet;
import br.com.juliocauan.openapi.model.CategoryPost;
import br.com.juliocauan.openapi.model.CategoryPut;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapStruct<CategoryEntity, CategoryGet, CategoryPost, CategoryPut> {

    @Override
    @Mapping(target = "id", ignore = true)
    void update(CategoryEntity newEntity, CategoryEntity oldEntity);

}
