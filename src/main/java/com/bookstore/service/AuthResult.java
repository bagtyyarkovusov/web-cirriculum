package com.bookstore.service;

import com.bookstore.model.SessionUser;

import java.time.LocalDateTime;

public class AuthResult {

    public enum Status {
        SUCCESS,
        BAD_CREDENTIALS,
        LOCKED,
        DISABLED
    }

    private final Status status;
    private final SessionUser user;
    private final LocalDateTime lockUntil;

    private AuthResult(Status status, SessionUser user, LocalDateTime lockUntil) {
        this.status = status;
        this.user = user;
        this.lockUntil = lockUntil;
    }

    public static AuthResult success(SessionUser user) {
        return new AuthResult(Status.SUCCESS, user, null);
    }

    public static AuthResult badCredentials() {
        return new AuthResult(Status.BAD_CREDENTIALS, null, null);
    }

    public static AuthResult locked(LocalDateTime lockUntil) {
        return new AuthResult(Status.LOCKED, null, lockUntil);
    }

    public static AuthResult disabled() {
        return new AuthResult(Status.DISABLED, null, null);
    }

    public Status getStatus() { return status; }

    public SessionUser getUser() { return user; }

    public LocalDateTime getLockUntil() { return lockUntil; }
}
