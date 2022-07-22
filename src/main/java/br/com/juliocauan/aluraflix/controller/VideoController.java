package br.com.juliocauan.aluraflix.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.aluraflix.infrastructure.mapper.application.VideoMapper;
import br.com.juliocauan.aluraflix.infrastructure.model.application.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructure.model.application.specification.VideoSpecification;
import br.com.juliocauan.aluraflix.infrastructure.repository.application.CategoryRepository;
import br.com.juliocauan.aluraflix.infrastructure.repository.application.VideoRepository;
import br.com.juliocauan.openapi.api.VideosApi;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;
import br.com.juliocauan.openapi.model.VideoPut;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class VideoController implements VideosApi {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<VideoGet> _addVideo(@Valid VideoPost videoPost) {
        VideoEntity video = videoMapper.postDtoToEntity(videoPost);
        video.setCategory(categoryRepository.findOneOrGetDefaultId(videoPost.getCategoryId()));
        VideoGet response = videoMapper.entityToGetDto(videoRepository.save(video));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<Page<VideoGet>> _findAllVideos(@Valid String search, Pageable pageable) {
        Page<VideoGet> response = videoRepository
                .findAll(VideoSpecification.hasInTitle(search), pageable)
                .map(videoMapper::entityToGetDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<VideoGet> _findVideoById(Integer videoId) {
        VideoGet response = videoMapper.entityToGetDto(videoRepository.findOneOrNotFound(videoId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<VideoGet> _updateVideo(Integer videoId, @Valid VideoPut videoPut) {
        VideoEntity videoNew = videoMapper.putDtoToEntity(videoPut);
        VideoEntity videoOld = videoRepository.findOneOrNotFound(videoId);
        videoMapper.update(videoNew, videoOld);
        VideoGet response = videoMapper.entityToGetDto(videoRepository.save(videoOld));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Void> _deleteVideo(Integer videoId) {
        videoRepository.remove(videoId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Page<VideoGet>> _findAllFreeVideos() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<VideoGet> response = videoRepository.findAll(pageable).map(videoMapper::entityToGetDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
