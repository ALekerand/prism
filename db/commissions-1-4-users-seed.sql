-- =============================================================================
-- Commissions 1 à 4 — comptes de démonstration (MySQL 8+)
--
-- Modèle périmètre :
--   • Commission 1 → 1re DRENA disposant d’au moins un IEP (tri id_drena)
--   • Commission 2 → 2e DRENA …
--   • Commission 3 → 3e …
--   • Commission 4 → 4e …
--   (DRENA « A/B/C/D » = quatre lignes distinctes de la table `drena`, pas un libellé fixe.)
--
-- Pour chaque commission N ∈ {1,2,3,4} :
--   • 9 conseillers          : commission_N_conseiller_1 … _9
--   • 3 coordinateurs       : commission_N_coordonnateur_1 … _3
--   • 1 superviseur DRENA   : commission_N_superviseur_1 (région + DRENA + IEP de cette commission)
--   • 1 référent DRENA      : commission_N_drena_1 (rôle ARO0000001, id_role 13)
--
-- Supervision nationale : un seul compte SUPERVISEUR_AENF (id_role 12) :
--   • superviseur_dcspa_national — sans rattachement DRENA / IEP (périmètre national)
--
-- Mot de passe : identique au compte `conseiller_test` (copie du hash en base).
--            Vérifier ce mot de passe dans votre environnement ; en démo il est souvent « 123456 ».
--
-- Libellé : SUPERVISEUR_AENF → « Superviseur DCSPA »
--
-- Prérequis : au moins 4 DRENA ayant chacune au moins une ligne dans `iep`.
--
-- Idempotent sur les INSERT utilisateur (NOT EXISTS). user_role en INSERT IGNORE.
--
-- Ancien modèle (un compte commission_N_superviseur_dcspa par commission) : voir bloc
-- « Nettoyage optionnel » en fin de fichier.
-- =============================================================================

SET @pwd := (SELECT password_hash FROM app_user WHERE username = 'conseiller_test' LIMIT 1);

UPDATE app_role
SET libelle_role     = 'Superviseur DCSPA',
    description_role = 'Supervision nationale (plan national DCSPA)'
WHERE code_role = 'SUPERVISEUR_AENF';

SET @n_drena_avec_iep := (
    SELECT COUNT(*)
    FROM (
             SELECT d.id_drena
             FROM drena d
                      INNER JOIN iep i ON i.id_drena = d.id_drena
             GROUP BY d.id_drena
         ) t
);

-- Conseillers × 4 commissions × 9 (région / DRENA / IEP dépendent de la commission)
INSERT INTO app_user (actif, username, email, password_hash, id_region, id_drena, id_iep, id_departement, id_sous_prefecture, id_commune, id_localite)
SELECT b'1',
       CONCAT('commission_', m.com_ix, '_conseiller_', n.num),
       CONCAT('commission', m.com_ix, '.c', n.num, '@demo.local'),
       @pwd,
       m.id_region,
       m.id_drena,
       CASE
         WHEN n.num BETWEEN 1 AND 3 THEN m.id_iep_1
         WHEN n.num BETWEEN 4 AND 6 THEN m.id_iep_2
         ELSE m.id_iep_3
       END,
       NULL,
       NULL,
       NULL,
       NULL
FROM (
         SELECT ROW_NUMBER() OVER (ORDER BY di.id_drena) AS com_ix,
                di.id_drena,
                di.id_iep_1,
                di.id_iep_2,
                di.id_iep_3,
                COALESCE(
                        (SELECT u.id_region
                         FROM app_user u
                         WHERE u.id_drena = di.id_drena
                           AND u.id_region IS NOT NULL
                         ORDER BY u.id_user
                         LIMIT 1),
                        (SELECT MIN(r.id_region) FROM region r)
                ) AS id_region
         FROM (
                  SELECT d.id_drena,
                         MIN(i.id_iep) AS id_iep_1,
                         COALESCE(
                           (SELECT i2.id_iep
                            FROM iep i2
                            WHERE i2.id_drena = d.id_drena
                            ORDER BY i2.id_iep
                            LIMIT 1 OFFSET 1),
                           MIN(i.id_iep)
                         ) AS id_iep_2,
                         COALESCE(
                           (SELECT i3.id_iep
                            FROM iep i3
                            WHERE i3.id_drena = d.id_drena
                            ORDER BY i3.id_iep
                            LIMIT 1 OFFSET 2),
                           (SELECT i2.id_iep
                            FROM iep i2
                            WHERE i2.id_drena = d.id_drena
                            ORDER BY i2.id_iep
                            LIMIT 1 OFFSET 1),
                           MIN(i.id_iep)
                         ) AS id_iep_3
                  FROM drena d
                           INNER JOIN iep i ON i.id_drena = d.id_drena
                  GROUP BY d.id_drena
              ) di
     ) m
         CROSS JOIN (
    SELECT 1 AS num
    UNION ALL SELECT 2
    UNION ALL SELECT 3
    UNION ALL SELECT 4
    UNION ALL SELECT 5
    UNION ALL SELECT 6
    UNION ALL SELECT 7
    UNION ALL SELECT 8
    UNION ALL SELECT 9
) n
WHERE @pwd IS NOT NULL
  AND @n_drena_avec_iep >= 4
  AND m.com_ix BETWEEN 1 AND 4
  AND NOT EXISTS (SELECT 1
                  FROM app_user x
                  WHERE x.username = CONCAT('commission_', m.com_ix, '_conseiller_', n.num));

