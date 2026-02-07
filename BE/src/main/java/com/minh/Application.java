package com.minh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args
        );
        byte[] key = new byte[32]; // 256-bit
        new SecureRandom().nextBytes(key);


    }

}
