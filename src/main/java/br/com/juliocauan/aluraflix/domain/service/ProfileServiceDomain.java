package br.com.juliocauan.aluraflix.domain.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.juliocauan.aluraflix.domain.repository.auth.ProfileRepositoryDomain;
import br.com.juliocauan.openapi.model.ProfileType;

public interface ProfileServiceDomain<E, ID> {

    ProfileRepositoryDomain<E, ID> getRepository();
    ID getClientId();
    
    default Set<E> newUserProfiles(){
        Set<E> set = new HashSet<>();
        set.add(getRepository().findOneOrBadRequest(getClientId()));
        return set;
    }

    default Set<E> updateUserProfiles(Set<E> oldProfiles, List<ProfileType> newProfilesAdd, List<ProfileType> newProfilesRemove) {
        if(newProfilesRemove != null) newProfilesRemove.forEach(profile -> oldProfiles.remove(getRepository().findByValue(profile)));
        if(newProfilesAdd != null) newProfilesAdd.forEach(profile -> oldProfiles.add(getRepository().findByValue(profile)));
        return oldProfiles;
    }

}
