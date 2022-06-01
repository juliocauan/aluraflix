package br.com.juliocauan.aluraflix.infrastructure.repository.config;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;

@NoRepositoryBean
public interface BaseJpaRepository<E, ID> extends BaseRepository<E, ID>, JpaRepository<E, ID>, JpaSpecificationExecutor<E>{

    @Override
    default List<E> findList() {
        return findAll();
    }

    @Override
    default List<E> findList(Specification<E> spec) {
        return findAll(spec, Sort.unsorted());
    }

    @Override
    default E findOne(ID id) {
        return findById(id).orElse(null);
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
