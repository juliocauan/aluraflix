package br.com.juliocauan.aluraflix.infrastructere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.juliocauan.aluraflix.infrastructere.mapper.config.BaseMapStruct;
import br.com.juliocauan.aluraflix.infrastructere.model.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructere.service.CategoriaService;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;
import br.com.juliocauan.openapi.model.VideoPut;

@Mapper(componentModel = "spring")
public abstract class VideoMapper implements BaseMapStruct<VideoEntity, VideoGet, VideoPost, VideoPut> {

    @Autowired
    protected CategoriaService categoriaService;

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract void update(VideoEntity newEntity, @MappingTarget VideoEntity oldEntity);
    
    //TODO
    @Override
    @Mapping(source = "categoria.id", target = "categoriaId")
    public abstract VideoGet entityToGetDto(VideoEntity entity);

    @Override
    @Mapping(target = "categoria", expression = "java(categoriaService.findById(postDto.getCategoriaId()))")
    public abstract VideoEntity postDtoToEntity(VideoPost postDto);

    @Override
    @Mapping(target = "categoria", expression = "java(categoriaService.findById(putDto.getCategoriaId()))")
    public abstract VideoEntity putDtoToEntity(VideoPut putDto);
}
