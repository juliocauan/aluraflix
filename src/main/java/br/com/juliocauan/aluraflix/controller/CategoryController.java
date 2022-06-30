package br.com.juliocauan.aluraflix.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.aluraflix.infrastructure.mapper.CategoriaMapper;
import br.com.juliocauan.aluraflix.infrastructure.mapper.VideoMapper;
import br.com.juliocauan.aluraflix.infrastructure.model.domain.specification.VideoSpecification;
import br.com.juliocauan.aluraflix.infrastructure.service.CategoryService;
import br.com.juliocauan.aluraflix.infrastructure.service.VideoService;
import br.com.juliocauan.openapi.api.CategoriesApi;
import br.com.juliocauan.openapi.model.CategoryGet;
import br.com.juliocauan.openapi.model.CategoryPost;
import br.com.juliocauan.openapi.model.CategoryPut;
import br.com.juliocauan.openapi.model.VideoGet;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class CategoryController implements CategoriesApi {

    private final CategoryService categoryService;
    private final CategoriaMapper categoryMapper;
    private final VideoService videoService;
    private final VideoMapper videoMapper;

    @Override
    public ResponseEntity<CategoryGet> _addCategory(@Valid CategoryPost categoryPost) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                categoryMapper.entityToGetDto(categoryService.save(categoryMapper.postDtoToEntity(categoryPost))));
    }

    @Override
    public ResponseEntity<Page<CategoryGet>> _findAllCategories(Pageable pageable) {
        Page<CategoryGet> response = categoryService.findAll(pageable).map(categoryMapper::entityToGetDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<CategoryGet> _findCategoryById(Integer categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                categoryMapper.entityToGetDto(categoryService.findOneOrNotFound(categoryId)));
    }

    @Override
    public ResponseEntity<CategoryGet> _updateCategory(Integer categoriaId, @Valid CategoryPut categoryPut) {
        return ResponseEntity.status(HttpStatus.OK).body(
                categoryMapper.entityToGetDto(categoryService.update(
                        categoriaId, categoryMapper.putDtoToEntity(categoryPut))));
    }

    @Override
    public ResponseEntity<Void> _deleteCategory(Integer categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Page<VideoGet>> _findVideosByCategory(Integer categoryId, Pageable pageable) {
        Page<VideoGet> response = videoService
                .find(VideoSpecification.isInCategoria(categoryService.findOneOrNotFound(categoryId)), pageable)
                .map(videoMapper::entityToGetDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