-- Coordinateurs × 4 × 3
INSERT INTO app_user (actif, username, email, password_hash, id_region, id_drena, id_iep, id_departement, id_sous_prefecture, id_commune, id_localite)
SELECT b'1',
       CONCAT('commission_', m.com_ix, '_coordonnateur_', n.num),
       CONCAT('commission', m.com_ix, '.coord', n.num, '@demo.local'),
       @pwd,
       m.id_region,
       m.id_drena,
       CASE
         WHEN n.num = 1 THEN m.id_iep_1
         WHEN n.num = 2 THEN m.id_iep_2
         ELSE m.id_iep_3
       END,
       NULL,
       NULL,
       NULL,
       NULL
FROM (
         SELECT ROW_NUMBER() OVER (ORDER BY di.id_drena) AS com_ix,
                di.id_drena,
                di.id_iep_1,
                di.id_iep_2,
                di.id_iep_3,
                COALESCE(
                        (SELECT u.id_region
                         FROM app_user u
                         WHERE u.id_drena = di.id_drena
                           AND u.id_region IS NOT NULL
                         ORDER BY u.id_user
                         LIMIT 1),
                        (SELECT MIN(r.id_region) FROM region r)
                ) AS id_region
         FROM (
                  SELECT d.id_drena,
                         MIN(i.id_iep) AS id_iep_1,
                         COALESCE(
                           (SELECT i2.id_iep
                            FROM iep i2
                            WHERE i2.id_drena = d.id_drena
                            ORDER BY i2.id_iep
                            LIMIT 1 OFFSET 1),
                           MIN(i.id_iep)
                         ) AS id_iep_2,
                         COALESCE(
                           (SELECT i3.id_iep
                            FROM iep i3
                            WHERE i3.id_drena = d.id_drena
                            ORDER BY i3.id_iep
                            LIMIT 1 OFFSET 2),
                           (SELECT i2.id_iep
                            FROM iep i2
                            WHERE i2.id_drena = d.id_drena
                            ORDER BY i2.id_iep
                            LIMIT 1 OFFSET 1),
                           MIN(i.id_iep)
                         ) AS id_iep_3
                  FROM drena d
                           INNER JOIN iep i ON i.id_drena = d.id_drena
                  GROUP BY d.id_drena
              ) di
     ) m
         CROSS JOIN (SELECT 1 AS num UNION ALL SELECT 2 UNION ALL SELECT 3) n
WHERE @pwd IS NOT NULL
  AND @n_drena_avec_iep >= 4
  AND m.com_ix BETWEEN 1 AND 4
  AND NOT EXISTS (SELECT 1
                  FROM app_user x
                  WHERE x.username = CONCAT('commission_', m.com_ix, '_coordonnateur_', n.num));

-- Superviseur DRENA × 4 (même DRENA / IEP / région que la commission, sans IEP utilisateur si non requis)
INSERT INTO app_user (actif, username, email, password_hash, id_region, id_drena, id_iep, id_departement, id_sous_prefecture, id_commune, id_localite)
SELECT b'1',
       CONCAT('commission_', m.com_ix, '_superviseur_1'),
       CONCAT('commission', m.com_ix, '.sup1@demo.local'),
       @pwd,
       m.id_region,
       m.id_drena,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL
