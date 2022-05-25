package br.com.juliocauan.aluraflix.infrastructere.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.juliocauan.aluraflix.domain.mapper.ServiceMapper;
import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.domain.service.BaseService;
import br.com.juliocauan.aluraflix.infrastructere.mapper.CategoryMapper;
import br.com.juliocauan.aluraflix.infrastructere.model.CategoryEntity;
import br.com.juliocauan.aluraflix.infrastructere.repository.CategoryRepository;

@Service
@Transactional
public class CategoryService extends BaseService<CategoryEntity, Integer> {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    protected BaseRepository<CategoryEntity, Integer> getRepository() {
        return categoryRepository;
    }

    @Override
    protected ServiceMapper<CategoryEntity> getMapper() {
        return categoryMapper;
    }

    @Override
    protected String getClassName() {
        return CategoryEntity.class.getName();
    }

}
