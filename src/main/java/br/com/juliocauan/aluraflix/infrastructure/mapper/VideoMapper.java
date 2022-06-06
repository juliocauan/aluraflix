package br.com.juliocauan.aluraflix.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.juliocauan.aluraflix.infrastructure.mapper.config.BaseMapStruct;
import br.com.juliocauan.aluraflix.infrastructure.model.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructure.service.CategoriaService;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;
import br.com.juliocauan.openapi.model.VideoPut;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class VideoMapper implements BaseMapStruct<VideoEntity, VideoGet, VideoPost, VideoPut> {

    @Autowired
    protected CategoriaService categoriaService;

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract void update(VideoEntity newEntity, @MappingTarget VideoEntity oldEntity);

    @Override
    @Mapping(source = "categoria.id", target = "categoriaId")
    public abstract VideoGet entityToGetDto(VideoEntity entity);

    @Override
    @Mapping(target = "categoria", expression = "java(categoriaService.findOneOrDefault(postDto.getCategoriaId()))")
    public abstract VideoEntity postDtoToEntity(VideoPost postDto);

    //TODO estudar
    @Override
    @Mapping(target = "categoria", expression = "java(putDto.getCategoriaId() == null ? null : categoriaService.findOneOrNotFound(putDto.getCategoriaId()))")
    public abstract VideoEntity putDtoToEntity(VideoPut putDto);
}
