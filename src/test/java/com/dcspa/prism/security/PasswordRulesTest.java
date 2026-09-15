package com.dcspa.prism.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PasswordRulesTest {

    @Test
    void rejectsBlankAndShortPasswords() {
        assertThrows(IllegalArgumentException.class, () -> PasswordRules.requireNewPassword(null));
        assertThrows(IllegalArgumentException.class, () -> PasswordRules.requireNewPassword("  "));
        assertThrows(IllegalArgumentException.class, () -> PasswordRules.requireNewPassword("1234567"));
    }

    @Test
    void acceptsEightCharacters() {
        assertDoesNotThrow(() -> PasswordRules.requireNewPassword("admin123"));
    }
}
