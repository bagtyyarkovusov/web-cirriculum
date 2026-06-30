package com.bookstore.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User JavaBean for authentication and later user-management screens.
 */
public class User implements Serializable {

    private long id;
    private String username;
    private String passwordSm3;
    private String salt;
    private String realName;
    private String gender;
    private String phoneEnc;
    private String addressEnc;
    private Role role;
    private String status;
    private int failCount;
    private LocalDateTime lockUntil;
    private LocalDateTime pwdChangedAt;
    private LocalDateTime createdAt;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordSm3() { return passwordSm3; }
    public void setPasswordSm3(String passwordSm3) { this.passwordSm3 = passwordSm3; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhoneEnc() { return phoneEnc; }
    public void setPhoneEnc(String phoneEnc) { this.phoneEnc = phoneEnc; }

    public String getAddressEnc() { return addressEnc; }
    public void setAddressEnc(String addressEnc) { this.addressEnc = addressEnc; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getFailCount() { return failCount; }
    public void setFailCount(int failCount) { this.failCount = failCount; }

    public LocalDateTime getLockUntil() { return lockUntil; }
    public void setLockUntil(LocalDateTime lockUntil) { this.lockUntil = lockUntil; }

    public LocalDateTime getPwdChangedAt() { return pwdChangedAt; }
    public void setPwdChangedAt(LocalDateTime pwdChangedAt) { this.pwdChangedAt = pwdChangedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
