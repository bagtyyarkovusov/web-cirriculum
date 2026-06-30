package com.bookstore.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordPolicyTest {

    @Test
    void accepts_passwordWithAllRequiredClasses_andMinLength() {
        assertTrue(PasswordPolicy.isValid("Abcd@123")); // 8 chars: upper, lower, digit, special
    }

    @Test
    void rejects_whenShorterThanEight() {
        assertFalse(PasswordPolicy.isValid("Ab@1"));
    }

    @Test
    void rejects_whenMissingUppercase() {
        assertFalse(PasswordPolicy.isValid("abcd@123"));
    }

    @Test
    void rejects_whenMissingLowercase() {
        assertFalse(PasswordPolicy.isValid("ABCD@123"));
    }

    @Test
    void rejects_whenMissingDigit() {
        assertFalse(PasswordPolicy.isValid("Abcd@xyz"));
    }

    @Test
    void rejects_whenMissingSpecialCharacter() {
        assertFalse(PasswordPolicy.isValid("Abcd1234"));
    }

    @Test
    void rejects_null() {
        assertFalse(PasswordPolicy.isValid(null));
    }
}
