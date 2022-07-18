package br.com.juliocauan.aluraflix.infrastructure.repository.application;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.domain.repository.application.CategoryRepositoryDomain;
import br.com.juliocauan.aluraflix.infrastructure.model.application.CategoryEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.config.BaseJpaRepository;

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
