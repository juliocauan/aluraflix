package br.com.juliocauan.aluraflix.infrastructere.repository;

import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.infrastructere.model.CategoriaEntity;
import br.com.juliocauan.aluraflix.infrastructere.repository.config.BaseJpaRepository;

@Repository
public interface CategoriaRepository extends BaseJpaRepository<CategoriaEntity, Integer>{
}
