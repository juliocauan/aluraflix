package br.com.juliocauan.aluraflix.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.aluraflix.infrastructure.mapper.CategoriaMapper;
import br.com.juliocauan.aluraflix.infrastructure.mapper.VideoMapper;
import br.com.juliocauan.aluraflix.infrastructure.service.CategoriaService;
import br.com.juliocauan.aluraflix.infrastructure.service.VideoService;
import br.com.juliocauan.openapi.api.CategoriasApi;
import br.com.juliocauan.openapi.model.CategoriaGet;
import br.com.juliocauan.openapi.model.CategoriaPost;
import br.com.juliocauan.openapi.model.CategoriaPut;
import br.com.juliocauan.openapi.model.VideoGet;

@RestController
public class CategoriaController implements CategoriasApi {

    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;
    private final VideoService videoService;
    private final VideoMapper videoMapper;

    @Autowired
    public CategoriaController(CategoriaService categoriaService, CategoriaMapper categoriaMapper,
            VideoService videoService, VideoMapper videoMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
        this.videoService = videoService;
        this.videoMapper = videoMapper;
    }

    @Override
    public ResponseEntity<CategoriaGet> _addCategoria(@Valid CategoriaPost categoriaPost) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                categoriaMapper.entityToGetDto(categoriaService.save(categoriaMapper.postDtoToEntity(categoriaPost))));
    }

    @Override
    public ResponseEntity<Void> _deleteCategoria(Integer categoriaId) {
        categoriaService.delete(categoriaId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<List<CategoriaGet>> _findAllCategorias() {
        List<CategoriaGet> categoriaList = new ArrayList<>();
        categoriaService.findAll().forEach(
                categoria -> categoriaList.add(categoriaMapper.entityToGetDto(categoria)));
        return ResponseEntity.status(HttpStatus.OK).body(categoriaList);
    }

    @Override
    public ResponseEntity<CategoriaGet> _findCategoriaById(Integer categoriaId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                categoriaMapper.entityToGetDto(categoriaService.findOneOrNotFound(categoriaId)));
    }

    @Override
    public ResponseEntity<CategoriaGet> _updateCategoria(@Valid CategoriaPut categoriaPut, Integer categoriaId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                categoriaMapper.entityToGetDto(categoriaService.update(
                        categoriaId, categoriaMapper.putDtoToEntity(categoriaPut))));
    }

    @Override
    public ResponseEntity<List<VideoGet>> _findVideoListByCategoria(Integer categoriaId) {
        List<VideoGet> response = new ArrayList<>();
        videoService.findAllByCategoria(categoriaService.findOneOrNotFound(categoriaId))
                .forEach(video -> response.add(videoMapper.entityToGetDto(video)));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
