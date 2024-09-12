package com.example.application.services;

import com.example.application.data.RecordDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SharedGridDataService {
    private List<RecordDTO> records;

    public List<RecordDTO> getRecords() {
        return records;
    }

    public void setRecords(List<RecordDTO> records) {
        this.records = records;
    }
}
