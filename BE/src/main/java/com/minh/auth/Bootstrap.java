package com.minh.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
@Component
public final class Bootstrap {
    public Bootstrap(@Value("${jwt.secret}") String secret) {
        byte[] secretBytes = Base64.getDecoder().decode(secret);
        Jwt.init(secretBytes);
        Security.init(secretBytes);
    }
}
