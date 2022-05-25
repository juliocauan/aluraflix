package br.com.juliocauan.aluraflix.infrastructere.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.juliocauan.aluraflix.domain.mapper.ServiceMapper;
import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.domain.service.BaseService;
import br.com.juliocauan.aluraflix.infrastructere.model.CategoryEntity;

@Service
@Transactional
public class CategoryService extends BaseService<CategoryEntity, Integer> {

    //private final CategoryRepository categoryRepository;

    @Override
    protected BaseRepository<CategoryEntity, Integer> getRepository() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected ServiceMapper<CategoryEntity> getMapper() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getClassName() {
        // TODO Auto-generated method stub
        return null;
    }

}
