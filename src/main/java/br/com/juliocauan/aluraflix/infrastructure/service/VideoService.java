package br.com.juliocauan.aluraflix.infrastructure.service;

import org.springframework.stereotype.Service;

import br.com.juliocauan.aluraflix.domain.mapper.ServiceMapper;
import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.domain.service.VideoServiceDomain;
import br.com.juliocauan.aluraflix.infrastructure.mapper.VideoMapper;
import br.com.juliocauan.aluraflix.infrastructure.model.domain.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.VideoRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VideoService extends VideoServiceDomain<VideoEntity, Integer> {

    private final VideoRepository videoRepositoryJpa;
    private final VideoMapper videoMapper;

    @Override
    protected BaseRepository<VideoEntity, Integer> getRepository() {
        return videoRepositoryJpa;
    }

    @Override
    protected ServiceMapper<VideoEntity> getMapper() {
        return videoMapper;
    }

    @Override
    protected String getClassName() {
        return VideoEntity.class.getName();
    }

}
