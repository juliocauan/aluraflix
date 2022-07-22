package br.com.juliocauan.aluraflix.domain.repository;

public interface BaseRepository<E, ID> {
    
    String getClassName();

    E findOneOrNull(ID id);
    E findOneOrNotFound(ID id);
    E findOneOrBadRequest(ID id);
    void remove(ID id);

}
