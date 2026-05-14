-- =============================================================================
-- Vérification rapide de la préparation "Commissions 1 à 4"
-- Objectif: contrôler la conformité des comptes et du workflow de validation.
--
-- Exécution:
--   mysql -u <user> -p <db> < db/commissions-1-4-verification.sql
--
-- Ce script est en lecture seule (SELECT uniquement).
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 0) Référence des rôles attendus
-- -----------------------------------------------------------------------------
SELECT id_role, code_role, libelle_role
FROM app_role
WHERE code_role IN ('CONSEILLER', 'COORDONNATEUR', 'SUPERVISEUR', 'SUPERVISEUR_AENF', 'IEPP', 'DIRECTEUR')
ORDER BY code_role;

-- -----------------------------------------------------------------------------
-- 1) Contrôle des comptes présents par commission
-- Attendu par commission:
--   - 9 conseillers
--   - 3 coordonnateurs
--   - 1 superviseur DRENA
-- Et au national:
--   - 1 superviseur_dcspa_national
-- -----------------------------------------------------------------------------
WITH per_commission AS (
  SELECT
    CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(u.username, '_', 2), '_', -1) AS UNSIGNED) AS commission_id,
    SUM(CASE WHEN u.username REGEXP '^commission_[1-4]_conseiller_[1-9]$' THEN 1 ELSE 0 END) AS nb_conseillers,
    SUM(CASE WHEN u.username REGEXP '^commission_[1-4]_coordonnateur_[1-3]$' THEN 1 ELSE 0 END) AS nb_coordonnateurs,
    SUM(CASE WHEN u.username REGEXP '^commission_[1-4]_superviseur_1$' THEN 1 ELSE 0 END) AS nb_superviseurs_drena
  FROM app_user u
  WHERE u.username REGEXP '^commission_[1-4]_(conseiller|coordonnateur|superviseur)_[0-9]+$'
  GROUP BY CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(u.username, '_', 2), '_', -1) AS UNSIGNED)
)
SELECT
  commission_id,
  nb_conseillers,
  nb_coordonnateurs,
  nb_superviseurs_drena,
  CASE WHEN nb_conseillers = 9 THEN 'OK' ELSE 'KO' END AS check_conseillers,
  CASE WHEN nb_coordonnateurs = 3 THEN 'OK' ELSE 'KO' END AS check_coordonnateurs,
  CASE WHEN nb_superviseurs_drena = 1 THEN 'OK' ELSE 'KO' END AS check_superviseur_drena
FROM per_commission
ORDER BY commission_id;

SELECT
  COUNT(*) AS nb_superviseur_dcspa_national,
  CASE WHEN COUNT(*) = 1 THEN 'OK' ELSE 'KO' END AS check_superviseur_national
FROM app_user
WHERE username = 'superviseur_dcspa_national';

-- -----------------------------------------------------------------------------
-- 2) Contrôle des rôles appliqués aux comptes commission
-- -----------------------------------------------------------------------------
SELECT
  u.username,
  GROUP_CONCAT(r.code_role ORDER BY r.code_role SEPARATOR ', ') AS roles
FROM app_user u
LEFT JOIN user_role ur ON ur.id_user = u.id_user
LEFT JOIN app_role r ON r.id_role = ur.id_role
WHERE u.username REGEXP '^commission_[1-4]_(conseiller|coordonnateur|superviseur)_'
   OR u.username = 'superviseur_dcspa_national'
GROUP BY u.id_user, u.username
ORDER BY u.username;

-- -----------------------------------------------------------------------------
-- 3) Contrôle de la répartition IEPP dans chaque commission
-- Attendu:
--   - conseillers: 3 + 3 + 3 sur 3 IEPP
--   - coordonnateurs: 1 + 1 + 1 sur 3 IEPP
-- -----------------------------------------------------------------------------
WITH users_commission AS (
  SELECT
    CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(u.username, '_', 2), '_', -1) AS UNSIGNED) AS commission_id,
    CASE
      WHEN u.username REGEXP '^commission_[1-4]_conseiller_[0-9]+$' THEN 'CONSEILLER'
      WHEN u.username REGEXP '^commission_[1-4]_coordonnateur_[0-9]+$' THEN 'COORDONNATEUR'
      ELSE 'AUTRE'
    END AS profil,
    u.id_iep
  FROM app_user u
  WHERE u.username REGEXP '^commission_[1-4]_(conseiller|coordonnateur)_[0-9]+$'
),
agg AS (
  SELECT commission_id, profil, id_iep, COUNT(*) AS nb
  FROM users_commission
  GROUP BY commission_id, profil, id_iep
)
SELECT *
FROM agg
ORDER BY commission_id, profil, id_iep;

