package com.mycheck.constants;

public enum ErrorsConstants {
    EMAIL_ALREADY_EXISTS("Could not register, email already exists in the system"),
    EMAIL_NOT_EXISTS("Email does not exist in the system"),
    AUTHORIZATION_FAILED("Authorization failed"),
    LOGIN_FAILED("Login failed - email exist or password is incorrect");


    String value;

    ErrorsConstants(String value) {
        this.value = value;
    }

    public String getMessage() {
        return this.value;
    }
}
