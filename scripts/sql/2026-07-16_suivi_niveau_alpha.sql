-- Niveau Alpha sur les suivis superviseur et IEPP (aligné sur points de visite / contrôle).
-- À exécuter sur staging/prod (ddl-auto=validate) avant déploiement.
-- Idempotent via prism_add_column_if_missing (voir 00_prism_sql_helpers.sql).

CALL prism_add_column_if_missing('suivi_iepp', 'ID_NIVEAU_ALPHA', 'INT NULL');
CALL prism_add_column_if_missing('suivi_superviseur', 'ID_NIVEAU_ALPHA', 'INT NULL');
