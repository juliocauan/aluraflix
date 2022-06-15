package br.com.juliocauan.aluraflix.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.aluraflix.infrastructure.mapper.VideoMapper;
import br.com.juliocauan.aluraflix.infrastructure.model.specification.VideoSpecification;
import br.com.juliocauan.aluraflix.infrastructure.service.VideoService;
import br.com.juliocauan.openapi.api.VideosApi;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;
import br.com.juliocauan.openapi.model.VideoPut;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class VideoController implements VideosApi {

    private final VideoService videoService;
    private final VideoMapper videoMapper;

    @Override
    public ResponseEntity<VideoGet> _addVideo(@Valid VideoPost videoPost) {
        return ResponseEntity.status(HttpStatus.CREATED).body(videoMapper.entityToGetDto(
                videoService.save(videoMapper.postDtoToEntity(videoPost))));
    }

    @Override
    public ResponseEntity<Page<VideoGet>> _findAllVideos(@Valid String search, Pageable pageable) {
        Page<VideoGet> response = videoService.find(VideoSpecification.hasInTitle(search), pageable)
                .map(videoMapper::entityToGetDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<VideoGet> _findVideoById(Integer videoId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                videoMapper.entityToGetDto(videoService.findOneOrNotFound(videoId)));
    }

    @Override
    public ResponseEntity<VideoGet> _updateVideo(Integer videoId, @Valid VideoPut videoPut) {
        return ResponseEntity.status(HttpStatus.OK).body(
                videoMapper.entityToGetDto(videoService.update(
                        videoId, videoMapper.putDtoToEntity(videoPut))));
    }

    @Override
    public ResponseEntity<Void> _deleteVideo(Integer videoId) {
        videoService.delete(videoId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
