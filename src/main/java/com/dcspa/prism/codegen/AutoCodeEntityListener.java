package com.dcspa.prism.codegen;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.lang.reflect.Field;
import java.util.Locale;

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

    private static String derivePrefixFromTable(Class<?> type) {
        Table t = type.getAnnotation(Table.class);
        String table = (t != null && t.name() != null && !t.name().isBlank())
                ? t.name().trim()
                : type.getSimpleName();
        String lettersOnly = table.replaceAll("[^A-Za-z]", "");
        if (lettersOnly.length() < 3) {
            lettersOnly = (type.getSimpleName() + "XXX").replaceAll("[^A-Za-z]", "");
        }
        return lettersOnly.substring(0, 3).toUpperCase(Locale.ROOT);
    }
}

