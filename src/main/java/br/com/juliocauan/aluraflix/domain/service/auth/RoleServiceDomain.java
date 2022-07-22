package br.com.juliocauan.aluraflix.domain.service.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.juliocauan.aluraflix.domain.repository.auth.RoleRepositoryDomain;
import br.com.juliocauan.openapi.model.RoleType;

public interface RoleServiceDomain<E, ID> {

    RoleRepositoryDomain<E, ID> getRepository();
    ID getClientId();
    
    default Set<E> newUserRoles(){
        Set<E> set = new HashSet<>();
        set.add(getRepository().findOneOrBadRequest(getClientId()));
        return set;
    }

    default Set<E> updateUserRoles(Set<E> oldRoles, List<RoleType> newRolesAdd, List<RoleType> newRolesRemove) {
        if(newRolesRemove != null) newRolesRemove.forEach(role -> oldRoles.remove(getRepository().findByValue(role)));
        if(newRolesAdd != null) newRolesAdd.forEach(role -> oldRoles.add(getRepository().findByValue(role)));
        return oldRoles;
    }

}
