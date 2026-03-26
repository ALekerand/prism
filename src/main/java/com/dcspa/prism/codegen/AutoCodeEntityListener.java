package com.dcspa.prism.codegen;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AutoCodeEntityListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity == null) return;
        Class<?> type = entity.getClass();

        AutoCode cfg = type.getAnnotation(AutoCode.class);
        if (cfg == null) {
            return;
        }

        String tablePrefix = derivePrefixFromTable(type);
        Field codeField = resolveCodeField(type, cfg.field());
        if (codeField == null) {
            throw new IllegalStateException("Aucun champ code trouvé pour " + type.getName());
        }

        try {
            codeField.setAccessible(true);
            Object current = codeField.get(entity);
            if (current instanceof String s && !s.isBlank()) {
                return;
            }
            String code = SpringContext.getBean(CodeGeneratorService.class).nextCode(tablePrefix);
            codeField.set(entity, code);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Impossible de renseigner le code pour " + type.getName(), e);
        }
    }

    private static Field resolveCodeField(Class<?> type, String explicitField) {
        if (explicitField != null && !explicitField.isBlank()) {
            try {
                Field f = type.getDeclaredField(explicitField.trim());
                if (f.getType() != String.class) {
                    throw new IllegalStateException("Le champ " + explicitField + " n'est pas un String");
                }
                return f;
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException("Champ introuvable: " + explicitField + " sur " + type.getName(), e);
            }
        }

        // Heuristique: premier champ String dont le nom commence par "code"
        for (Field f : type.getDeclaredFields()) {
            if (f.getType() == String.class && f.getName().toLowerCase(Locale.ROOT).startsWith("code")) {
                return f;
            }
        }
        // Heuristique 2: champ String annoté @Column avec name commençant par "CODE_"
        for (Field f : type.getDeclaredFields()) {
            if (f.getType() != String.class) continue;
            Column c = f.getAnnotation(Column.class);
            if (c != null && c.name() != null && c.name().toUpperCase(Locale.ROOT).startsWith("CODE_")) {
                return f;
            }
        }
        return null;
    }

    /**
     * Préfixe sur 3 lettres majuscules, dérivé du nom physique {@link Table} :
     * <ul>
     *   <li>un seul segment (pas de {@code _}) : 3 premières lettres — ex. {@code alpha} → {@code ALP}</li>
     *   <li>plusieurs segments séparés par {@code _} : 1ʳᵉ lettre du 1ᵉʳ mot, 1ʳᵉ du 2ᵉ, 2ᵉ du 2ᵉ —
     *       ex. {@code anne_scolaire} → {@code ASC}</li>
     *   <li>3 segments ou plus : 1ʳᵉ lettre de chacun des 3 premiers segments — ex. {@code a_b_c} → {@code ABC}</li>
     * </ul>
     */
    private static String derivePrefixFromTable(Class<?> type) {
        Table t = type.getAnnotation(Table.class);
        String table = (t != null && t.name() != null && !t.name().isBlank())
                ? t.name().trim()
                : type.getSimpleName();

        List<String> segments = Arrays.stream(table.split("_"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.replaceAll("[^A-Za-z]", ""))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        if (segments.isEmpty()) {
            String fb = (type.getSimpleName() + "XXX").replaceAll("[^A-Za-z]", "");
            if (fb.length() < 3) {
                fb = fb + "XXX";
            }
            return fb.substring(0, 3).toUpperCase(Locale.ROOT);
        }

        if (segments.size() == 1) {
            String s = segments.get(0);
            if (s.length() < 3) {
                s = (s + type.getSimpleName()).replaceAll("[^A-Za-z]", "");
            }
            if (s.length() < 3) {
                s = s + "XXX";
            }
            return s.substring(0, 3).toUpperCase(Locale.ROOT);
        }

        if (segments.size() == 2) {
            String a = segments.get(0);
            String b = segments.get(1);
            char c1 = Character.toUpperCase(a.charAt(0));
            char c2 = Character.toUpperCase(b.charAt(0));
            char c3 = b.length() >= 2
                    ? Character.toUpperCase(b.charAt(1))
                    : (a.length() >= 2 ? Character.toUpperCase(a.charAt(1)) : 'X');
            return "" + c1 + c2 + c3;
        }

        return "" + Character.toUpperCase(segments.get(0).charAt(0))
                + Character.toUpperCase(segments.get(1).charAt(0))
                + Character.toUpperCase(segments.get(2).charAt(0));
    }
}

