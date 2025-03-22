package com.identity_service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jakarta.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class IdentityServiceApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(IdentityServiceApplicationTests.class);

    @Test
    void contextLoads() {}

    @Test
    void hash() throws NoSuchAlgorithmException {
        String password = "123456";

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte[] digest = md.digest();
        String md5Hash = DatatypeConverter.printHexBinary(digest);
        log.info("MD5 Hash 1: {}", md5Hash);

        md.update(password.getBytes());
        digest = md.digest();
        md5Hash = DatatypeConverter.printHexBinary(digest);
        log.info("MD5 Hash 2: {}", md5Hash);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        log.info("Encoded Password: {}", encodedPassword);
        log.info("Hash Again: {}", passwordEncoder.encode(password));
    }
}