FROM (
         SELECT ROW_NUMBER() OVER (ORDER BY di.id_drena) AS com_ix,
                di.id_drena,
                di.id_iep,
                COALESCE(
                        (SELECT u.id_region
                         FROM app_user u
                         WHERE u.id_drena = di.id_drena
                           AND u.id_region IS NOT NULL
                         ORDER BY u.id_user
                         LIMIT 1),
                        (SELECT MIN(r.id_region) FROM region r)
                ) AS id_region
         FROM (
                  SELECT d.id_drena, MIN(i.id_iep) AS id_iep
                  FROM drena d
                           INNER JOIN iep i ON i.id_drena = d.id_drena
                  GROUP BY d.id_drena
              ) di
     ) m
WHERE @pwd IS NOT NULL
  AND @n_drena_avec_iep >= 4
  AND m.com_ix BETWEEN 1 AND 4
  AND NOT EXISTS (SELECT 1
                  FROM app_user x
                  WHERE x.username = CONCAT('commission_', m.com_ix, '_superviseur_1'));

-- Référent DRENA × 4
INSERT INTO app_user (actif, username, email, password_hash, id_region, id_drena, id_iep, id_departement, id_sous_prefecture, id_commune, id_localite)
SELECT b'1',
       CONCAT('commission_', m.com_ix, '_drena_1'),
       CONCAT('commission', m.com_ix, '.drena1@demo.local'),
       @pwd,
       m.id_region,
       m.id_drena,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL
FROM (
         SELECT ROW_NUMBER() OVER (ORDER BY di.id_drena) AS com_ix,
                di.id_drena,
                di.id_iep,
                COALESCE(
                        (SELECT u.id_region
                         FROM app_user u
                         WHERE u.id_drena = di.id_drena
                           AND u.id_region IS NOT NULL
                         ORDER BY u.id_user
                         LIMIT 1),
                        (SELECT MIN(r.id_region) FROM region r)
                ) AS id_region
         FROM (
                  SELECT d.id_drena, MIN(i.id_iep) AS id_iep
                  FROM drena d
                           INNER JOIN iep i ON i.id_drena = d.id_drena
                  GROUP BY d.id_drena
              ) di
     ) m
WHERE @pwd IS NOT NULL
  AND @n_drena_avec_iep >= 4
  AND m.com_ix BETWEEN 1 AND 4
  AND NOT EXISTS (SELECT 1
                  FROM app_user x
                  WHERE x.username = CONCAT('commission_', m.com_ix, '_drena_1'));

-- Un seul superviseur national (DCSPA / SUPERVISEUR_AENF)
INSERT INTO app_user (actif, username, email, password_hash, id_region, id_drena, id_iep, id_departement, id_sous_prefecture, id_commune, id_localite)
SELECT b'1',
       'superviseur_dcspa_national',
       'superviseur.dcspa.national@demo.local',
       @pwd,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL
WHERE @pwd IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM app_user x WHERE x.username = 'superviseur_dcspa_national');

-- Rôles (id_role alignés sur db/prism_bd.sql : 4 CONSEILLER, 5 COORDONNATEUR, 6 SUPERVISEUR, 12 SUPERVISEUR_AENF, 13 ARO0000001)
INSERT IGNORE INTO user_role (id_role, id_user)
SELECT 4, id_user
FROM app_user
WHERE username REGEXP '^commission_[1-4]_conseiller_[0-9]+$';

INSERT IGNORE INTO user_role (id_role, id_user)
SELECT 5, id_user
FROM app_user
WHERE username REGEXP '^commission_[1-4]_coordonnateur_[0-9]+$';

INSERT IGNORE INTO user_role (id_role, id_user)
SELECT 6, id_user
FROM app_user
WHERE username REGEXP '^commission_[1-4]_superviseur_1$';

INSERT IGNORE INTO user_role (id_role, id_user)
SELECT 13, id_user
FROM app_user
WHERE username REGEXP '^commission_[1-4]_drena_1$';

INSERT IGNORE INTO user_role (id_role, id_user)
SELECT 12, id_user
FROM app_user
WHERE username = 'superviseur_dcspa_national';

