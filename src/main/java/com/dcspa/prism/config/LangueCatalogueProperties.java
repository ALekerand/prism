package com.dcspa.prism.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Centre technique pour le catalogue « Langues d'apprentissage » (Paramétrage).
 * Si {@code centreId} est absent, le premier centre alpha est utilisé.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "prism.langue-catalogue")
public class LangueCatalogueProperties {

	private Integer centreId;
}
