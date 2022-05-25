package br.com.juliocauan.aluraflix.infrastructere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.infrastructere.model.CategoriaEntity;

@Repository
public interface CategoriaRepository extends BaseRepository<CategoriaEntity, Integer>, JpaRepository<CategoriaEntity, Integer>{
    
    @Override
    default List<CategoriaEntity> findList() {
        return findAll();
    }

    @Override
    default CategoriaEntity findOne(Integer id) {
        return findById(id).orElse(null);
    }

    @Override
    default CategoriaEntity post(CategoriaEntity entity) {
        return save(entity);
    }

    @Override
    default void remove(CategoriaEntity entity) {
        delete(entity);
    }

}
