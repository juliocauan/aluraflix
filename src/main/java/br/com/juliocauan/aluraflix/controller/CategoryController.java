package br.com.juliocauan.aluraflix.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.aluraflix.infrastructure.mapper.CategoriaMapper;
import br.com.juliocauan.aluraflix.infrastructure.mapper.VideoMapper;
import br.com.juliocauan.aluraflix.infrastructure.model.application.CategoryEntity;
import br.com.juliocauan.aluraflix.infrastructure.model.application.specification.VideoSpecification;
import br.com.juliocauan.aluraflix.infrastructure.repository.application.CategoryRepository;
import br.com.juliocauan.aluraflix.infrastructure.repository.application.VideoRepository;
import br.com.juliocauan.openapi.api.CategoriesApi;
import br.com.juliocauan.openapi.model.CategoryGet;
import br.com.juliocauan.openapi.model.CategoryPost;
import br.com.juliocauan.openapi.model.CategoryPut;
import br.com.juliocauan.openapi.model.VideoGet;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class CategoryController implements CategoriesApi {

    private final CategoryRepository categoryRepository;
    private final CategoriaMapper categoryMapper;
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    @Override
    public ResponseEntity<CategoryGet> _addCategory(@Valid CategoryPost categoryPost) {
        CategoryEntity category = categoryMapper.postDtoToEntity(categoryPost);
        CategoryGet response = categoryMapper.entityToGetDto(categoryRepository.save(category));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<Page<CategoryGet>> _findAllCategories(Pageable pageable) {
        Page<CategoryGet> response = categoryRepository.findAll(pageable).map(categoryMapper::entityToGetDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<CategoryGet> _findCategoryById(Integer categoryId) {
        CategoryGet response = categoryMapper.entityToGetDto(categoryRepository.findOneOrNotFound(categoryId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<CategoryGet> _updateCategory(Integer categoryId, @Valid CategoryPut categoryPut) {
        CategoryEntity categoryNew = categoryMapper.putDtoToEntity(categoryPut);
        CategoryEntity categoryOld = categoryRepository.findOneOrNotFound(categoryId);
        categoryMapper.update(categoryNew, categoryOld);
        CategoryGet response = categoryMapper.entityToGetDto(categoryRepository.save(categoryOld));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Void> _deleteCategory(Integer categoryId) {
        categoryRepository.remove(categoryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Page<VideoGet>> _findVideosByCategory(Integer categoryId, Pageable pageable) {
        Page<VideoGet> response = videoRepository
                .findAll(VideoSpecification.isInCategoria(categoryRepository.findOneOrNotFound(categoryId)), pageable)
                .map(videoMapper::entityToGetDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
