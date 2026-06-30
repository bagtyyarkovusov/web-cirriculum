package com.bookstore.service;

public class RegistrationResult {

    public enum Status {
        SUCCESS,
        INVALID_INPUT,
        USERNAME_TAKEN,
        WEAK_PASSWORD
    }

    private final Status status;

    private RegistrationResult(Status status) {
        this.status = status;
    }

    public static RegistrationResult success() {
        return new RegistrationResult(Status.SUCCESS);
    }

    public static RegistrationResult invalidInput() {
        return new RegistrationResult(Status.INVALID_INPUT);
    }

    public static RegistrationResult usernameTaken() {
        return new RegistrationResult(Status.USERNAME_TAKEN);
    }

    public static RegistrationResult weakPassword() {
        return new RegistrationResult(Status.WEAK_PASSWORD);
    }

    public Status getStatus() {
        return status;
    }
}
