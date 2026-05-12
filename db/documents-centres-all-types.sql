-- Generalise les documents de centre a tous les types de centre.
-- Avant : document.ID_CENTRE reference alpha.ID_CENTRE.
-- Apres : document.ID_CENTRE reference centre.ID_CENTRE.

SET @old_fk := (
  SELECT CONSTRAINT_NAME
  FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'document'
    AND COLUMN_NAME = 'ID_CENTRE'
    AND REFERENCED_TABLE_NAME = 'alpha'
  LIMIT 1
);

SET @drop_sql := IF(
  @old_fk IS NULL,
  'SELECT 1',
  CONCAT('ALTER TABLE document DROP FOREIGN KEY `', REPLACE(@old_fk, '`', '``'), '`')
);
PREPARE stmt FROM @drop_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @new_fk_exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'document'
    AND COLUMN_NAME = 'ID_CENTRE'
    AND REFERENCED_TABLE_NAME = 'centre'
);

SET @add_sql := IF(
  @new_fk_exists = 0,
  'ALTER TABLE document ADD CONSTRAINT fk_document_centre FOREIGN KEY (ID_CENTRE) REFERENCES centre (ID_CENTRE)',
  'SELECT 1'
);
PREPARE stmt FROM @add_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
