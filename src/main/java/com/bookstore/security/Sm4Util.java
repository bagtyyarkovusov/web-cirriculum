package com.bookstore.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

/**
 * 国密 SM4 field-encryption utility (via BouncyCastle), used to protect sensitive
 * fields (phone, address) at rest — satisfies the 数据保密性 requirement.
 *
 * <p>Mode is SM4/CBC/PKCS7Padding with a random 16-byte IV per call. The IV is
 * prepended to the ciphertext and the whole thing is Base64-encoded for storage.
 */
public final class Sm4Util {

    private static final String TRANSFORMATION = "SM4/CBC/PKCS7Padding";
    private static final int BLOCK_SIZE = 16; // SM4 block / IV / key length (bytes)
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final byte[] CONFIGURED_KEY = loadConfiguredKey();

    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private Sm4Util() {
    }

    /** Encrypts with an explicit 16-byte key. Returns Base64(IV ‖ ciphertext). */
    public static String encrypt(String plaintext, byte[] key) {
        try {
            byte[] iv = new byte[BLOCK_SIZE];
            RANDOM.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "SM4"), new IvParameterSpec(iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            byte[] out = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, out, 0, iv.length);
            System.arraycopy(ciphertext, 0, out, iv.length, ciphertext.length);
            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new IllegalStateException("SM4 encryption failed", e);
        }
    }

    /** Decrypts Base64(IV ‖ ciphertext) with an explicit 16-byte key. */
    public static String decrypt(String base64, byte[] key) {
        try {
            byte[] in = Base64.getDecoder().decode(base64);
            byte[] iv = Arrays.copyOfRange(in, 0, BLOCK_SIZE);
            byte[] ciphertext = Arrays.copyOfRange(in, BLOCK_SIZE, in.length);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, "BC");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "SM4"), new IvParameterSpec(iv));
            return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("SM4 decryption failed", e);
        }
    }

    /** Encrypts using the application's configured key ({@code sm4.key} in db.properties). */
    public static String encrypt(String plaintext) {
        return encrypt(plaintext, CONFIGURED_KEY);
    }

    /** Decrypts using the application's configured key. */
    public static String decrypt(String base64) {
        return decrypt(base64, CONFIGURED_KEY);
    }

    private static byte[] loadConfiguredKey() {
        String value = System.getProperty("sm4.key");
        if (value == null || value.isBlank()) {
            value = System.getenv("SM4_KEY");
        }
        if (value == null || value.isBlank()) {
            Properties p = new Properties();
            try (InputStream in = Sm4Util.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (in != null) {
                    p.load(in);
                }
            } catch (IOException ignored) {
                // properties unavailable
            }
            value = p.getProperty("sm4.key");
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "SM4 key is not configured. Set the SM4_KEY environment variable, "
                            + "the sm4.key system property, or sm4.key in db.properties.");
        }
        String hex = value.trim();
        if (!hex.matches("^[0-9a-fA-F]{32}$")) {
            throw new IllegalStateException("SM4 key must be 32 hex characters (16 bytes), got: " + hex);
        }
        return hexToBytes(hex);
    }

    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] out = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            out[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return out;
    }
}
