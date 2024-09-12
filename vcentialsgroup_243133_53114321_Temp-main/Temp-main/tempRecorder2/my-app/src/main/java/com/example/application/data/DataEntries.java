package com.example.application.data;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "DataEntries")
public class DataEntries {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "DataEntriesID",columnDefinition = "uuid")
    @ColumnDefault(value = "RANDOM_UUID()")
    private byte[] dataEntriesId;// = UUID.randomUUID().toString().getBytes();
    @Basic
    @Column(name = "UserID",columnDefinition = "uuid")
    private byte[] userId;
    @Basic
    @Column(name = "MachineID")
    private Long machineId;
    @Basic
    @Column(name = "MachineTempF")
    private Integer machineTempF;
    @Basic
    @Column(name = "RoomTempF")
    private Integer roomTempF;
    @Basic
    @Column(name = "Date")
    private LocalDate date;
    @Basic
    @Column(name = "Time")
    private LocalTime time;

    public byte[] getDataEntriesId() {
        return dataEntriesId;
    }

    public void setDataEntriesId(byte[] dataEntriesId) {
        this.dataEntriesId = dataEntriesId;
    }

    public byte[] getUserId() {
        return userId;
    }

    public void setUserId(byte[] userId) {
        this.userId = userId;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public Integer getMachineTempF() {
        return machineTempF;
    }

    public void setMachineTempF(Integer machineTempF) {
        this.machineTempF = machineTempF;
    }

    public Integer getRoomTempF() {
        return roomTempF;
    }

    public void setRoomTempF(Integer roomTempF) {
        this.roomTempF = roomTempF;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataEntries that = (DataEntries) o;
        return machineId == that.machineId && Arrays.equals(dataEntriesId, that.dataEntriesId) && Arrays.equals(userId, that.userId) && Objects.equals(machineTempF, that.machineTempF) && Objects.equals(roomTempF, that.roomTempF) && Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(machineId, machineTempF, roomTempF, date, time);
        result = 31 * result + Arrays.hashCode(dataEntriesId);
        result = 31 * result + Arrays.hashCode(userId);
        return result;
    }
}
