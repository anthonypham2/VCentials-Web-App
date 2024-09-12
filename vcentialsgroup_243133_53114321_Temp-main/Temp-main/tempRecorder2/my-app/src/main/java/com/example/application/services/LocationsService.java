package com.example.application.services;


import com.example.application.data.Locations;
import com.example.application.data.LocationsRepository;
import com.example.application.data.MachinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationsService {
    @Autowired
    private LocationsRepository repository;

    public Optional<Locations> get(Long id) {
        return repository.findById(id);
    }

    public Locations update(Locations entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Locations> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Locations> list(Pageable pageable, Specification<Locations> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}

