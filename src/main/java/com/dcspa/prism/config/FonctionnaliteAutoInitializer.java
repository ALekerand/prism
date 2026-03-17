package com.dcspa.prism.config;

import com.dcspa.prism.entity.Fonctionnalite;
import com.dcspa.prism.repository.FonctionnaliteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.util.Locale;
import java.util.Set;

/**
 * Option B: génère automatiquement les fonctionnalités à partir des routes exposées par les Controllers.
 * Convention: on prend le 1er segment après /api (ou /api/v1) comme code de fonctionnalité.
 * Exemple: /api/campagnes -> CAMPAGNES, /api/v1/centres -> CENTRES.
 */
@Component
@RequiredArgsConstructor
public class FonctionnaliteAutoInitializer {

    private final RequestMappingHandlerMapping handlerMapping;
    private final FonctionnaliteRepository fonctionnaliteRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Order(2)
    @Transactional
    public void initFonctionnalitesFromRoutes() {
        handlerMapping.getHandlerMethods().forEach((RequestMappingInfo info, Object handlerMethod) -> {
            if (info.getPatternsCondition() == null) {
                return;
            }

            for (Object pat : info.getPatternsCondition().getPatterns()) {
                String path = asPathString(pat);
                String code = extractFonctionnaliteCode(path);
                if (code == null || code.isBlank()) {
                    continue;
                }
                fonctionnaliteRepository.findByCodeFonctionnalite(code).orElseGet(() -> {
                    Fonctionnalite f = new Fonctionnalite();
                    f.setCodeFonctionnalite(code);
                    f.setLibelleFonctionnalite("Gestion " + code.toLowerCase(Locale.ROOT));
                    f.setModule("API");
                    return fonctionnaliteRepository.save(f);
                });
            }
        });
    }

    private static String asPathString(Object pattern) {
        if (pattern == null) return null;
        // Spring PathPattern
        if (pattern instanceof org.springframework.web.util.pattern.PathPattern pp) {
            return pp.getPatternString();
        }
        // String / legacy
        return pattern.toString();
    }

    private static String extractFonctionnaliteCode(String path) {
        if (path == null) return null;
        String p = path.trim();
        if (!p.startsWith("/")) p = "/" + p;
        if (!p.startsWith("/api/")) return null;

        // /api/v1/xxx -> xxx
        String rest = p.substring("/api/".length());
        if (rest.startsWith("v1/")) {
            rest = rest.substring("v1/".length());
        }
        // take first segment only
        int slash = rest.indexOf('/');
        String segment = slash >= 0 ? rest.substring(0, slash) : rest;

        // ignore auth + swagger endpoints for "fonctionnalite"
        if (segment.equalsIgnoreCase("auth")
                || segment.equalsIgnoreCase("swagger-ui")
                || segment.equalsIgnoreCase("v3")
                || segment.isBlank()
                || segment.startsWith("{")) {
            return null;
        }
        return segment.toUpperCase(Locale.ROOT);
    }
}