WITH users_commission AS (
  SELECT
    CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(u.username, '_', 2), '_', -1) AS UNSIGNED) AS commission_id,
    CASE
      WHEN u.username REGEXP '^commission_[1-4]_conseiller_[0-9]+$' THEN 'CONSEILLER'
      WHEN u.username REGEXP '^commission_[1-4]_coordonnateur_[0-9]+$' THEN 'COORDONNATEUR'
      ELSE 'AUTRE'
    END AS profil,
    u.id_iep
  FROM app_user u
  WHERE u.username REGEXP '^commission_[1-4]_(conseiller|coordonnateur)_[0-9]+$'
),
stat AS (
  SELECT
    commission_id,
    profil,
    COUNT(DISTINCT id_iep) AS nb_iepp_distinct,
    MIN(nb_per_iep) AS min_par_iepp,
    MAX(nb_per_iep) AS max_par_iepp
  FROM (
    SELECT commission_id, profil, id_iep, COUNT(*) AS nb_per_iep
    FROM users_commission
    GROUP BY commission_id, profil, id_iep
  ) x
  GROUP BY commission_id, profil
)
SELECT
  commission_id,
  profil,
  nb_iepp_distinct,
  min_par_iepp,
  max_par_iepp,
  CASE
    WHEN profil = 'CONSEILLER' AND nb_iepp_distinct = 3 AND min_par_iepp = 3 AND max_par_iepp = 3 THEN 'OK'
    WHEN profil = 'COORDONNATEUR' AND nb_iepp_distinct = 3 AND min_par_iepp = 1 AND max_par_iepp = 1 THEN 'OK'
    ELSE 'KO'
  END AS check_repartition_iepp
FROM stat
ORDER BY commission_id, profil;

-- -----------------------------------------------------------------------------
-- 4) Contrôle workflow (toutes ressources) pour les utilisateurs commission
-- NB: permet de voir l'état du pipeline BROUILLON -> ... -> VALIDEE_CENTRALE
-- -----------------------------------------------------------------------------
SELECT
  sw.statut,
  COUNT(*) AS nb_lignes
FROM saisie_workflow sw
GROUP BY sw.statut
ORDER BY FIELD(sw.statut,
  'BROUILLON',
  'SOUMIS',
  'VALIDEE_COORDONNATEUR',
  'VALIDEE_SUPERVISEUR',
  'VALIDEE_CENTRALE',
  'RETOURNE',
  'REJETE'
);

SELECT
  sw.resource_path,
  sw.statut,
  COUNT(*) AS nb_lignes
FROM saisie_workflow sw
GROUP BY sw.resource_path, sw.statut
ORDER BY sw.resource_path,
         FIELD(sw.statut,
           'BROUILLON',
           'SOUMIS',
           'VALIDEE_COORDONNATEUR',
           'VALIDEE_SUPERVISEUR',
           'VALIDEE_CENTRALE',
           'RETOURNE',
           'REJETE');

-- -----------------------------------------------------------------------------
-- 5) Contrôle "prévalidation par niveau" sur les lignes déjà soumises
-- -----------------------------------------------------------------------------
SELECT
  COUNT(*) AS total_soumis_ou_plus,
  SUM(CASE WHEN statut IN ('VALIDEE_COORDONNATEUR', 'VALIDEE_SUPERVISEUR', 'VALIDEE_CENTRALE') THEN 1 ELSE 0 END) AS prevalidation_coordonnateur_ok,
  SUM(CASE WHEN statut IN ('VALIDEE_SUPERVISEUR', 'VALIDEE_CENTRALE') THEN 1 ELSE 0 END) AS prevalidation_superviseur_ok
FROM saisie_workflow
WHERE statut IN ('SOUMIS', 'VALIDEE_COORDONNATEUR', 'VALIDEE_SUPERVISEUR', 'VALIDEE_CENTRALE');

-- -----------------------------------------------------------------------------
-- 6) Alerte rapide: lignes soumises sans auteur/propriétaire
-- -----------------------------------------------------------------------------
SELECT
  sw.resource_path,
  sw.record_id,
  sw.statut,
  sw.proprietaire,
  sw.soumis_par
FROM saisie_workflow sw
WHERE sw.statut IN ('SOUMIS', 'VALIDEE_COORDONNATEUR', 'VALIDEE_SUPERVISEUR', 'VALIDEE_CENTRALE')
  AND (sw.proprietaire IS NULL OR sw.soumis_par IS NULL)
ORDER BY sw.resource_path, sw.record_id;

