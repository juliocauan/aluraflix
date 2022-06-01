package br.com.juliocauan.aluraflix.infrastructure.model.Specification;

import javax.validation.Valid;

import org.springframework.data.jpa.domain.Specification;

import br.com.juliocauan.aluraflix.infrastructure.model.VideoEntity;

public class VideoSpecification {

    //TODO implementar
    public static Specification<VideoEntity> name(@Valid String search) {
        return (root, query, cb) -> {
            if (search == null) return null;
            return cb.equal(root.get(search), search);
        };
    }
    
}
