package br.com.juliocauan.aluraflix.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<Void> _deleteCategory(Integer categoryId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<List<CategoryGet>> _findAllCategories() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<CategoryGet> _findCategoryById(Integer categoryId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<Void> _updateCategory(@Valid CategoryPut categoryPut, Integer categoryId) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
