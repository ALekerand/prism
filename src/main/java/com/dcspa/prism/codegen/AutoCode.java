package com.dcspa.prism.codegen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Active la génération automatique d'un code sur insertion.
 * Par défaut:
 * - prefix = 3 premières lettres du nom de table (annotation @Table), en uppercase
 * - field = 1er champ String "code*" (ou précisé via field())
 * - format = PREFIX + 6 chiffres (000001, 000002, ...)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoCode {
    /**
     * Nom du champ Java (ex: "codeAlpha"). Si vide, on déduit automatiquement.
     */
    String field() default "";
}

