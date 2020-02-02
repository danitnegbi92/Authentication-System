package com.mycheck.controller;

import com.mycheck.constants.ErrorsConstants;
import com.mycheck.dto.model.UserDataDto;
import com.mycheck.dto.model.UserRegistrationDto;
import com.mycheck.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.mycheck.constants.ErrorsConstants.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private static Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "Register a new user in the system")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegistrationDto userRegistrationDto){
        LOG.info("POST request to register new user");
        if(userService.registerUser(userRegistrationDto)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(ErrorsConstants.EMAIL_ALREADY_EXISTS.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    @ApiOperation(value = "User login to system and getting Authorization token response header for successful login")
    public ResponseEntity<String> login(@RequestBody @Valid UserRegistrationDto userRegistrationDto){
        LOG.info("POST request to user login");
        String token = userService.login(userRegistrationDto);
        if(StringUtils.isEmpty(token))
            return new ResponseEntity<>(LOGIN_FAILED.getMessage(), HttpStatus.UNAUTHORIZED);

        LOG.info("Login successfully - generate Authorization response header");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,  token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/data")
    @ApiOperation(value = "Receive user Authorization token in header and get corresponding user's data")
    public ResponseEntity<?> login(HttpServletRequest request){
        LOG.info("GET request to get email by Authorization request header");
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        UserDataDto userDataDto = userService.getEmail(token);
        if(StringUtils.isEmpty(userDataDto))
            return new ResponseEntity<>(AUTHENTICATION_FAILED.getMessage(), HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(userDataDto, HttpStatus.OK);
    }
}

