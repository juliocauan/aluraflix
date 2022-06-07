package br.com.juliocauan.aluraflix.domain.service;

import org.springframework.data.domain.Page;

import br.com.juliocauan.aluraflix.domain.model.Categoria;
import br.com.juliocauan.aluraflix.domain.service.config.BaseService;

public abstract class VideoServiceDomain<E, ID> extends BaseService<E, ID> {

    // TODO apagar
    // public final List<E> findAllByCategoria(Categoria categoria) {
    // List<E> list = this.findAll().stream()
    // .filter(video -> ((Video) video).getCategoria().equals(categoria))
    // .collect(Collectors.toList());
    // return list;
    // }

    //TODO implementar
    public final Page<E> findAllByCategoria(Categoria categoria) {
        return null;
    }

}
