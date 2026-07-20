-- PRISM : effectif total H / F (harmonisation avec Alpha)
-- À exécuter sur staging/prod (ddl-auto=validate) avant déploiement.
-- Idempotent via prism_add_column_if_missing (voir 00_prism_sql_helpers.sql).

CALL prism_add_column_if_missing('effectif_cp', 'EFFECTIF_CP_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_cp', 'EFFECTIF_CP_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_cec', 'EFFECTIF_CEC_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_cec', 'EFFECTIF_CEC_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_sie', 'EFFECTIF_SIE_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_sie', 'EFFECTIF_SIE_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_abandon_cp', 'EFFECTIF_ABANDON_CP_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_abandon_cp', 'EFFECTIF_ABANDON_CP_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_abandon_cec', 'EFFECTIF_ABANDON_CEC_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_abandon_cec', 'EFFECTIF_ABANDON_CEC_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_abondan_sie', 'EFFECTIF_ABANDON_SIE_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_abondan_sie', 'EFFECTIF_ABANDON_SIE_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_situation_handicap_cp', 'EFFECTIF_SIT_HANDICAP_CP_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_situation_handicap_cp', 'EFFECTIF_SIT_HANDICAP_CP_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_situation_handicap_cec', 'EFFECTIF_SIT_HANDICAP_CEC_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_situation_handicap_cec', 'EFFECTIF_SIT_HANDICAP_CEC_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_situation_handicap_sie', 'EFFECTIF_SIT_HANDICAP_SIE_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_situation_handicap_sie', 'EFFECTIF_SIT_HANDICAP_SIE_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_admis_integration_cp', 'EFFECTIF_ADMIS_INT_CP_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_admis_integration_cp', 'EFFECTIF_ADMIS_INT_CP_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_integration_formel_cp', 'EFFECTIF_INT_FORMEL_CP_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_integration_formel_cp', 'EFFECTIF_INT_FORMEL_CP_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_promu_sie', 'EFFECTIF_PROMU_SIE_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_promu_sie', 'EFFECTIF_PROMU_SIE_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_promu_cec', 'EFFECTIF_PROMU_CEC_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_promu_cec', 'EFFECTIF_PROMU_CEC_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_reverse_formel_sie', 'EFFECTIF_REV_FORMEL_SIE_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_reverse_formel_sie', 'EFFECTIF_REV_FORMEL_SIE_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_cepe_cp', 'EFFECTIF_CEPE_CP_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_cepe_cp', 'EFFECTIF_CEPE_CP_NIVEAU_F', 'INT NULL');

CALL prism_add_column_if_missing('effectif_cepe_cec', 'EFFECTIF_CEPE_CEC_NIVEAU_H', 'INT NULL');
CALL prism_add_column_if_missing('effectif_cepe_cec', 'EFFECTIF_CEPE_CEC_NIVEAU_F', 'INT NULL');
