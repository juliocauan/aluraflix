package br.com.juliocauan.aluraflix.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliocauan.aluraflix.infrastructure.mapper.auth.UserMapper;
import br.com.juliocauan.aluraflix.infrastructure.repository.auth.UserRepository;
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

    @Override
    public ResponseEntity<UserGet> _addUser(@Valid UserPost userPost) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<Page<UserGet>> _findAllUsers(@Valid String search, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<UserGet> _findUserById(Long userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<UserGet> _updateUser(Long userId, @Valid UserPut userPut) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<Void> _deleteUser(Long userId) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
