package br.com.juliocauan.aluraflix.infrastructere.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.juliocauan.aluraflix.domain.mapper.ServiceMapper;
import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.domain.service.BaseService;
import br.com.juliocauan.aluraflix.infrastructere.mapper.CategoriaMapper;
import br.com.juliocauan.aluraflix.infrastructere.model.CategoriaEntity;
import br.com.juliocauan.aluraflix.infrastructere.repository.CategoriaRepository;
import br.com.juliocauan.openapi.model.VideoGet;

@Service
@Transactional
public class CategoriaService extends BaseService<CategoriaEntity, Integer> {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final VideoService videoService;

    @Autowired
    public CategoriaService(CategoriaRepository categoryRepository, CategoriaMapper categoryMapper,
            VideoService videoService) {
        this.categoriaRepository = categoryRepository;
        this.categoriaMapper = categoryMapper;
        this.videoService = videoService;
    }

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

    public List<VideoGet> findVideoListByCategoria(Integer categoriaId) {
        return videoService.findAllByCategoria(this.findById(categoriaId));
    }

}
