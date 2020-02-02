package com.mycheck.dto.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;

@Accessors(chain = true)
@Getter
@Setter

//This class expose all the data the user should see
public class UserDataDto {
    @Email
    private String email;
}
