-- Helpers PRISM pour migrations idempotentes (MySQL 8.0.x)

DROP PROCEDURE IF EXISTS prism_add_column_if_missing;

DELIMITER $$

CREATE PROCEDURE prism_add_column_if_missing(
  IN p_table VARCHAR(64),
  IN p_column VARCHAR(64),
  IN p_definition VARCHAR(255)
)
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table
      AND COLUMN_NAME = p_column
  ) THEN
    SET @sql = CONCAT(
      'ALTER TABLE `',
      REPLACE(p_table, '`', '``'),
      '` ADD COLUMN `',
      REPLACE(p_column, '`', '``'),
      '` ',
      p_definition
    );
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END$$

DELIMITER ;
