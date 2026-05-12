-- Migration du nouveau workflow ACTIVITES CENTRE.
-- Le workflow concerne uniquement les centres Alpha : ID_ALPHA référence alpha(ID_CENTRE).

SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.columns
  WHERE table_schema = DATABASE()
    AND table_name = 'visite'
    AND column_name = 'VALIDEE_COORDONNATEUR'
);
SET @ddl := IF(@col_exists = 0,
  'ALTER TABLE visite ADD COLUMN VALIDEE_COORDONNATEUR TINYINT(1) DEFAULT 0',
  'SELECT 1'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS suivi_iepp (
  ID_SUIVI_IEPP INT NOT NULL AUTO_INCREMENT,
  ID_ALPHA INT NOT NULL,
  NOMBRE_VISITE_EFFECTUE_PAR_IEPP INT NULL,
  NOMBRE_REUNION_POINT_ACTIVITE_ALPHA INT NULL,
  VALIDEE_IEPP TINYINT(1) DEFAULT 0,
  PRIMARY KEY (ID_SUIVI_IEPP),
  INDEX IDX_SUIVI_IEPP_ALPHA (ID_ALPHA),
  CONSTRAINT FK_SUIVI_IEPP_ALPHA FOREIGN KEY (ID_ALPHA) REFERENCES alpha (ID_CENTRE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS suivi_superviseur (
  ID_SUIVI_SUPERVISEUR INT NOT NULL AUTO_INCREMENT,
  ID_ALPHA INT NOT NULL,
  NOMBRE_VISITE_SUPERVISEUR_EFFECTUE INT NULL,
  NOMBRE_REUNION_BILAN_SUPERVISEUR INT NULL,
  VALIDEE_SUPERVISEUR TINYINT(1) DEFAULT 0,
  PRIMARY KEY (ID_SUIVI_SUPERVISEUR),
  INDEX IDX_SUIVI_SUPERVISEUR_ALPHA (ID_ALPHA),
  CONSTRAINT FK_SUIVI_SUPERVISEUR_ALPHA FOREIGN KEY (ID_ALPHA) REFERENCES alpha (ID_CENTRE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP PROCEDURE IF EXISTS prism_add_column_if_missing;
DELIMITER //
CREATE PROCEDURE prism_add_column_if_missing(
  IN p_table_name varchar(64),
  IN p_column_name varchar(64),
  IN p_column_definition text
)
BEGIN
  SET @column_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table_name
      AND COLUMN_NAME = p_column_name
  );
  SET @ddl := IF(
    @column_exists = 0,
    CONCAT('ALTER TABLE `', REPLACE(p_table_name, '`', '``'), '` ADD COLUMN `', REPLACE(p_column_name, '`', '``'), '` ', p_column_definition),
    'SELECT 1'
  );
  PREPARE add_column_stmt FROM @ddl;
  EXECUTE add_column_stmt;
  DEALLOCATE PREPARE add_column_stmt;
END//
DELIMITER ;

CALL prism_add_column_if_missing('controle', 'VALIDEE_COORDONNATEUR', 'TINYINT(1) DEFAULT 0');
CALL prism_add_column_if_missing('controle', 'VALIDEE_SUPERVISEUR', 'TINYINT(1) DEFAULT 0');
CALL prism_add_column_if_missing('controle', 'VALIDEE_CENTRALE', 'TINYINT(1) DEFAULT 0');

CALL prism_add_column_if_missing('performance', 'VALIDEE_COORDONNATEUR', 'TINYINT(1) DEFAULT 0');
CALL prism_add_column_if_missing('performance', 'VALIDEE_SUPERVISEUR', 'TINYINT(1) DEFAULT 0');
CALL prism_add_column_if_missing('performance', 'VALIDEE_CENTRALE', 'TINYINT(1) DEFAULT 0');

CALL prism_add_column_if_missing('evaluation', 'VALIDEE_COORDONNATEUR', 'TINYINT(1) DEFAULT 0');
CALL prism_add_column_if_missing('evaluation', 'VALIDEE_SUPERVISEUR', 'TINYINT(1) DEFAULT 0');
CALL prism_add_column_if_missing('evaluation', 'VALIDEE_CENTRALE', 'TINYINT(1) DEFAULT 0');

DROP PROCEDURE IF EXISTS prism_add_column_if_missing;
