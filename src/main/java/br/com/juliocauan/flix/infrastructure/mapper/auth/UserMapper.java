package br.com.juliocauan.flix.infrastructure.mapper.auth;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.juliocauan.flix.infrastructure.mapper.BaseMapStruct;
import br.com.juliocauan.flix.infrastructure.mapper.MapperConfiguration;
import br.com.juliocauan.flix.infrastructure.model.auth.UserEntity;
import br.com.juliocauan.openapi.model.UserGet;
import br.com.juliocauan.openapi.model.UserPost;
import br.com.juliocauan.openapi.model.UserPut;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper extends BaseMapStruct<UserEntity, UserGet, UserPost, UserPut>{
    
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void update(UserEntity newEntity, @MappingTarget UserEntity oldEntity);

    @Override
    UserGet entityToGetDto(UserEntity entity);

    @Override
    UserEntity postDtoToEntity(UserPost postDto);

    @Override
    @Mapping(target = "roles", ignore = true)
    UserEntity putDtoToEntity(UserPut putDto);

}
