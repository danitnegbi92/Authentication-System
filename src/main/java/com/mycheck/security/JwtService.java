package com.mycheck.security;

import com.mycheck.utils.DateUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JwtService {
    private static Logger LOG = LoggerFactory.getLogger(JwtService.class);

    public static String createJWT(String subject, int numOfDaysForExpiration) {
        LOG.info("Starting create JWT");

        Date now = new Date(System.currentTimeMillis());
        String secretKey = getSecretKeyFromFile();

        if(StringUtils.isEmpty(secretKey)) {
            LOG.error("secretKey is null or empty");
            return null;
        }

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(DateUtils.getDateAfterSelectedDays(now, numOfDaysForExpiration))
                .signWith(SignatureAlgorithm.HS512, getSecretKeyFromFile())
                .compact();
    }

    private static String getSecretKeyFromFile() {
        String data = null;
        try {
            Path path = Paths.get("secretKey.txt");
            Stream<String> lines = Files.lines(path);
            data = lines.collect(Collectors.joining("\n"));
            lines.close();
        }catch (IOException e){
            LOG.error("Could not load secretKey.txt file due to: "+e.getMessage());
        }

        return data;
    }

    private static Claims decodeJWT(String jwt) {
        LOG.info("Starting decode JWT");

        if (!isValidJwt(jwt)) {
            LOG.error("jwt is not valid");
            return null;
        }

        jwt = jwt.replace(SecurityConstants.AUTHORIZATION_TOKEN.getValue(), "");

        //This line will throw an exception if it is not a signed JWS (as expected)
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(getSecretKeyFromFile()))
                .parseClaimsJws(jwt).getBody();
    }

    public static String getSubject(String token) {
        Claims claims = null;
        try {
            claims = decodeJWT(token);
        } catch (MalformedJwtException | SignatureException e) {
            LOG.error("Could not decode jwt");
        }
        return claims!=null ? claims.getSubject() : null;
    }

    private static boolean isValidJwt(String jwt) {
        return jwt != null && jwt.startsWith(SecurityConstants.AUTHORIZATION_TOKEN.getValue());
    }
}
