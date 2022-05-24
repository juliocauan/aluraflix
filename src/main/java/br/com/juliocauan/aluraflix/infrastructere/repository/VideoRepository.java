package br.com.juliocauan.aluraflix.infrastructere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.infrastructere.model.VideoEntity;

@Repository
public interface VideoRepository extends BaseRepository<VideoEntity, Integer>, JpaRepository<VideoEntity, Integer>{

    @Override
    default List<VideoEntity> findList() {
        return findAll();
    }

    @Override
    default VideoEntity findOne(Integer id) {
        return findById(id).orElse(null);
    }

    @Override
    default VideoEntity post(VideoEntity entity) {
        return save(entity);
    }

    @Override
    default void remove(VideoEntity entity) {
        delete(entity);
    }

}
