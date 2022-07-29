package br.com.juliocauan.flix.infrastructure.model.application.specification;

import javax.validation.Valid;

import org.springframework.data.jpa.domain.Specification;

import br.com.juliocauan.flix.infrastructure.model.application.VideoEntity_;
import br.com.juliocauan.flix.infrastructure.model.application.CategoryEntity;
import br.com.juliocauan.flix.infrastructure.model.application.VideoEntity;

public class VideoSpecification {

    public static Specification<VideoEntity> hasInTitle(@Valid String search) {
        return (root, query, cb) -> {
            if (search == null)
                return null;
            return cb.like(root.get(VideoEntity_.title), cb.literal('%' +  search + '%'));
        };
    }

    public static Specification<VideoEntity> isInCategoria(CategoryEntity category) {
        return (root, query, cb) -> {
            return cb.equal(root.get(VideoEntity_.category), category);
        };
    }

}
