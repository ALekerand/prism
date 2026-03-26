package com.dcspa.prism.codegen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Active la génération automatique d'un code sur insertion.
 * Par défaut:
 * - prefix = 3 lettres majuscules dérivées du nom {@link jakarta.persistence.Table} :
 *   segment unique → 3 premières lettres ({@code alpha} → {@code ALP}) ;
 *   deux segments {@code _} → 1ʳᵉ lettre du 1ᵉʳ mot, 1ʳᵉ et 2ᵉ du 2ᵉ ({@code anne_scolaire} → {@code ASC}) ;
 *   trois segments ou plus → initiale des trois premiers mots.
 * - field = 1er champ String "code*" (ou précisé via field())
 * - format = PREFIX + 7 chiffres ({@code ALP0000001}, {@code ALP0000002}, …)
 * <p>
 * Si le champ cible est déjà renseigné (non vide) avant {@code persist}, la valeur est conservée
 * (saisie manuelle ou import possible).
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoCode {
    /**
     * Nom du champ Java (ex: "codeAlpha"). Si vide, on déduit automatiquement.
     */
    String field() default "";
}

