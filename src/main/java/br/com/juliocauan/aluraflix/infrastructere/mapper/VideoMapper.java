package br.com.juliocauan.aluraflix.infrastructere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.juliocauan.aluraflix.infrastructere.mapper.config.BaseMapStruct;
import br.com.juliocauan.aluraflix.infrastructere.model.VideoEntity;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;
import br.com.juliocauan.openapi.model.VideoPut;

@Mapper(componentModel = "spring")
public interface VideoMapper extends BaseMapStruct<VideoEntity, VideoGet, VideoPost, VideoPut> {

    @Override
    @Mapping(target = "id", ignore = true)
    void update(VideoEntity newEntity, @MappingTarget VideoEntity oldEntity);
    
    //TODO
    @Override
    @Mapping(source = "categoria.id", target = "categoriaId")
    VideoGet entityToGetDto(VideoEntity entity);

    @Override
    //@Mapping(source = "categoriaId", target = "categoria.id")
    VideoEntity postDtoToEntity(VideoPost postDto);

    @Override
    VideoEntity putDtoToEntity(VideoPut putDto);
}
