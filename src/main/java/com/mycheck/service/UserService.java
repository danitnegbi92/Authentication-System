package com.mycheck.service;

import com.mycheck.dto.model.UserRegistration;
import com.mycheck.model.User;
import com.mycheck.repository.UserRepository;
import io.jsonwebtoken.*;
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

    public boolean registerUser(UserRegistration userRegistration) {
        User user = new User();
        String hashedPassword = getHashValue(userRegistration.getPassword());
        user.setEmail(userRegistration.getEmail()).setPassword(hashedPassword);
        if (userRepository.existsById(userRegistration.getEmail())) {
            LOG.error(EMAIL_ALREADY_EXISTS.getMessage());
            return false;
        }
        userRepository.save(user);
        return true;
    }

    private boolean isPasswordMatchUser(String realPassword, String dbPassword) {
        return compareHashValue(realPassword, dbPassword);
    }

    public String login(UserRegistration userRegistration) {
        String token = null;
        String email = userRegistration.getEmail();
        Optional<User> user = userRepository.findById(email);

        if(!user.isPresent()){
            LOG.error(EMAIL_NOT_EXISTS.getMessage());
            return null;
        }

        if (isPasswordMatchUser(userRegistration.getPassword(), user.get().getPassword())) {
            LOG.info("Password match to user");
            token = JwtService.createJWT(email, 10);
            token = StringUtils.isEmpty(token)? null :  SecurityConstants.AUTHORIZATION_TOKEN.getValue() + token;
        }

        return token;
    }

    public String getEmail(String token) {
        Claims claims = null;
        try {
            claims = JwtService.decodeJWT(token);
        } catch (MalformedJwtException | SignatureException e) {
            LOG.error(AUTHENTICATION_FAILED.getMessage() + " - could not decode jwt");
        }
        return claims!=null ? claims.getSubject() : null;
    }
}