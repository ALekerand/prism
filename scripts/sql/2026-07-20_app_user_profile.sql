-- PRISM : profil utilisateur (nom, prénoms, dates)
-- Idempotent via prism_add_column_if_missing (voir 00_prism_sql_helpers.sql).

CALL prism_add_column_if_missing('app_user', 'NOM', 'VARCHAR(100) NULL');
CALL prism_add_column_if_missing('app_user', 'PRENOMS', 'VARCHAR(100) NULL');
CALL prism_add_column_if_missing('app_user', 'DATE_NAISSANCE', 'DATE NULL');
CALL prism_add_column_if_missing('app_user', 'LIEU_NAISSANCE', 'VARCHAR(150) NULL');
CALL prism_add_column_if_missing('app_user', 'DATE_PRISE_SERVICE', 'DATE NULL');
CALL prism_add_column_if_missing('app_user', 'DATE_DEPART_RETRAITE', 'DATE NULL');
