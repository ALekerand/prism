-- RBAC ACTIVITES CENTRE conforme au modèle :
-- fonctionnalite = écran / action métier, permission = colonne standard (LIRE, CREER, MODIFIER, etc.).
-- Script idempotent : peut être exécuté plusieurs fois sur prism_bd.

-- Nettoyage des anciens codes créés par erreur comme permissions métier.
DELETE rfp
FROM role_fonctionnalite_permission rfp
JOIN permission p ON p.id_permission = rfp.id_permission
WHERE p.code_permission IN (
  'POINTS_VISITES_GERER',
  'SUIVI_CONSEILLER_MODIFIER',
  'SUIVI_SUPERVISEUR_MODIFIER',
  'SUIVI_IEPP_MODIFIER'
);

DELETE FROM permission
WHERE code_permission IN (
  'POINTS_VISITES_GERER',
  'SUIVI_CONSEILLER_MODIFIER',
  'SUIVI_SUPERVISEUR_MODIFIER',
  'SUIVI_IEPP_MODIFIER'
);

-- Fonctionnalités Activités Centre.
INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'POINTS_VISITES', 'Activités centre', 'Points des visites'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'POINTS_VISITES');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'SUIVI_CONSEILLER', 'Activités centre', 'Suivi du conseiller'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'SUIVI_CONSEILLER');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'SUIVI_SUPERVISEUR', 'Activités centre', 'Suivi par le superviseur'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'SUIVI_SUPERVISEUR');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'SUIVI_IEPP', 'Activités centre', 'Suivi par l''IEPP'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'SUIVI_IEPP');

-- Rôles métier manquants.
INSERT INTO app_role (code_role, libelle_role, description_role)
SELECT 'CONSEILLER', 'Conseiller', 'Niveau 1'
WHERE NOT EXISTS (SELECT 1 FROM app_role WHERE code_role = 'CONSEILLER');

INSERT INTO app_role (code_role, libelle_role, description_role)
SELECT 'SUPERVISEUR', 'Superviseur', 'Niveau 3'
WHERE NOT EXISTS (SELECT 1 FROM app_role WHERE code_role = 'SUPERVISEUR');

INSERT INTO app_role (code_role, libelle_role, description_role)
SELECT 'IEPP', 'IEPP', 'Inspection de l''enseignement primaire et préscolaire'
WHERE NOT EXISTS (SELECT 1 FROM app_role WHERE code_role = 'IEPP');

-- CONSEILLER : voir/créer/modifier les points + voir/modifier le suivi conseiller.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission IN ('LIRE', 'CREER', 'MODIFIER')
JOIN app_role r ON r.code_role = 'CONSEILLER'
WHERE f.code_fonctionnalite = 'POINTS_VISITES'
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission IN ('LIRE', 'MODIFIER')
JOIN app_role r ON r.code_role = 'CONSEILLER'
WHERE f.code_fonctionnalite = 'SUIVI_CONSEILLER'
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

-- SUPERVISEUR : voir/modifier le suivi superviseur.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission IN ('LIRE', 'MODIFIER')
JOIN app_role r ON r.code_role = 'SUPERVISEUR'
WHERE f.code_fonctionnalite = 'SUIVI_SUPERVISEUR'
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

-- IEPP : voir/modifier le suivi IEPP.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission IN ('LIRE', 'MODIFIER')
JOIN app_role r ON r.code_role = 'IEPP'
WHERE f.code_fonctionnalite = 'SUIVI_IEPP'
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

-- ADMIN / SUPER_ADMIN / SUPER_ROOT : toutes les colonnes de permission sur ces fonctionnalités.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission IN ('CREER', 'LIRE', 'MODIFIER', 'SUPPRIMER', 'VALIDER', 'EXPORTER')
JOIN app_role r ON r.code_role IN ('ADMIN', 'SUPER_ADMIN', 'SUPER_ROOT')
WHERE f.code_fonctionnalite IN ('POINTS_VISITES', 'SUIVI_CONSEILLER', 'SUIVI_SUPERVISEUR', 'SUIVI_IEPP')
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );
