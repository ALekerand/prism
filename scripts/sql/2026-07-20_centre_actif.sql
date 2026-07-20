-- PRISM : colonne centre.ACTIF (activation / désactivation)
-- Idempotent via prism_add_column_if_missing (voir 00_prism_sql_helpers.sql).

CALL prism_add_column_if_missing('centre', 'ACTIF', 'TINYINT(1) NULL DEFAULT 1');

UPDATE centre SET ACTIF = 1 WHERE ACTIF IS NULL;
