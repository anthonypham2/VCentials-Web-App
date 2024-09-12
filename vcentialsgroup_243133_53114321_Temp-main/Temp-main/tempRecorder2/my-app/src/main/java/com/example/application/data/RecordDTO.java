package com.example.application.data;

import java.time.LocalDate;
import java.time.LocalTime;

public class RecordDTO {

    private byte[] dataEntriesId;
    private LocalDate date;
    private LocalTime time;
    private String machine;
    private Integer machineTempF;
    private String room;
    private Integer roomTempF;
    private String location;
    private String username;

    public RecordDTO(byte[] dataId,LocalDate date, LocalTime time, String machine, Integer machineTemp, String room, Integer roomTemp, String location, String username) {
        setDataEntriesId(dataId);
        setDate(date);
        setTime(time);
        setMachine(machine);
        setMachineTempF(machineTemp);
        setRoom(room);
        setRoomTempF(roomTemp);
        setLocation(location);
        setUsername(username);
    }

    public byte[] getDataEntriesId() {
        return dataEntriesId;
    }

    public void setDataEntriesId(byte[] dataEntriesId) {
        this.dataEntriesId = dataEntriesId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public Integer getMachineTempF() {
        return machineTempF;
    }

    public void setMachineTempF(Integer machineTempF) {
        this.machineTempF = machineTempF;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getRoomTempF() {
        return roomTempF;
    }

    public void setRoomTempF(Integer roomTempF) {
        this.roomTempF = roomTempF;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
    
    

    
