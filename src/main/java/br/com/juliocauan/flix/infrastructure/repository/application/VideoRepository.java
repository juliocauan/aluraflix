package br.com.juliocauan.flix.infrastructure.repository.application;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.flix.infrastructure.model.application.VideoEntity;
import br.com.juliocauan.flix.infrastructure.repository.BaseJpaRepository;

@Repository
public interface VideoRepository extends BaseJpaRepository<VideoEntity, Integer>{
    
    @Override
    default String getClassName() {
        return VideoEntity.class.getName();
    }

}
