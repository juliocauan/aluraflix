package br.com.juliocauan.aluraflix.infrastructure.mapper.auth;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.juliocauan.aluraflix.infrastructure.mapper.config.BaseMapStruct;
import br.com.juliocauan.aluraflix.infrastructure.mapper.config.MapperConfiguration;
import br.com.juliocauan.aluraflix.infrastructure.model.auth.UserEntity;
import br.com.juliocauan.openapi.model.UserGet;
import br.com.juliocauan.openapi.model.UserPost;
import br.com.juliocauan.openapi.model.UserPut;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper extends BaseMapStruct<UserEntity, UserGet, UserPost, UserPut>{
    
    @Override
    @Mapping(target = "id", ignore = true)
    void update(UserEntity newEntity, @MappingTarget UserEntity oldEntity);

}
