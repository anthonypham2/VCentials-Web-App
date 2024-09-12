package com.example.application.data;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Rooms")
public class Rooms {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "RoomID")
    private Long roomId;
    @Basic
    @Column(name = "LocationID")
    private Long locationId;
    @Basic
    @Column(name = "RoomName")
    private String roomName;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rooms rooms = (Rooms) o;
        return roomId == rooms.roomId && locationId == rooms.locationId && Objects.equals(roomName, rooms.roomName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, locationId, roomName);
    }
}
