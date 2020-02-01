package com.mycheck.dto.model;

import lombok.Getter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Accessors(chain = true)
@Getter
public class UserRegistration {
    @Email
    private String email;

    @NotBlank
    private String password;
}
