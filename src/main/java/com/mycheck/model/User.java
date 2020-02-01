package com.mycheck.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Accessors(chain = true)
@Setter
@Getter
public class User {
    @Id
    @Email
    private String email;

    @NotNull
    private String password;
}
