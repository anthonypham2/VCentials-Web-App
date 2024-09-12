package com.example.application.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomsRepository
        extends
        JpaRepository<Rooms, Long>,
        JpaSpecificationExecutor<Rooms> {

    @Query(value = "SELECT * FROM Rooms WHERE Rooms.locationId = :locationId",nativeQuery = true)
    List<Rooms> findRoomsWithLocationID(@Param("locationId") Long locationId);

}

