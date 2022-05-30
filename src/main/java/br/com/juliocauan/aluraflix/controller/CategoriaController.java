package br.com.juliocauan.aluraflix.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.aluraflix.infrastructere.mapper.CategoriaMapper;
import br.com.juliocauan.aluraflix.infrastructere.service.CategoriaService;
import br.com.juliocauan.openapi.api.CategoriasApi;
import br.com.juliocauan.openapi.model.CategoriaGet;
import br.com.juliocauan.openapi.model.CategoriaPost;
import br.com.juliocauan.openapi.model.CategoriaPut;

@RestController
public class CategoriaController implements CategoriasApi {

    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaController(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
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
                categoriaMapper.entityToGetDto(categoriaService.findById(categoriaId)));
    }

    @Override
    public ResponseEntity<CategoriaGet> _updateCategoria(@Valid CategoriaPut categoriaPut, Integer categoriaId) {
        categoriaService.update(categoriaId, categoriaMapper.putDtoToEntity(categoriaPut));
        return ResponseEntity.status(HttpStatus.OK).body(
                categoriaMapper.entityToGetDto(categoriaMapper.putDtoToEntity(categoriaPut)));
    }

}
