-- Ajoute la colonne propriétaire (conseiller auteur) pour le filtrage commission.
SET @col := (
  SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'saisie_workflow' AND COLUMN_NAME = 'PROPRIETAIRE'
);
SET @ddl := IF(@col = 0, 'ALTER TABLE saisie_workflow ADD COLUMN PROPRIETAIRE VARCHAR(100) NULL', 'SELECT 1');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
