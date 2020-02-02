package com.mycheck.service;

import com.mycheck.dto.model.UserDataDto;
import com.mycheck.dto.model.UserRegistrationDto;
import com.mycheck.model.User;
import com.mycheck.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.mycheck.security.JwtService;
import com.mycheck.security.SecurityConstants;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.util.Optional;

import static com.mycheck.constants.ErrorsConstants.*;
import static com.mycheck.security.BCryptService.*;

@Service
public class UserService {
    private static Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserRepository userRepository;

    public boolean registerUser(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        String hashedPassword = getHashValue(userRegistrationDto.getPassword());
        user.setEmail(userRegistrationDto.getEmail()).setPassword(hashedPassword);
        if (userRepository.existsById(userRegistrationDto.getEmail())) {
            LOG.error(EMAIL_ALREADY_EXISTS.getMessage());
            return false;
        }
        userRepository.save(user);
        return true;
    }

    private boolean isPasswordMatchUser(String realPassword, String dbPassword) {
        return compareHashValue(realPassword, dbPassword);
    }

    public String login(UserRegistrationDto userRegistrationDto) {
        String token = null;
        String email = userRegistrationDto.getEmail();
        Optional<User> user = userRepository.findById(email);

        if(!user.isPresent()){
            LOG.error(EMAIL_NOT_EXISTS.getMessage());
            return null;
        }

        if (isPasswordMatchUser(userRegistrationDto.getPassword(), user.get().getPassword())) {
            LOG.info("Password match to user");
            token = JwtService.createJWT(email, 5);
            token = StringUtils.isEmpty(token)? null :  SecurityConstants.AUTHORIZATION_TOKEN.getValue() + token;
        }

        return token;
    }

    public UserDataDto getEmail(String token) {
        String email = JwtService.getSubject(token);
        if(StringUtils.isEmpty(email))
            return null;

        UserDataDto userDataDto = new UserDataDto();
        userDataDto.setEmail(email);
        return userDataDto;
    }
}