package br.com.juliocauan.aluraflix.infrastructere.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.juliocauan.aluraflix.domain.mapper.ServiceMapper;
import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.domain.service.BaseService;
import br.com.juliocauan.aluraflix.infrastructere.mapper.VideoMapper;
import br.com.juliocauan.aluraflix.infrastructere.model.CategoriaEntity;
import br.com.juliocauan.aluraflix.infrastructere.model.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructere.repository.VideoRepository;
import br.com.juliocauan.openapi.model.VideoGet;

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

    public List<VideoGet> findAllByCategoria(CategoriaEntity categoria) {
        List<VideoGet> videoListByCategoria = new ArrayList<>();
        List<VideoEntity> list = this.findAll().stream()
                .filter(video -> video.getCategoria().getId() == categoria.getId())
                .collect(Collectors.toList());
        list.forEach(video -> videoListByCategoria.add(videoMapper.entityToGetDto(video)));
        return videoListByCategoria;
    }

}
