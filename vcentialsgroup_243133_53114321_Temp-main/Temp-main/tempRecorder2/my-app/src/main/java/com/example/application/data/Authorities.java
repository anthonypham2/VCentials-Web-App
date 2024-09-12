package com.example.application.data;

import jakarta.persistence.*;

@Entity
public class Authorities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "username", nullable = false)
    private String username;
    @Basic
    @Column(name = "authority", nullable = false)
    private String authority;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    private UserInfo userInfo;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public UserInfo getUser() {
        return userInfo;
    }

    public void setUser(UserInfo user) {
        this.userInfo = user;
    }
}
