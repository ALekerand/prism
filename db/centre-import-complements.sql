-- Complements pour l'import et la creation des centres.
-- Tous les champs sont nullable afin que les lignes partiellement propres restent enregistrables.

CREATE TABLE IF NOT EXISTS `region` (
  `id_region` int NOT NULL AUTO_INCREMENT,
  `code_region` varchar(10) DEFAULT NULL,
  `libelle_region` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
    CONCAT(
      'ALTER TABLE `',
      REPLACE(p_table_name, '`', '``'),
      '` ADD COLUMN `',
      REPLACE(p_column_name, '`', '``'),
      '` ',
      p_column_definition
    ),
    'SELECT 1'
  );
  PREPARE add_column_stmt FROM @ddl;
  EXECUTE add_column_stmt;
  DEALLOCATE PREPARE add_column_stmt;
END//
DELIMITER ;

CALL prism_add_column_if_missing('departement', 'id_region', 'int DEFAULT NULL');

SET @fk_departement_region_exists := (
  SELECT COUNT(*)
  FROM information_schema.TABLE_CONSTRAINTS
  WHERE CONSTRAINT_SCHEMA = DATABASE()
    AND TABLE_NAME = 'departement'
    AND CONSTRAINT_NAME = 'fk_departement_region'
);
SET @fk_departement_region_sql := IF(
  @fk_departement_region_exists = 0,
  'ALTER TABLE `departement` ADD CONSTRAINT `fk_departement_region` FOREIGN KEY (`id_region`) REFERENCES `region` (`id_region`)',
  'SELECT 1'
);
PREPARE fk_departement_region_stmt FROM @fk_departement_region_sql;
EXECUTE fk_departement_region_stmt;
DEALLOCATE PREPARE fk_departement_region_stmt;

CALL prism_add_column_if_missing('centre', 'total_apprenants', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('centre', 'total_hommes', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('centre', 'total_femmes', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('centre', 'latitude_gps', 'varchar(50) DEFAULT NULL');
CALL prism_add_column_if_missing('centre', 'longitude_gps', 'varchar(50) DEFAULT NULL');
CALL prism_add_column_if_missing('centre', 'gps_valide', 'bit(1) DEFAULT NULL');
CALL prism_add_column_if_missing('centre', 'structure_partenaire', 'varchar(150) DEFAULT NULL');
CALL prism_add_column_if_missing('centre', 'nom_partenaire', 'varchar(150) DEFAULT NULL');

CALL prism_add_column_if_missing('alpha', 'total_apprenants', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('alpha', 'total_hommes', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('alpha', 'total_femmes', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('alpha', 'latitude_gps', 'varchar(50) DEFAULT NULL');
CALL prism_add_column_if_missing('alpha', 'longitude_gps', 'varchar(50) DEFAULT NULL');
CALL prism_add_column_if_missing('alpha', 'gps_valide', 'bit(1) DEFAULT NULL');
CALL prism_add_column_if_missing('alpha', 'structure_partenaire', 'varchar(150) DEFAULT NULL');
CALL prism_add_column_if_missing('alpha', 'nom_partenaire', 'varchar(150) DEFAULT NULL');

CALL prism_add_column_if_missing('cp', 'total_apprenants', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('cp', 'total_hommes', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('cp', 'total_femmes', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('cp', 'latitude_gps', 'varchar(50) DEFAULT NULL');
CALL prism_add_column_if_missing('cp', 'longitude_gps', 'varchar(50) DEFAULT NULL');
CALL prism_add_column_if_missing('cp', 'gps_valide', 'bit(1) DEFAULT NULL');
CALL prism_add_column_if_missing('cp', 'structure_partenaire', 'varchar(150) DEFAULT NULL');
CALL prism_add_column_if_missing('cp', 'nom_partenaire', 'varchar(150) DEFAULT NULL');

CALL prism_add_column_if_missing('cec', 'total_apprenants', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('cec', 'total_hommes', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('cec', 'total_femmes', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('cec', 'latitude_gps', 'varchar(50) DEFAULT NULL');
CALL prism_add_column_if_missing('cec', 'longitude_gps', 'varchar(50) DEFAULT NULL');
CALL prism_add_column_if_missing('cec', 'gps_valide', 'bit(1) DEFAULT NULL');
CALL prism_add_column_if_missing('cec', 'structure_partenaire', 'varchar(150) DEFAULT NULL');
CALL prism_add_column_if_missing('cec', 'nom_partenaire', 'varchar(150) DEFAULT NULL');

CALL prism_add_column_if_missing('sie', 'total_apprenants', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('sie', 'total_hommes', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('sie', 'total_femmes', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('sie', 'latitude_gps', 'varchar(50) DEFAULT NULL');
CALL prism_add_column_if_missing('sie', 'longitude_gps', 'varchar(50) DEFAULT NULL');
CALL prism_add_column_if_missing('sie', 'gps_valide', 'bit(1) DEFAULT NULL');
CALL prism_add_column_if_missing('sie', 'structure_partenaire', 'varchar(150) DEFAULT NULL');
CALL prism_add_column_if_missing('sie', 'nom_partenaire', 'varchar(150) DEFAULT NULL');

DROP PROCEDURE IF EXISTS prism_add_column_if_missing;
