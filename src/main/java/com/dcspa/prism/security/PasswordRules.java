package com.dcspa.prism.security;

/**
 * Règles communes pour tout mot de passe nouvellement posé (création, reset admin, changement perso).
 */
public final class PasswordRules {

    public static final int MIN_LENGTH = 8;

    private PasswordRules() {}

    public static void requireNewPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire.");
        }
        if (password.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "Le mot de passe doit contenir au moins " + MIN_LENGTH + " caractères.");
        }
    }
}
