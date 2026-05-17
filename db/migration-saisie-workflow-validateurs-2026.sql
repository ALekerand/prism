-- Traçabilité validateur par palier (onglets « Déjà validé par moi »)
-- Idempotent : n'ajoute une colonne que si elle n'existe pas encore.

SET @db := DATABASE();

SET @sql := (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE saisie_workflow ADD COLUMN VALIDE_COORD_PAR VARCHAR(100) NULL AFTER DECIDE_PAR',
    'SELECT ''VALIDE_COORD_PAR déjà présente'' AS info'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @db
    AND TABLE_NAME = 'saisie_workflow'
    AND COLUMN_NAME = 'VALIDE_COORD_PAR'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql := (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE saisie_workflow ADD COLUMN VALIDE_SUP_PAR VARCHAR(100) NULL AFTER VALIDE_COORD_PAR',
    'SELECT ''VALIDE_SUP_PAR déjà présente'' AS info'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @db
    AND TABLE_NAME = 'saisie_workflow'
    AND COLUMN_NAME = 'VALIDE_SUP_PAR'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql := (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE saisie_workflow ADD COLUMN VALIDE_CENTRAL_PAR VARCHAR(100) NULL AFTER VALIDE_SUP_PAR',
    'SELECT ''VALIDE_CENTRAL_PAR déjà présente'' AS info'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @db
    AND TABLE_NAME = 'saisie_workflow'
    AND COLUMN_NAME = 'VALIDE_CENTRAL_PAR'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
