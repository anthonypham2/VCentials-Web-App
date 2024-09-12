package com.example.application.services;


import com.example.application.data.Rooms;
import com.example.application.data.UserInfo;
import com.example.application.data.UserInfoRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {
    @Autowired
    private  UserInfoRepository repository;

    public Optional<UserInfo> get(byte[] id) {
        return repository.findById(id);
    }

    public UserInfo update(UserInfo entity) {
        return repository.save(entity);
    }

    public void delete(byte[] id) {
        repository.deleteById(id);
    }

    public Page<UserInfo> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<UserInfo> list(Pageable pageable, Specification<UserInfo> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public UserInfo getUsername(@RequestParam String username) {
        return repository.findByUsername(username);
    }

    public UserInfo getEmail(@RequestParam String email) {
        return repository.findByEmail(email);
    }

    public UserInfo getPasswordToken(@RequestParam String passwordToken) {return repository.findByToken(passwordToken);}

}
