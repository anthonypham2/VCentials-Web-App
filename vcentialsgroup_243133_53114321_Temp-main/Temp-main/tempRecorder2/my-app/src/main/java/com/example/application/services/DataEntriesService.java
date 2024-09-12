package com.example.application.services;


import com.example.application.data.DataEntries;
import com.example.application.data.DataEntriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DataEntriesService {

    @Autowired
    private DataEntriesRepository repository;

    public Optional<DataEntries> get(byte[] id) {
        return repository.findById(id);
    }

    public DataEntries update(DataEntries entity) {
        return repository.save(entity);
    }

    public void delete(byte[] id) {
        repository.deleteById(id);
    }

    public Page<DataEntries> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<DataEntries> list(Pageable pageable, Specification<DataEntries> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
