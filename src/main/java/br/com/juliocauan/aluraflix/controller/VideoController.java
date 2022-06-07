package br.com.juliocauan.aluraflix.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.aluraflix.infrastructure.mapper.VideoMapper;
import br.com.juliocauan.aluraflix.infrastructure.service.VideoService;
import br.com.juliocauan.openapi.api.VideosApi;
import br.com.juliocauan.openapi.model.VideoGet;
import br.com.juliocauan.openapi.model.VideoPost;
import br.com.juliocauan.openapi.model.VideoPut;

@RestController
public class VideoController implements VideosApi {

    private final VideoService videoService;
    private final VideoMapper videoMapper;

    @Autowired
    public VideoController(VideoService videoService, VideoMapper videoMapper) {
        this.videoService = videoService;
        this.videoMapper = videoMapper;
    }

    //TODO Apagar
    // @Override
    // public ResponseEntity<List<VideoGet>> _findAllVideos(@Valid String search) {
    //     List<VideoGet> videoList = new ArrayList<>();
    //     videoService.findAll(
    //             VideoSpecification.hasInTitle(search)).forEach(
    //                     video -> videoList.add(videoMapper.entityToGetDto(video)));
    //     return ResponseEntity.status(HttpStatus.OK).body(videoList);
    // }

    @Override
    public ResponseEntity<Page<VideoGet>> _findAllVideos(@Valid String search, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<VideoGet> _addVideo(@Valid VideoPost videoPost) {
        return ResponseEntity.status(HttpStatus.CREATED).body(videoMapper.entityToGetDto(
                videoService.save(videoMapper.postDtoToEntity(videoPost))));
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
