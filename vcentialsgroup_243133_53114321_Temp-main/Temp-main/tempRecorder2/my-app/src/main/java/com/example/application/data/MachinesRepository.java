package com.example.application.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MachinesRepository
        extends
        JpaRepository<Machines, Long>,
        JpaSpecificationExecutor<Machines> {

    @Query(value = "SELECT * FROM machines WHERE machines.roomId = :roomId",nativeQuery = true)
    List<Machines> findMachinesWithRoomID(@Param("roomId") Long roomId);

}


