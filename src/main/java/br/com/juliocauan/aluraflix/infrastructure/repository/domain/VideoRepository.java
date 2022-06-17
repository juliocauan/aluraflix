package br.com.juliocauan.aluraflix.infrastructure.repository.domain;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.infrastructure.model.domain.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.config.BaseJpaRepository;

@Repository
public interface VideoRepository extends BaseJpaRepository<VideoEntity, Integer>{
}
