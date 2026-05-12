-- Rattachement des utilisateurs a leurs circonscriptions de travail.
-- Colonnes nullable pour conserver les comptes administratifs/globaux sans périmètre.

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

CALL prism_add_column_if_missing('app_user', 'id_region', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('app_user', 'id_drena', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('app_user', 'id_iep', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('app_user', 'id_departement', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('app_user', 'id_sous_prefecture', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('app_user', 'id_commune', 'int DEFAULT NULL');
CALL prism_add_column_if_missing('app_user', 'id_localite', 'int DEFAULT NULL');

DROP PROCEDURE IF EXISTS prism_add_column_if_missing;
