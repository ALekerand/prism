-- Evolution de l'évaluation périodique :
-- - type d'évaluation porté par l'évaluation
-- - plusieurs thèmes avec taux saisi pour une même évaluation
-- Script idempotent pour MySQL.

SET @col_exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'evaluation'
    AND COLUMN_NAME = 'TYPE_EVALUATION'
);

SET @ddl := IF(
  @col_exists = 0,
  'ALTER TABLE evaluation ADD COLUMN TYPE_EVALUATION VARCHAR(30) NULL',
  'SELECT 1'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS evaluation_theme_taux (
  ID_EVALUATION_THEME_TAUX INT NOT NULL AUTO_INCREMENT,
  ID_EVALUATION INT NOT NULL,
  ID_THEME_EVALUATION INT NOT NULL,
  TAUX INT NOT NULL,
  PRIMARY KEY (ID_EVALUATION_THEME_TAUX),
  INDEX idx_evaluation_theme_taux_evaluation (ID_EVALUATION),
  INDEX idx_evaluation_theme_taux_theme (ID_THEME_EVALUATION),
  CONSTRAINT fk_evaluation_theme_taux_evaluation
    FOREIGN KEY (ID_EVALUATION) REFERENCES evaluation (ID_EVALUATION)
    ON DELETE CASCADE,
  CONSTRAINT fk_evaluation_theme_taux_theme
    FOREIGN KEY (ID_THEME_EVALUATION) REFERENCES theme_evaluation (ID_THEME_EVALUATION)
);
