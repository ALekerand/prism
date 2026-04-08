package com.dcspa.prism;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Charge tout le contexte Spring + JPA (MySQL {@code prism_bd} attendu).
 * Désactivé par défaut pour que {@code mvn test} reste autonome sur la CI sans base locale.
 * Retirer {@link Disabled} ou lancer avec MySQL pour valider le démarrage complet.
 */
@SpringBootTest
@Disabled("Intégration complète : nécessite MySQL (voir application.properties).")
class PrismApplicationTests {

	@Test
	void contextLoads() {
	}

}
