package br.com.juliocauan.aluraflix.infrastructure.model.auth.specification;

import javax.validation.Valid;

import org.springframework.data.jpa.domain.Specification;

import br.com.juliocauan.aluraflix.infrastructure.model.auth.UserEntity;
import br.com.juliocauan.aluraflix.infrastructure.model.auth.UserEntity_;

public class UserSpecification {
    
    public static Specification<UserEntity> hasInEmail(@Valid String search) {
        return (root, query, cb) -> {
            if (search == null)
                return null;
            return cb.like(root.get(UserEntity_.email), cb.literal('%' +  search + '%'));
        };
    }

}
