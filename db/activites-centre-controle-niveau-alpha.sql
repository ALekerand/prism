-- Contrôle démarrage : le niveau sélectionné est le niveau Alpha.
-- Script idempotent pour MySQL.

SET @col_exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'controle'
    AND COLUMN_NAME = 'ID_NIVEAU_ALPHA'
);

SET @ddl := IF(
  @col_exists = 0,
  'ALTER TABLE controle ADD COLUMN ID_NIVEAU_ALPHA INT NULL',
  'SELECT 1'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'controle'
    AND CONSTRAINT_NAME = 'fk_controle_niveau_alpha'
);

SET @ddl := IF(
  @fk_exists = 0,
  'ALTER TABLE controle ADD CONSTRAINT fk_controle_niveau_alpha FOREIGN KEY (ID_NIVEAU_ALPHA) REFERENCES niveau_alpha (ID_NIVEAU_ALPHA)',
  'SELECT 1'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
