package br.com.juliocauan.aluraflix.infrastructure.service;

import org.springframework.stereotype.Service;

import br.com.juliocauan.aluraflix.domain.repository.BaseRepository;
import br.com.juliocauan.aluraflix.domain.service.ProfileServiceDomain;
import br.com.juliocauan.aluraflix.infrastructure.model.auth.ProfileEntity;
import br.com.juliocauan.aluraflix.infrastructure.repository.auth.ProfileRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfileService implements ProfileServiceDomain<ProfileEntity, Short> {

    private final ProfileRepository profileRepository;

    @Override
    public BaseRepository<ProfileEntity, Short> getRepository() {
        return profileRepository;
    }

    @Override
    public Short getClientId() {
        return 2;
    }
    
}
