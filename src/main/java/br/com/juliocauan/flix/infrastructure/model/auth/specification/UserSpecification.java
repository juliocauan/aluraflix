package br.com.juliocauan.flix.infrastructure.model.auth.specification;

import javax.validation.Valid;

import org.springframework.data.jpa.domain.Specification;

import br.com.juliocauan.flix.infrastructure.model.auth.UserEntity_;
import br.com.juliocauan.flix.infrastructure.model.auth.UserEntity;

public class UserSpecification {
    
    public static Specification<UserEntity> hasInEmail(@Valid String search) {
        return (root, query, cb) -> {
            if (search == null)
                return null;
            return cb.like(root.get(UserEntity_.email), cb.literal('%' +  search + '%'));
        };
    }

}
