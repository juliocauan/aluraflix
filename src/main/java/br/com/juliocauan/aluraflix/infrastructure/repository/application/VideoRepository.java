package br.com.juliocauan.aluraflix.infrastructure.repository.application;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.infrastructure.model.application.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.BaseJpaRepository;

@Repository
public interface VideoRepository extends BaseJpaRepository<VideoEntity, Integer>{
    
    @Override
    default String getClassName() {
        return VideoEntity.class.getName();
    }

}
