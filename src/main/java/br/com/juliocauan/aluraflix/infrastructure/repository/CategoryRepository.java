package br.com.juliocauan.aluraflix.infrastructure.repository;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.infrastructure.model.CategoryEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.config.BaseJpaRepository;

@Repository
public interface CategoryRepository extends BaseJpaRepository<CategoryEntity, Integer>{
}
