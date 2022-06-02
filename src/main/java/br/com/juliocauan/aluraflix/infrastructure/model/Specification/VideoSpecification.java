package br.com.juliocauan.aluraflix.infrastructure.model.Specification;

import javax.validation.Valid;

import org.springframework.data.jpa.domain.Specification;

import br.com.juliocauan.aluraflix.infrastructure.model.VideoEntity;
import br.com.juliocauan.aluraflix.infrastructure.model.VideoEntity_;

public class VideoSpecification {

    public static Specification<VideoEntity> hasInTitle(@Valid String search) {
        return (root, query, cb) -> {
            if (search == null) return null;
            return cb.like(
                    cb.function("locate_str", String.class, root.get(VideoEntity_.titulo)),
                    cb.function("locate_str", String.class, cb.literal("%" + search + "%")));
        };
    }

}
