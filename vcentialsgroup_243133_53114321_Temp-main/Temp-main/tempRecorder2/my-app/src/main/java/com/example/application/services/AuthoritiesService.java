package com.example.application.services;

import com.example.application.data.AuthoritiesRepository;
import com.example.application.data.Authorities;
import com.example.application.data.AuthoritiesRepository;
import com.example.application.data.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Service
public class AuthoritiesService {

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    public Optional<Authorities> get(Long id) {
        return authoritiesRepository.findById(id);
    }

    public Authorities update(Authorities entity) {
        return authoritiesRepository.save(entity);
    }

    public void delete(Long id) {
        authoritiesRepository.deleteById(id);
    }

    public Page<Authorities> list(Pageable pageable) {
        return authoritiesRepository.findAll(pageable);
    }

    public Page<Authorities> list(Pageable pageable, Specification<Authorities> filter) {
        return authoritiesRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) authoritiesRepository.count();
    }

    public Authorities getUsername(@RequestParam String username) {
        return authoritiesRepository.findByUsername(username);
    }


}
