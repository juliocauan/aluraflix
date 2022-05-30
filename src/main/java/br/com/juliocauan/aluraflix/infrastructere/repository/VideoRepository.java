package br.com.juliocauan.aluraflix.infrastructere.repository;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.infrastructere.model.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructere.repository.config.BaseJpaRepository;

@Repository
public interface VideoRepository extends BaseJpaRepository<VideoEntity, Integer>{
}
