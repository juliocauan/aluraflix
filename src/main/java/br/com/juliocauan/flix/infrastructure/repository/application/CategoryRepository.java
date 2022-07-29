package br.com.juliocauan.flix.infrastructure.repository.application;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.flix.domain.repository.application.CategoryRepositoryDomain;
import br.com.juliocauan.flix.infrastructure.model.application.CategoryEntity;
import br.com.juliocauan.flix.infrastructure.repository.BaseJpaRepository;

@Repository
public interface CategoryRepository extends BaseJpaRepository<CategoryEntity, Integer>, CategoryRepositoryDomain<CategoryEntity, Integer>{

    @Override
    default String getClassName() {
        return CategoryEntity.class.getName();
    }

    @Override
    default Integer getDefaultId() {
        return 1;
    }

}
