package com.mycheck.security;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptService {


    public static String getHashValue(String value) {
        String salt = BCrypt.gensalt(6);
        return BCrypt.hashpw(value, salt);
    }
    public static boolean compareHashValue(String realValue, String valueToCompare) {
        return BCrypt.checkpw(realValue, valueToCompare);
    }
}
