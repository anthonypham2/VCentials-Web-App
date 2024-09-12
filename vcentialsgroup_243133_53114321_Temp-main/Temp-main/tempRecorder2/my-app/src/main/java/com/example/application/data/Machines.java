package com.example.application.data;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Machines {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "MachineID")
    private Long machineId;
    @Basic
    @Column(name = "RoomID")
    private Long roomId;
    @Basic
    @Column(name = "MachineName")
    private String machineName;

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machines machines = (Machines) o;
        return machineId == machines.machineId && roomId == machines.roomId && Objects.equals(machineName, machines.machineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machineId, roomId, machineName);
    }
}
