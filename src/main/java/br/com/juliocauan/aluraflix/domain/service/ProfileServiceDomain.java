package br.com.juliocauan.aluraflix.domain.service;

import java.util.ArrayList;
import java.util.List;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;

public interface ProfileServiceDomain<E, ID> {

    BaseRepository<E, ID> getRepository();
    ID getClientId();
    
    default List<E> newUserProfiles(){
        List<E> list = new ArrayList<>();
        list.add(getRepository().findOneOrBadRequest(getClientId()));
        return list;
    }

}
