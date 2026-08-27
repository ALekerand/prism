-- Référentiel Type SIE (Confectionnel, Madrassa) + colonne sur fiche SIE
-- Flyway : V11__type_sie.sql (identique)

CREATE TABLE IF NOT EXISTS type_sie (
  ID_TYPE_SIE INT NOT NULL AUTO_INCREMENT,
  LIBELLE_TYPE_SIE VARCHAR(50) NULL,
  PRIMARY KEY (ID_TYPE_SIE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CALL prism_add_column_if_missing('sie', 'ID_TYPE_SIE', 'INT NULL');

INSERT INTO type_sie (LIBELLE_TYPE_SIE)
SELECT 'Confectionnel'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM type_sie WHERE LIBELLE_TYPE_SIE = 'Confectionnel');

INSERT INTO type_sie (LIBELLE_TYPE_SIE)
SELECT 'Madrassa'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM type_sie WHERE LIBELLE_TYPE_SIE = 'Madrassa');
