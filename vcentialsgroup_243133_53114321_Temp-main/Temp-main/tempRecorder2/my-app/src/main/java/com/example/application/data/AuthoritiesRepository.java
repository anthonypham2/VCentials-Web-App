package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthoritiesRepository
        extends
        JpaRepository<Authorities, Long>,
        JpaSpecificationExecutor<Authorities>{


    @Query(value = "SELECT * FROM authorities WHERE authorities.username = :username", nativeQuery = true)
    Authorities findByUsername(@Param("username") String username);

}
