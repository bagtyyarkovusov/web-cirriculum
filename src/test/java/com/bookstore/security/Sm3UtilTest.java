package com.bookstore.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Sm3UtilTest {

    /** GM/T 0004-2012 standard known-answer: SM3("abc"). */
    private static final String SM3_ABC =
            "66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0";

    @Test
    void sm3Hex_matchesStandardKnownAnswer_forAbc() {
        assertEquals(SM3_ABC, Sm3Util.sm3Hex("abc"));
    }

    @Test
    void hashPassword_isDeterministic_forSameSaltAndPassword() {
        String salt = "a1b2c3d4e5f60718";
        assertEquals(Sm3Util.hashPassword("S3cret@pw", salt),
                     Sm3Util.hashPassword("S3cret@pw", salt));
    }

    @Test
    void hashPassword_differs_whenSaltDiffers() {
        assertNotEquals(Sm3Util.hashPassword("S3cret@pw", "salt-one"),
                        Sm3Util.hashPassword("S3cret@pw", "salt-two"));
    }

    @Test
    void hashPassword_producesSixtyFourHexChars() {
        String hash = Sm3Util.hashPassword("S3cret@pw", Sm3Util.newSalt());
        assertTrue(hash.matches("[0-9a-f]{64}"), "expected 64 lowercase hex chars, got: " + hash);
    }

    @Test
    void newSalt_isRandom_andThirtyTwoHexChars() {
        String s1 = Sm3Util.newSalt();
        String s2 = Sm3Util.newSalt();
        assertNotEquals(s1, s2);
        assertTrue(s1.matches("[0-9a-f]{32}"), "salt should be 32 hex chars: " + s1);
    }

    @Test
    void matches_trueForCorrectPassword_falseForWrong() {
        String salt = Sm3Util.newSalt();
        String hash = Sm3Util.hashPassword("S3cret@pw", salt);
        assertTrue(Sm3Util.matches("S3cret@pw", salt, hash));
        assertFalse(Sm3Util.matches("wrong-pw", salt, hash));
    }
}
