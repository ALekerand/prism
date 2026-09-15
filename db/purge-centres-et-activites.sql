-- =============================================================================
-- PRISM — Purge centres + toutes activités / effectifs / documents liés
-- + workflows orphelins. Conserve : référentiels, utilisateurs, rôles, promoteurs.
--
-- Usage :
--   mysql -u root prism_bd < db/purge-centres-et-activites.sql
--
-- ATTENTION : opération destructive sur les données métier centres.
-- =============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Workflows transversaux (effectifs, activités centre, visites…)
TRUNCATE TABLE saisie_workflow;

-- Contrôle
TRUNCATE TABLE controle_horaire_formation;
TRUNCATE TABLE controle_kit_manuel;
TRUNCATE TABLE controle;

-- Évaluation
TRUNCATE TABLE evaluation_theme_taux;
TRUNCATE TABLE evaluation;

-- Autres activités centre
TRUNCATE TABLE performance;
TRUNCATE TABLE visite;
TRUNCATE TABLE suivi_superviseur;
TRUNCATE TABLE suivi_iepp;

-- Documents dossier centre
TRUNCATE TABLE document;

-- Effectifs (toutes variantes)
TRUNCATE TABLE effectif_abandon_alpha;
TRUNCATE TABLE effectif_abandon_cec;
TRUNCATE TABLE effectif_abandon_cp;
TRUNCATE TABLE effectif_abondan_sie;
TRUNCATE TABLE effectif_admis_integration_cp;
TRUNCATE TABLE effectif_alpha;
TRUNCATE TABLE effectif_cec;
TRUNCATE TABLE effectif_cepe_cec;
TRUNCATE TABLE effectif_cepe_cp;
TRUNCATE TABLE effectif_cp;
TRUNCATE TABLE effectif_integration_formel_cp;
TRUNCATE TABLE effectif_passage_alpha;
TRUNCATE TABLE effectif_promu_cec;
TRUNCATE TABLE effectif_promu_cp;
TRUNCATE TABLE effectif_promu_sie;
TRUNCATE TABLE effectif_reverse_formel_sie;
TRUNCATE TABLE effectif_sie;
TRUNCATE TABLE effectif_situation_handicap_alpha;
TRUNCATE TABLE effectif_situation_handicap_cec;
TRUNCATE TABLE effectif_situation_handicap_cp;
TRUNCATE TABLE effectif_situation_handicap_sie;

-- Personnel rattaché aux centres
TRUNCATE TABLE personnel;

-- Enfants Alpha (centre)
TRUNCATE TABLE alpha_niveau;
TRUNCATE TABLE modealphabetisation;
TRUNCATE TABLE domaine_activite_alpha;
TRUNCATE TABLE programme_alpha;

-- Enfants centre générique
TRUNCATE TABLE appui_partenaire;
TRUNCATE TABLE competence_centre;
TRUNCATE TABLE difficulte_alpha;
TRUNCATE TABLE impact_alpha;
TRUNCATE TABLE infrastructure_centre;
TRUNCATE TABLE langue_apprentissage;
TRUNCATE TABLE materiel_alpha;
TRUNCATE TABLE ressource_financiere_materiel;
TRUNCATE TABLE support_didactique_alpha;

-- Niveaux par type
TRUNCATE TABLE cec_niveau;
TRUNCATE TABLE cp_niveau;
TRUNCATE TABLE sie_niveau;

-- Typage centre (Alpha / CEC / CP / SIE) puis centre
TRUNCATE TABLE alpha;
TRUNCATE TABLE cec;
TRUNCATE TABLE cp;
TRUNCATE TABLE sie;
TRUNCATE TABLE centre;

SET FOREIGN_KEY_CHECKS = 1;

-- Contrôle post-purge (doit retourner 0 partout)
SELECT 'centre' AS table_name, COUNT(*) AS nb FROM centre
UNION ALL SELECT 'alpha', COUNT(*) FROM alpha
UNION ALL SELECT 'controle', COUNT(*) FROM controle
UNION ALL SELECT 'controle_orphan', COUNT(*) FROM controle c
    LEFT JOIN alpha a ON c.id_alpha = a.id_centre WHERE a.id_centre IS NULL
UNION ALL SELECT 'evaluation', COUNT(*) FROM evaluation
UNION ALL SELECT 'performance', COUNT(*) FROM performance
UNION ALL SELECT 'visite', COUNT(*) FROM visite
UNION ALL SELECT 'document', COUNT(*) FROM document
UNION ALL SELECT 'effectif_alpha', COUNT(*) FROM effectif_alpha
UNION ALL SELECT 'personnel', COUNT(*) FROM personnel
UNION ALL SELECT 'saisie_workflow', COUNT(*) FROM saisie_workflow;
