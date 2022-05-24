package br.com.juliocauan.aluraflix.infrastructere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.infrastructere.model.VideoEntity;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, Integer>{

}
