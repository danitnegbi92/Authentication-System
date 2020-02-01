package com.mycheck.constants;

public enum ErrorsConstants {
    EMAIL_ALREADY_EXISTS("Could not register, email already exists in the system"),
    EMAIL_NOT_EXISTS("Email does not exist in the system"),
    AUTHENTICATION_FAILED("Authentication failed"),
    LOGIN_FAILED("Login failed");


    String value;

    ErrorsConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return this.value;
    }
}
