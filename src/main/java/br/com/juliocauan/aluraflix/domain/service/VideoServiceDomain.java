package br.com.juliocauan.aluraflix.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import br.com.juliocauan.aluraflix.domain.model.Categoria;
import br.com.juliocauan.aluraflix.domain.model.Video;
import br.com.juliocauan.aluraflix.domain.service.config.BaseService;

public abstract class VideoServiceDomain<E, ID> extends BaseService<E, ID> {
    
    public final List<E> findAllByCategoria(Categoria categoria) {
        List<E> list = this.findAll().stream()
                .filter(video -> ((Video) video).getCategoria().equals(categoria))
                .collect(Collectors.toList());
        return list;
    }

}
