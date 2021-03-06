package br.com.juliocauan.flix.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.flix.infrastructure.mapper.auth.UserMapper;
import br.com.juliocauan.flix.infrastructure.model.auth.UserEntity;
import br.com.juliocauan.flix.infrastructure.model.auth.specification.UserSpecification;
import br.com.juliocauan.flix.infrastructure.repository.auth.UserRepository;
import br.com.juliocauan.flix.infrastructure.service.auth.RoleService;
import br.com.juliocauan.openapi.api.UsersApi;
import br.com.juliocauan.openapi.model.UserGet;
import br.com.juliocauan.openapi.model.UserPost;
import br.com.juliocauan.openapi.model.UserPut;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class UserController implements UsersApi{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;

    @Override
    public ResponseEntity<UserGet> _addUser(@Valid UserPost userPost) {
        UserEntity user = userMapper.postDtoToEntity(userPost);
        user.setRoles(roleService.newUserRoles());
        UserGet response = userMapper.entityToGetDto(userRepository.save(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<Page<UserGet>> _findAllUsers(@Valid String search, Pageable pageable) {
        Page<UserGet> response = userRepository
            .findAll(UserSpecification.hasInEmail(search), pageable)
            .map(userMapper::entityToGetDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<UserGet> _findUserById(Long userId) {
        UserEntity user = userRepository.findOneOrNotFound(userId);
        UserGet response = userMapper.entityToGetDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<UserGet> _updateUser(Long userId, @Valid UserPut userPut) {
        UserEntity userNew = userMapper.putDtoToEntity(userPut);
        UserEntity userOld = userRepository.findOneOrNotFound(userId);
        userMapper.update(userNew, userOld);
        userOld.setRoles(roleService.updateUserRoles(userOld.getRoles(), userPut.getRolesAdd(), userPut.getRolesRemove()));
        UserGet response = userMapper.entityToGetDto(userRepository.save(userOld));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Void> _deleteUser(Long userId) {
        userRepository.remove(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
}
