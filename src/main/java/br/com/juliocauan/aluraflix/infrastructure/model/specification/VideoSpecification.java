package br.com.juliocauan.aluraflix.infrastructure.model.specification;

import javax.validation.Valid;

import org.springframework.data.jpa.domain.Specification;

import br.com.juliocauan.aluraflix.infrastructure.model.CategoriaEntity;
import br.com.juliocauan.aluraflix.infrastructure.model.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructure.model.VideoEntity_;

public class VideoSpecification {

    public static Specification<VideoEntity> hasInTitle(@Valid String search) {
        return (root, query, cb) -> {
            if (search == null)
                return null;
            return cb.like(root.get(VideoEntity_.titulo), cb.literal('%' +  search + '%'));
        };
    }

    //TODO implementar
    public static Specification<VideoEntity> isInCategoria(CategoriaEntity findOneOrNotFound) {
        return null;
    }

}
