package br.com.juliocauan.aluraflix.infrastructere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.infrastructere.model.CategoryEntity;

@Repository
public interface CategoryRepository extends BaseRepository<CategoryEntity, Integer>, JpaRepository<CategoryEntity, Integer>{
    
    @Override
    default List<CategoryEntity> findList() {
        return findAll();
    }

    @Override
    default CategoryEntity findOne(Integer id) {
        return findById(id).orElse(null);
    }

    @Override
    default CategoryEntity post(CategoryEntity entity) {
        return save(entity);
    }

    @Override
    default void remove(CategoryEntity entity) {
        delete(entity);
    }

}
