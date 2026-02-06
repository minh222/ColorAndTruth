package com.minh.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class Security {
    private static byte[] serverSecretHash;

    public static void init(byte[] secret) {
        serverSecretHash = hash(secret);
    }

    public static boolean verify(char[] tryPassword, byte[] password) {
        byte[] verify = creteVerify(tryPassword);
        return MessageDigest.isEqual(verify, password);
    }

    public static byte[] creteVerify(char[] password)  {
        byte[] bytesPassword = new String(password).getBytes(StandardCharsets.UTF_8);
        byte[] pwHash = hash(bytesPassword);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pwHash);
            md.update(serverSecretHash);

            return  md.digest();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] hash(byte[] in) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
