package com.example.application.services;

import com.example.application.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecordServices {

    @Autowired
    private  UserInfoRepository userInfoRepository;
    @Autowired
    private LocationsRepository locationsRepository;
    @Autowired
    private RoomsRepository roomsRepository;
    @Autowired
    private MachinesRepository machinesRepository;
    @Autowired
    private DataEntriesRepository dataEntriesRepository;

    /**
     * Creates a new record object for each dataEntry object and gives it values joined from multiple tables
     * @return the record objects containing information from multiple tables to display
     */
    public List<RecordDTO> getRecords() {
        List<RecordDTO> records = new ArrayList<>();
        List<DataEntries> dataEntries = dataEntriesRepository.findAll();
        for (DataEntries dataEntry: dataEntries){
            Optional<UserInfo> userInfo = userInfoRepository.findById(dataEntry.getUserId());
            Optional<Machines> machines = machinesRepository.findById(dataEntry.getMachineId());
            Optional<Rooms> rooms = roomsRepository.findById(machines.get().getRoomId());
            Optional<Locations> locations = locationsRepository.findById(rooms.get().getLocationId());
            records.add(new RecordDTO(
                dataEntry.getDataEntriesId(),
                dataEntry.getDate(),
                dataEntry.getTime(),
                machines.get().getMachineName(),
                dataEntry.getMachineTempF(),
                rooms.get().getRoomName(),
                dataEntry.getRoomTempF(),
                locations.get().getLocationName(),
                userInfo.get().getUsername()
            ));
        }
        return records;
    }
}
