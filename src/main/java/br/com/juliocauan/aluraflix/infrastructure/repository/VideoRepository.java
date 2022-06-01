package br.com.juliocauan.aluraflix.infrastructure.repository;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.infrastructure.model.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.config.BaseJpaRepository;

@Repository
public interface VideoRepository extends BaseJpaRepository<VideoEntity, Integer>{
}
