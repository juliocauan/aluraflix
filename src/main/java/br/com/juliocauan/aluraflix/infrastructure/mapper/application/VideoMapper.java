package br.com.juliocauan.aluraflix.infrastructure.mapper.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.juliocauan.aluraflix.infrastructure.mapper.BaseMapStruct;
import br.com.juliocauan.aluraflix.infrastructure.mapper.MapperConfiguration;
import br.com.juliocauan.aluraflix.infrastructure.model.application.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.application.CategoryRepository;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;
import br.com.juliocauan.openapi.model.VideoPut;

@Mapper(config = MapperConfiguration.class)
public abstract class VideoMapper implements BaseMapStruct<VideoEntity, VideoGet, VideoPost, VideoPut> {

    @Autowired
    protected CategoryRepository categoryRepository;

    @Override
    @Mapping(target = "id", ignore = true)
    public abstract void update(VideoEntity newEntity, @MappingTarget VideoEntity oldEntity);

    @Override
    @Mapping(source = "category.id", target = "categoryId")
    public abstract VideoGet entityToGetDto(VideoEntity entity);

    @Override
    public abstract VideoEntity postDtoToEntity(VideoPost postDto);

    @Override
    @Mapping(target = "category", expression = "java(putDto.getCategoryId() == null ? null : categoryRepository.findOneOrBadRequest(putDto.getCategoryId()))")
    public abstract VideoEntity putDtoToEntity(VideoPut putDto);
}
