-- Écarts schéma code vs dump VPS (alignement ddl-auto local → Flyway staging/prod).

CALL prism_add_column_if_missing('centre', 'DATE_ENREGISTREMENT', 'DATE NULL');
CALL prism_add_column_if_missing('centre', 'DATE_CREATION_DAAJE', 'DATE NULL');

CREATE TABLE IF NOT EXISTS ecole_tutrice (
  id_ecole_tutrice INT NOT NULL AUTO_INCREMENT,
  code_ecole_tutrice VARCHAR(20) NULL,
  libelle_ecole_tutrice VARCHAR(150) NULL,
  PRIMARY KEY (id_ecole_tutrice)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CALL prism_add_column_if_missing('effectif_abondan_sie', 'ID_CENTRE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_situation_handicap_sie', 'ID_CENTRE', 'INT NULL');

CALL prism_add_column_if_missing('effectif_cec', 'EFFECTIF_CEC_MOINS_DE_6_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_cec', 'EFFECTIF_CEC_MOINS_DE_6_F', 'INT NULL');
CALL prism_add_column_if_missing('effectif_cec', 'EFFECTIF_CEC_MOINS_DE_6_IVOIRIEN_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_cec', 'EFFECTIF_CEC_MOINS_DE_6_IVOIRIEN_F', 'INT NULL');
CALL prism_add_column_if_missing('effectif_cec', 'EFFECTIF_CEC_MOINS_DE_6_HANDICAP_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_cec', 'EFFECTIF_CEC_MOINS_DE_6_HANDICAP_F', 'INT NULL');

CALL prism_add_column_if_missing('evaluation_theme_taux', 'NOMBRE_TOTAL_EVALUE', 'INT NULL');
CALL prism_add_column_if_missing('evaluation_theme_taux', 'NOMBRE_RESULTAT_OBTENU', 'INT NULL');

CALL prism_add_column_if_missing('fonction', 'TYPE_CENTRE', 'VARCHAR(20) NULL');

CALL prism_add_column_if_missing('periode_activite', 'DATE_DEBUT', 'DATE NULL');
CALL prism_add_column_if_missing('periode_activite', 'DATE_FIN', 'DATE NULL');

CALL prism_add_column_if_missing('personnephysique', 'MAIL', 'VARCHAR(150) NULL');
CALL prism_add_column_if_missing('personnephysique', 'ORGANISATION_FAITIERE', 'VARCHAR(150) NULL');
