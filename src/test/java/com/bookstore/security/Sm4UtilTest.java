package com.bookstore.security;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class Sm4UtilTest {

    /** 16-byte SM4 key. */
    private static final byte[] KEY = "0123456789abcdef".getBytes(StandardCharsets.UTF_8);

    @Test
    void encryptThenDecrypt_roundTrips_withExplicitKey() {
        String plaintext = "13800001234";
        String cipher = Sm4Util.encrypt(plaintext, KEY);
        assertEquals(plaintext, Sm4Util.decrypt(cipher, KEY));
    }

    @Test
    void encrypt_outputDiffersFromPlaintext() {
        String plaintext = "13800001234";
        assertNotEquals(plaintext, Sm4Util.encrypt(plaintext, KEY));
    }

    @Test
    void encrypt_usesRandomIv_soCiphertextsDiffer_butBothDecrypt() {
        String plaintext = "北京市海淀区中关村大街1号";
        String c1 = Sm4Util.encrypt(plaintext, KEY);
        String c2 = Sm4Util.encrypt(plaintext, KEY);
        assertNotEquals(c1, c2, "random IV should yield different ciphertexts");
        assertEquals(plaintext, Sm4Util.decrypt(c1, KEY));
        assertEquals(plaintext, Sm4Util.decrypt(c2, KEY));
    }

    @Test
    void roundTrips_withConfiguredKey_forChineseText() {
        String plaintext = "张三丰";
        assertEquals(plaintext, Sm4Util.decrypt(Sm4Util.encrypt(plaintext)));
    }
}
