-- Evolution du contrôle démarrage :
-- - plusieurs jours/heures de formation par contrôle
-- - nombre de kits saisi par manuel
-- Script idempotent pour MySQL.

CREATE TABLE IF NOT EXISTS controle_horaire_formation (
  ID_CONTROLE_HORAIRE INT NOT NULL AUTO_INCREMENT,
  ID_CONTROLE INT NOT NULL,
  JOUR_SEMAINE VARCHAR(20) NOT NULL,
  HEURE_DEBUT TIME NOT NULL,
  HEURE_FIN TIME NOT NULL,
  PRIMARY KEY (ID_CONTROLE_HORAIRE),
  INDEX idx_controle_horaire_controle (ID_CONTROLE),
  CONSTRAINT fk_controle_horaire_controle
    FOREIGN KEY (ID_CONTROLE) REFERENCES controle (ID_CONTROLE)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS controle_kit_manuel (
  ID_CONTROLE_KIT_MANUEL INT NOT NULL AUTO_INCREMENT,
  ID_CONTROLE INT NOT NULL,
  ID_MANUEL INT NOT NULL,
  NOMBRE_KIT INT NOT NULL DEFAULT 0,
  PRECISION_AUTRE VARCHAR(150) NULL,
  PRIMARY KEY (ID_CONTROLE_KIT_MANUEL),
  INDEX idx_controle_kit_controle (ID_CONTROLE),
  INDEX idx_controle_kit_manuel (ID_MANUEL),
  CONSTRAINT fk_controle_kit_controle
    FOREIGN KEY (ID_CONTROLE) REFERENCES controle (ID_CONTROLE)
    ON DELETE CASCADE,
  CONSTRAINT fk_controle_kit_manuel
    FOREIGN KEY (ID_MANUEL) REFERENCES manuel (ID_MANUEL)
);
