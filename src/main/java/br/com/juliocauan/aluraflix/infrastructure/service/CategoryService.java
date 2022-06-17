package br.com.juliocauan.aluraflix.infrastructure.service;

import org.springframework.stereotype.Service;

import br.com.juliocauan.aluraflix.domain.mapper.ServiceMapper;
import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.domain.service.CategoryServiceDomain;
import br.com.juliocauan.aluraflix.infrastructure.mapper.CategoriaMapper;
import br.com.juliocauan.aluraflix.infrastructure.model.domain.CategoryEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.domain.CategoryRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService extends CategoryServiceDomain<CategoryEntity, Integer> {

    private final CategoryRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    protected BaseRepository<CategoryEntity, Integer> getRepository() {
        return categoriaRepository;
    }

    @Override
    protected ServiceMapper<CategoryEntity> getMapper() {
        return categoriaMapper;
    }

    @Override
    protected String getClassName() {
        return CategoryEntity.class.getName();
    }

    @Override
    protected Integer getDefaultId() {
        return 1;
    }

}
