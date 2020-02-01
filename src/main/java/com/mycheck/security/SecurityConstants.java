package com.mycheck.security;

public enum SecurityConstants {
    AUTHORIZATION_TOKEN("Bearer ");

    String value;

    SecurityConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
