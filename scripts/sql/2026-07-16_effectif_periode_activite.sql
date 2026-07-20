-- PRISM : période d'activité sur effectifs CP/SIE / CEPE / intégration / abandon / handicap / promus / reverse
-- À exécuter sur staging/prod (ddl-auto=validate) avant déploiement.
-- Idempotent via prism_add_column_if_missing (voir 00_prism_sql_helpers.sql).

CALL prism_add_column_if_missing('effectif_cp', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_sie', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_abandon_cp', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_abandon_cec', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_abondan_sie', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_situation_handicap_cp', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_situation_handicap_cec', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_situation_handicap_sie', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_cepe_cp', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_cepe_cec', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_admis_integration_cp', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_integration_formel_cp', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_promu_sie', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_promu_cec', 'ID_PERIODE_ACTIVITE', 'INT NULL');
CALL prism_add_column_if_missing('effectif_reverse_formel_sie', 'ID_PERIODE_ACTIVITE', 'INT NULL');

-- FKs optionnelles (Hibernate peut les gérer en local) :
-- ALTER TABLE ... ADD CONSTRAINT fk_... FOREIGN KEY (ID_PERIODE_ACTIVITE) REFERENCES periode_activite (ID_PERIODE_ACTIVITE);
