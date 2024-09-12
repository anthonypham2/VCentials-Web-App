package com.example.application.services;


import com.example.application.data.Machines;
import com.example.application.data.MachinesRepository;
import com.example.application.data.MachinesRepository;
import com.example.application.data.Rooms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class MachinesService {
    @Autowired
    private MachinesRepository repository;

    public Optional<Machines> get(Long id) {
        return repository.findById(id);
    }

    public Machines update(Machines entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Machines> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Machines> list(Pageable pageable, Specification<Machines> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Machines> getMachinesWithRoomID(@RequestParam Long roomId) {
        return repository.findMachinesWithRoomID(roomId);
    }
}