-- =============================================================================
-- Alignement idempotent du périmètre (DRENA / IEP / Région) des comptes déjà existants
-- (si le script a été exécuté avant que les référentiels soient complets, ou avec un ordre différent).
-- =============================================================================
UPDATE app_user u
INNER JOIN (
  SELECT ROW_NUMBER() OVER (ORDER BY di.id_drena) AS com_ix,
         di.id_drena,
         di.id_iep_1,
         di.id_iep_2,
         di.id_iep_3,
         COALESCE(
           (SELECT u2.id_region
            FROM app_user u2
            WHERE u2.id_drena = di.id_drena
              AND u2.id_region IS NOT NULL
            ORDER BY u2.id_user
            LIMIT 1),
           (SELECT MIN(r.id_region) FROM region r)
         ) AS id_region
  FROM (
    SELECT d.id_drena,
           MIN(i.id_iep) AS id_iep_1,
           COALESCE(
             (SELECT i2.id_iep
              FROM iep i2
              WHERE i2.id_drena = d.id_drena
              ORDER BY i2.id_iep
              LIMIT 1 OFFSET 1),
             MIN(i.id_iep)
           ) AS id_iep_2,
           COALESCE(
             (SELECT i3.id_iep
              FROM iep i3
              WHERE i3.id_drena = d.id_drena
              ORDER BY i3.id_iep
              LIMIT 1 OFFSET 2),
             (SELECT i2.id_iep
              FROM iep i2
              WHERE i2.id_drena = d.id_drena
              ORDER BY i2.id_iep
              LIMIT 1 OFFSET 1),
             MIN(i.id_iep)
           ) AS id_iep_3
    FROM drena d
    INNER JOIN iep i ON i.id_drena = d.id_drena
    GROUP BY d.id_drena
  ) di
) m ON m.com_ix BETWEEN 1 AND 4
SET u.id_region = m.id_region,
    u.id_drena  = m.id_drena,
    u.id_iep    = CASE
                    WHEN u.username LIKE CONCAT('commission_', m.com_ix, '_coordonnateur_%')
                      THEN CASE
                             WHEN CAST(SUBSTRING_INDEX(u.username, '_', -1) AS UNSIGNED) = 1 THEN m.id_iep_1
                             WHEN CAST(SUBSTRING_INDEX(u.username, '_', -1) AS UNSIGNED) = 2 THEN m.id_iep_2
                             ELSE m.id_iep_3
                           END
                    ELSE CASE
                           WHEN CAST(SUBSTRING_INDEX(u.username, '_', -1) AS UNSIGNED) BETWEEN 1 AND 3 THEN m.id_iep_1
                           WHEN CAST(SUBSTRING_INDEX(u.username, '_', -1) AS UNSIGNED) BETWEEN 4 AND 6 THEN m.id_iep_2
                           ELSE m.id_iep_3
                         END
                  END
WHERE u.username LIKE CONCAT('commission_', m.com_ix, '_conseiller_%')
   OR u.username LIKE CONCAT('commission_', m.com_ix, '_coordonnateur_%');

-- Superviseur / référent DRENA : rattachés à la DRENA + région, sans IEP utilisateur.
UPDATE app_user u
INNER JOIN (
  SELECT ROW_NUMBER() OVER (ORDER BY di.id_drena) AS com_ix,
         di.id_drena,
         COALESCE(
           (SELECT u2.id_region
            FROM app_user u2
            WHERE u2.id_drena = di.id_drena
              AND u2.id_region IS NOT NULL
            ORDER BY u2.id_user
            LIMIT 1),
           (SELECT MIN(r.id_region) FROM region r)
         ) AS id_region
  FROM (
    SELECT d.id_drena
    FROM drena d
    INNER JOIN iep i ON i.id_drena = d.id_drena
    GROUP BY d.id_drena
  ) di
) m ON m.com_ix BETWEEN 1 AND 4
SET u.id_region = m.id_region,
    u.id_drena  = m.id_drena,
    u.id_iep    = NULL
WHERE u.username = CONCAT('commission_', m.com_ix, '_superviseur_1')
   OR u.username = CONCAT('commission_', m.com_ix, '_drena_1');

-- =============================================================================
-- Nettoyage optionnel : anciens comptes « commission_N_superviseur_dcspa » (un par commission)
-- Décommenter si vous aviez déjà exécuté une version précédente du script.
-- =============================================================================
-- DELETE ur FROM user_role ur
-- INNER JOIN app_user u ON u.id_user = ur.id_user
-- WHERE u.username REGEXP '^commission_[1-4]_superviseur_dcspa$';
-- DELETE FROM app_user WHERE username REGEXP '^commission_[1-4]_superviseur_dcspa$';
