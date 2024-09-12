package com.example.application.data;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Locations {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "LocationID")
    private Long locationId;
    @Basic
    @Column(name = "LocationName")
    private String locationName;

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locations locations = (Locations) o;
        return locationId == locations.locationId && Objects.equals(locationName, locations.locationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, locationName);
    }
}
