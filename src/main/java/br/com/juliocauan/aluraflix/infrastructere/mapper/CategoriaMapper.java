package br.com.juliocauan.aluraflix.infrastructere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import br.com.juliocauan.aluraflix.infrastructere.mapper.config.BaseMapStruct;
import br.com.juliocauan.aluraflix.infrastructere.model.CategoriaEntity;
import br.com.juliocauan.openapi.model.CategoriaGet;
import br.com.juliocauan.openapi.model.CategoriaPost;
import br.com.juliocauan.openapi.model.CategoriaPut;

@Mapper(componentModel = "spring")
public interface CategoriaMapper extends BaseMapStruct<CategoriaEntity, CategoriaGet, CategoriaPost, CategoriaPut> {

    @Override
    //TODO revisar
    void update(CategoriaEntity newEntity, @MappingTarget CategoriaEntity oldEntity);

}
