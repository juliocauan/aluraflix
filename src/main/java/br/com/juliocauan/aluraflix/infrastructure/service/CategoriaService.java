package br.com.juliocauan.aluraflix.infrastructure.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.juliocauan.aluraflix.domain.mapper.ServiceMapper;
import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.domain.service.CategoriaServiceDomain;
import br.com.juliocauan.aluraflix.infrastructure.mapper.CategoriaMapper;
import br.com.juliocauan.aluraflix.infrastructure.model.CategoriaEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.CategoriaRepository;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CategoriaService extends CategoriaServiceDomain<CategoriaEntity, Integer> {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    protected BaseRepository<CategoriaEntity, Integer> getRepository() {
        return categoriaRepository;
    }

    @Override
    protected ServiceMapper<CategoriaEntity> getMapper() {
        return categoriaMapper;
    }

    @Override
    protected String getClassName() {
        return CategoriaEntity.class.getName();
    }

    @Override
    protected Integer getDefaultId() {
        return 1;
    }

}
