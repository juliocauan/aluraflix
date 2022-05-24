package br.com.juliocauan.aluraflix.infrastructere.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.juliocauan.aluraflix.infrastructere.mapper.VideoMapper;
import br.com.juliocauan.aluraflix.infrastructere.model.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructere.repository.VideoRepository;

@Service
@Transactional
public class VideoService {

    private final VideoRepository videoRepositoryJpa;
    private final VideoMapper videoMapper;

    @Autowired
    public VideoService(VideoRepository videoRepositoryJpa, VideoMapper videoMapper) {
        this.videoRepositoryJpa = videoRepositoryJpa;
        this.videoMapper = videoMapper;
    }

    public List<VideoEntity> findAll() {
        return videoRepositoryJpa.findAll();
    }

    public VideoEntity findOne(Integer videoId) {
        VideoEntity video = videoRepositoryJpa.findById(videoId).orElse(null);
        if(video == null)
            throw new EntityNotFoundException(String.format("Unable to find %s with id %d",
                VideoEntity.class.getPackageName(), videoId));
        return video;
    }

    public VideoEntity save(VideoEntity video) {
        return videoRepositoryJpa.save(video);
    }

    public void update(Integer videoId, VideoEntity newVideoEntity) {
        VideoEntity oldVideoEntity = findOne(videoId);
        videoMapper.update(newVideoEntity, oldVideoEntity);
        save(oldVideoEntity);
    }

    public void delete(Integer videoId) {
        videoRepositoryJpa.delete(findOne(videoId));
    }

}
