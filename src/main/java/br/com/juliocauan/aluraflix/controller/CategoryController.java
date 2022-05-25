package br.com.juliocauan.aluraflix.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.aluraflix.infrastructere.mapper.CategoryMapper;
import br.com.juliocauan.aluraflix.infrastructere.service.CategoryService;
import br.com.juliocauan.openapi.api.CategoriesApi;
import br.com.juliocauan.openapi.model.CategoryGet;
import br.com.juliocauan.openapi.model.CategoryPost;
import br.com.juliocauan.openapi.model.CategoryPut;

@RestController
public class CategoryController implements CategoriesApi {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public ResponseEntity<CategoryGet> _addCategory(@Valid CategoryPost categoryPost) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            categoryMapper.entityToGetDto(categoryService.save(categoryMapper.postDtoToEntity(categoryPost))));
    }

    @Override
    public ResponseEntity<Void> _deleteCategory(Integer categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<List<CategoryGet>> _findAllCategories() {
        List<CategoryGet> categoryList = new ArrayList<>();
        categoryService.findAll().forEach(
            category -> categoryList.add(categoryMapper.entityToGetDto(category)));
        return ResponseEntity.status(HttpStatus.OK).body(categoryList);
    }

    @Override
    public ResponseEntity<CategoryGet> _findCategoryById(Integer categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            categoryMapper.entityToGetDto(categoryService.findById(categoryId)));
    }

    @Override
    public ResponseEntity<Void> _updateCategory(@Valid CategoryPut categoryPut, Integer categoryId) {
        categoryService.update(categoryId, categoryMapper.putDtoToEntity(categoryPut));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
