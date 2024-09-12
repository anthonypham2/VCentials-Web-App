package com.example.application.data;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Entity
public class UserInfo {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "UserID",columnDefinition = "uuid")
    @ColumnDefault(value = "RANDOM_UUID()")
    private byte[] userId;// = UUID.randomUUID().toString().getBytes();
    @Basic
    @Column(name = "Username", unique = true)
    private String username;
    @Basic
    @Column(name = "VCID",unique = true)
    private String vcid;
    @Basic
    @Column(name = "Password")
    private String password;
    @Basic
    @Column(name = "Email", unique = true)
    private String email;
    @Basic
    @Column(name = "IsAdmin")
    @ColumnDefault(value = "false")
    private Boolean isAdmin;

    @Basic
    @Column(name = "enabled")
    @ColumnDefault(value = "true")
    private Boolean enabled;

    @Basic
    @Column(name = "passwordToken", unique = true)
    private String passwordToken;

    public byte[] getUserId() {
        return userId;
    }

    public void setUserId(byte[] userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVcid() {
        return vcid;
    }

    public void setVcid(String vcid) {
        this.vcid = vcid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPasswordToken() {
        return passwordToken;
    }

    public void setPasswordToken(String passwordToken) {
        this.passwordToken = passwordToken;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userinfo = (UserInfo) o;
        return Arrays.equals(userId, userinfo.userId) && Objects.equals(username, userinfo.username) && Objects.equals(vcid, userinfo.vcid) && Objects.equals(password, userinfo.password) && Objects.equals(email, userinfo.email) && Objects.equals(isAdmin, userinfo.isAdmin);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(username, vcid, password, email, isAdmin);
        result = 31 * result + Arrays.hashCode(userId);
        return result;
    }
}
