package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInfoRepository
        extends
        JpaRepository<UserInfo, byte[]>,
        JpaSpecificationExecutor<UserInfo> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByVcid(String vcid);


    @Query(value = "SELECT * FROM userInfo WHERE userInfo.username = :username", nativeQuery = true)
    UserInfo findByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM userInfo WHERE userInfo.email = :email",nativeQuery = true)
    UserInfo findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM userInfo WHERE userInfo.passwordToken = :passwordToken",nativeQuery = true)
    UserInfo findByToken(@Param("passwordToken") String passwordToken);

}
