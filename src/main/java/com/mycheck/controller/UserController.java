package com.mycheck.controller;

import com.mycheck.constants.ErrorsConstants;
import com.mycheck.dto.model.UserRegistration;
import com.mycheck.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.mycheck.constants.ErrorsConstants.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private static Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistration userRegistration){
        LOG.info("POST request to register new user");
        if(userService.registerUser(userRegistration)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(ErrorsConstants.EMAIL_ALREADY_EXISTS.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserRegistration userRegistration, HttpServletResponse response){
        LOG.info("POST request to user login");
        String token = userService.login(userRegistration);
        if(StringUtils.isEmpty(token))
            return new ResponseEntity<>(LOGIN_FAILED.getMessage(), HttpStatus.UNAUTHORIZED);

        LOG.info("Login successfully - generate Authorization response header");
        response.addHeader(HttpHeaders.AUTHORIZATION,  token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/data")
    public ResponseEntity<?> login(HttpServletRequest request, String token1){ //todo delete token1
        LOG.info("GET request to get email by Authorization request header");
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = userService.getEmail(token1);
        if(StringUtils.isEmpty(email))
            return new ResponseEntity<>(AUTHORIZATION_FAILED.getMessage(), HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(email, HttpStatus.OK);
    }
}

