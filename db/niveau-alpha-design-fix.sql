-- Correction de conception:
-- - niveau_alpha devient un referentiel global.
-- - alpha_niveau porte le rattachement entre un centre Alpha et ses niveaux.
-- Script idempotent MySQL.

START TRANSACTION;

SET @fk_name := (
  SELECT CONSTRAINT_NAME
  FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'niveau_alpha'
    AND COLUMN_NAME = 'id_centre'
    AND REFERENCED_TABLE_NAME = 'alpha'
  LIMIT 1
);

SET @ddl := IF(
  @fk_name IS NOT NULL,
  CONCAT('ALTER TABLE niveau_alpha DROP FOREIGN KEY ', @fk_name),
  'SELECT 1'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS alpha_niveau (
  id_alpha_niveau INT NOT NULL AUTO_INCREMENT,
  id_centre INT NOT NULL,
  id_niveau_alpha INT NOT NULL,
  PRIMARY KEY (id_alpha_niveau),
  UNIQUE KEY uk_alpha_niveau_centre_niveau (id_centre, id_niveau_alpha),
  KEY fk_alpha_niveau_niveau (id_niveau_alpha),
  CONSTRAINT fk_alpha_niveau_centre FOREIGN KEY (id_centre) REFERENCES alpha (id_centre),
  CONSTRAINT fk_alpha_niveau_niveau FOREIGN KEY (id_niveau_alpha) REFERENCES niveau_alpha (id_niveau_alpha)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET @has_id_centre := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'niveau_alpha'
    AND COLUMN_NAME = 'id_centre'
);

SET @ddl := IF(
  @has_id_centre > 0,
  'ALTER TABLE niveau_alpha MODIFY id_centre INT NULL',
  'SELECT 1'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @ddl := IF(
  @has_id_centre > 0,
  'INSERT INTO niveau_alpha (code_niveau_alpha, libelle_niveau_alpha) SELECT ''NAL-NIVEAU1'', ''NIVEAU 1'' WHERE NOT EXISTS (SELECT 1 FROM niveau_alpha WHERE id_centre IS NULL AND UPPER(REPLACE(libelle_niveau_alpha, ''_'', '' '')) = ''NIVEAU 1'')',
  'INSERT INTO niveau_alpha (code_niveau_alpha, libelle_niveau_alpha) SELECT ''NAL-NIVEAU1'', ''NIVEAU 1'' WHERE NOT EXISTS (SELECT 1 FROM niveau_alpha WHERE UPPER(REPLACE(libelle_niveau_alpha, ''_'', '' '')) = ''NIVEAU 1'')'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @ddl := IF(
  @has_id_centre > 0,
  'INSERT INTO niveau_alpha (code_niveau_alpha, libelle_niveau_alpha) SELECT ''NAL-NIVEAU2'', ''NIVEAU 2'' WHERE NOT EXISTS (SELECT 1 FROM niveau_alpha WHERE id_centre IS NULL AND UPPER(REPLACE(libelle_niveau_alpha, ''_'', '' '')) = ''NIVEAU 2'')',
  'INSERT INTO niveau_alpha (code_niveau_alpha, libelle_niveau_alpha) SELECT ''NAL-NIVEAU2'', ''NIVEAU 2'' WHERE NOT EXISTS (SELECT 1 FROM niveau_alpha WHERE UPPER(REPLACE(libelle_niveau_alpha, ''_'', '' '')) = ''NIVEAU 2'')'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @ddl := IF(
  @has_id_centre > 0,
  'INSERT INTO niveau_alpha (code_niveau_alpha, libelle_niveau_alpha) SELECT ''NAL-POSTALPHA'', ''POST ALPHA'' WHERE NOT EXISTS (SELECT 1 FROM niveau_alpha WHERE id_centre IS NULL AND UPPER(REPLACE(REPLACE(libelle_niveau_alpha, ''_'', '' ''), ''-'', '' '')) LIKE ''%POST%ALPHA%'')',
  'INSERT INTO niveau_alpha (code_niveau_alpha, libelle_niveau_alpha) SELECT ''NAL-POSTALPHA'', ''POST ALPHA'' WHERE NOT EXISTS (SELECT 1 FROM niveau_alpha WHERE UPPER(REPLACE(REPLACE(libelle_niveau_alpha, ''_'', '' ''), ''-'', '' '')) LIKE ''%POST%ALPHA%'')'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

DROP TEMPORARY TABLE IF EXISTS tmp_niveau_alpha_mapping;
SET @ddl := IF(
  @has_id_centre > 0,
  'CREATE TEMPORARY TABLE tmp_niveau_alpha_mapping AS SELECT old.id_niveau_alpha AS old_id, old.id_centre AS id_centre, target.id_niveau_alpha AS new_id FROM niveau_alpha old JOIN niveau_alpha target ON target.id_centre IS NULL AND target.libelle_niveau_alpha = CASE WHEN UPPER(REPLACE(REPLACE(old.libelle_niveau_alpha, ''_'', '' ''), ''-'', '' '')) LIKE ''%POST%'' OR UPPER(old.libelle_niveau_alpha) LIKE ''%3%'' THEN ''POST ALPHA'' WHEN UPPER(REPLACE(old.libelle_niveau_alpha, ''_'', '' '')) LIKE ''%2%'' THEN ''NIVEAU 2'' ELSE ''NIVEAU 1'' END WHERE old.id_centre IS NOT NULL',
  'CREATE TEMPORARY TABLE tmp_niveau_alpha_mapping AS SELECT NULL AS old_id, NULL AS id_centre, NULL AS new_id WHERE 1 = 0'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

INSERT INTO alpha_niveau (id_centre, id_niveau_alpha)
SELECT DISTINCT id_centre, new_id
FROM tmp_niveau_alpha_mapping
WHERE id_centre IS NOT NULL
  AND new_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1
    FROM alpha_niveau an
    WHERE an.id_centre = tmp_niveau_alpha_mapping.id_centre
      AND an.id_niveau_alpha = tmp_niveau_alpha_mapping.new_id
  );

SET @controle_col_exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'controle'
    AND COLUMN_NAME = 'id_niveau_alpha'
);

SET @ddl := IF(
  @controle_col_exists > 0,
  'UPDATE controle c JOIN tmp_niveau_alpha_mapping m ON m.old_id = c.id_niveau_alpha SET c.id_niveau_alpha = m.new_id',
  'SELECT 1'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @effectif_col_exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'effectif_alpha'
    AND COLUMN_NAME = 'id_niveau_alpha'
);

SET @ddl := IF(
  @effectif_col_exists > 0,
  'UPDATE effectif_alpha e JOIN tmp_niveau_alpha_mapping m ON m.old_id = e.id_niveau_alpha SET e.id_niveau_alpha = m.new_id',
  'SELECT 1'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

DELETE old
FROM niveau_alpha old
JOIN tmp_niveau_alpha_mapping m ON m.old_id = old.id_niveau_alpha;

DROP TEMPORARY TABLE IF EXISTS tmp_niveau_alpha_mapping;

SET @idx_name := (
  SELECT INDEX_NAME
  FROM INFORMATION_SCHEMA.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'niveau_alpha'
    AND COLUMN_NAME = 'id_centre'
  LIMIT 1
);

SET @ddl := IF(
  @idx_name IS NOT NULL AND @has_id_centre > 0,
  CONCAT('ALTER TABLE niveau_alpha DROP INDEX ', @idx_name),
  'SELECT 1'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @ddl := IF(
  @has_id_centre > 0,
  'ALTER TABLE niveau_alpha DROP COLUMN id_centre',
  'SELECT 1'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

COMMIT;
