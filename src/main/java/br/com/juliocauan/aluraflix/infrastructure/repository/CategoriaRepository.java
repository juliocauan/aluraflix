package br.com.juliocauan.aluraflix.infrastructure.repository;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.infrastructure.model.CategoriaEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.config.BaseJpaRepository;

@Repository
public interface CategoriaRepository extends BaseJpaRepository<CategoriaEntity, Integer>{
}
