package com.bookstore.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

/**
 * 国密 SM3 hashing utility (via BouncyCastle).
 *
 * <p>Passwords are stored as {@code SM3(salt + password)} in hex, with a per-user
 * random salt — satisfies the course's 身份鉴别 requirement (SM3 password storage).
 */
public final class Sm3Util {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final char[] HEX = "0123456789abcdef".toCharArray();

    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private Sm3Util() {
    }

    /** Raw SM3 hash of the UTF-8 bytes of {@code input}, as lowercase hex. */
    public static String sm3Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SM3", "BC");
            return toHex(md.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new IllegalStateException("SM3 algorithm unavailable", e);
        }
    }

    /** Salted password hash: {@code SM3(salt + password)} as hex. */
    public static String hashPassword(String password, String salt) {
        return sm3Hex(salt + password);
    }

    /** A new random 16-byte salt encoded as 32 hex chars. */
    public static String newSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return toHex(salt);
    }

    /** Verifies a password against a stored salt + hash (constant-time comparison). */
    public static boolean matches(String password, String salt, String expectedHex) {
        if (expectedHex == null) {
            return false;
        }
        String actual = hashPassword(password, salt);
        return MessageDigest.isEqual(
                actual.getBytes(StandardCharsets.US_ASCII),
                expectedHex.getBytes(StandardCharsets.US_ASCII));
    }

    private static String toHex(byte[] bytes) {
        char[] out = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xff;
            out[i * 2] = HEX[v >>> 4];
            out[i * 2 + 1] = HEX[v & 0x0f];
        }
        return new String(out);
    }
}
