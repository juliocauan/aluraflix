package br.com.juliocauan.aluraflix.infrastructure.repository.config;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;

@NoRepositoryBean
public interface BaseJpaRepository<E, ID> extends BaseRepository<E, ID>, JpaRepository<E, ID>, JpaSpecificationExecutor<E>{

    @Override
    default Page<E> getPage(Pageable pageable) {
        return findAll(pageable);
    }

    @Override
    default Page<E> getPage(Specification<E> spec, Pageable pageable) {
        return findAll(spec, pageable);
    }

    @Override
    default E findOne(ID id) {
        return id == null ? null : findById(id).orElse(null);
    }

    @Override
    default E post(E entity) {
        return save(entity);
    }

    @Override
    default void remove(E entity) {
        delete(entity);
    }

}
