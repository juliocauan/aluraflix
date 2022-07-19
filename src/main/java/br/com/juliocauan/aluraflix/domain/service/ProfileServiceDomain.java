package br.com.juliocauan.aluraflix.domain.service;

import java.util.HashSet;
import java.util.Set;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;

public interface ProfileServiceDomain<E, ID> {

    BaseRepository<E, ID> getRepository();
    ID getClientId();
    
    default Set<E> newUserProfiles(){
        Set<E> set = new HashSet<>();
        set.add(getRepository().findOneOrBadRequest(getClientId()));
        return set;
    }

}
