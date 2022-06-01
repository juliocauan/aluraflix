package br.com.juliocauan.aluraflix.infrastructure.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.juliocauan.aluraflix.domain.mapper.ServiceMapper;
import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.domain.service.BaseService;
import br.com.juliocauan.aluraflix.infrastructure.mapper.VideoMapper;
import br.com.juliocauan.aluraflix.infrastructure.model.CategoriaEntity;
import br.com.juliocauan.aluraflix.infrastructure.model.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.VideoRepository;

@Service
@Transactional
public class VideoService extends BaseService<VideoEntity, Integer> {

    private final VideoRepository videoRepositoryJpa;
    private final VideoMapper videoMapper;

    @Autowired
    public VideoService(VideoRepository videoRepositoryJpa, VideoMapper videoMapper) {
        this.videoRepositoryJpa = videoRepositoryJpa;
        this.videoMapper = videoMapper;
    }

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

    //TODO passar isso pro domínio
    public List<VideoEntity> findAllByCategoria(CategoriaEntity categoria) {
        List<VideoEntity> list = this.findAll().stream()
                .filter(video -> video.getCategoria().equals(categoria))
                .collect(Collectors.toList());
        return list;
    }

}
