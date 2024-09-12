package com.example.application.services;


import com.example.application.data.Rooms;
import com.example.application.data.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class RoomsService {
    @Autowired
    private RoomsRepository repository;

    public Optional<Rooms> get(Long id) {
        return repository.findById(id);
    }

    public Rooms update(Rooms entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Rooms> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Rooms> list(Pageable pageable, Specification<Rooms> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Rooms> getRoomsWithLocationID(@RequestParam Long locationId) {
        return repository.findRoomsWithLocationID(locationId);
    }

}


