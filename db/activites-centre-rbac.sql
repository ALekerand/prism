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
SELECT 'VALIDATION_VISITES_CONSEILLER', 'Activités centre', 'Validation des visites conseiller'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'VALIDATION_VISITES_CONSEILLER');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'SUIVI_CONSEILLER', 'Activités centre', 'Suivi du conseiller'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'SUIVI_CONSEILLER');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'SUIVI_SUPERVISEUR', 'Activités centre', 'Suivi par le superviseur'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'SUIVI_SUPERVISEUR');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'SUIVI_IEPP', 'Activités centre', 'Suivi par l''IEPP'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'SUIVI_IEPP');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'SUIVI_CENTRALE', 'Activités centre', 'Suivi central AENF'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'SUIVI_CENTRALE');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'ACTIVITES_CENTRE_PARTENARIAT', 'Activités centre', 'Partenariat activités centre'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'ACTIVITES_CENTRE_PARTENARIAT');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'ACTIVITES_CENTRE_PERFORMANCE', 'Activités centre', 'Performance activités centre'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'ACTIVITES_CENTRE_PERFORMANCE');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'ACTIVITES_CENTRE_CONTROLE', 'Activités centre', 'Contrôle activités centre'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'ACTIVITES_CENTRE_CONTROLE');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'ACTIVITES_CENTRE_EVALUATION', 'Activités centre', 'Évaluation activités centre'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'ACTIVITES_CENTRE_EVALUATION');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'ACTIVITES_CENTRE_INFOS', 'Activités centre', 'Informations centres activités centre'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'ACTIVITES_CENTRE_INFOS');

INSERT INTO fonctionnalite (code_fonctionnalite, module, libelle_fonctionnalite)
SELECT 'SAISIE_DONNEES', 'Workflow', 'Saisie des données'
WHERE NOT EXISTS (SELECT 1 FROM fonctionnalite WHERE code_fonctionnalite = 'SAISIE_DONNEES');

-- Rôles métier manquants.
INSERT INTO app_role (code_role, libelle_role, description_role)
SELECT 'CONSEILLER', 'Conseiller', 'Niveau 1'
WHERE NOT EXISTS (SELECT 1 FROM app_role WHERE code_role = 'CONSEILLER');

INSERT INTO app_role (code_role, libelle_role, description_role)
SELECT 'COORDONNATEUR', 'Coordonnateur', 'Niveau 2'
WHERE NOT EXISTS (SELECT 1 FROM app_role WHERE code_role = 'COORDONNATEUR');

INSERT INTO app_role (code_role, libelle_role, description_role)
SELECT 'SUPERVISEUR', 'Superviseur', 'Niveau 3'
WHERE NOT EXISTS (SELECT 1 FROM app_role WHERE code_role = 'SUPERVISEUR');

INSERT INTO app_role (code_role, libelle_role, description_role)
SELECT 'IEPP', 'IEPP', 'Inspection de l''enseignement primaire et préscolaire'
WHERE NOT EXISTS (SELECT 1 FROM app_role WHERE code_role = 'IEPP');

INSERT INTO app_role (code_role, libelle_role, description_role)
SELECT 'SUPERVISEUR_AENF', 'Superviseur AENF', 'Supervision centrale AENF'
WHERE NOT EXISTS (SELECT 1 FROM app_role WHERE code_role = 'SUPERVISEUR_AENF');

INSERT INTO app_role (code_role, libelle_role, description_role)
SELECT 'DIRECTEUR', 'Directeur', 'Direction centrale'
WHERE NOT EXISTS (SELECT 1 FROM app_role WHERE code_role = 'DIRECTEUR');

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

-- CONSEILLER : créer/modifier les autres données Activités Centre issues de l'ancien menu.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission IN ('LIRE', 'CREER', 'MODIFIER')
JOIN app_role r ON r.code_role = 'CONSEILLER'
WHERE f.code_fonctionnalite IN ('ACTIVITES_CENTRE_PARTENARIAT', 'ACTIVITES_CENTRE_PERFORMANCE', 'ACTIVITES_CENTRE_CONTROLE', 'ACTIVITES_CENTRE_EVALUATION', 'SAISIE_DONNEES')
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission = 'LIRE'
JOIN app_role r ON r.code_role = 'CONSEILLER'
WHERE f.code_fonctionnalite = 'ACTIVITES_CENTRE_INFOS'
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

-- Lecture des autres sous-menus Activités Centre pour les niveaux de validation.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission = 'LIRE'
JOIN app_role r ON r.code_role IN ('COORDONNATEUR', 'SUPERVISEUR', 'IEPP', 'SUPERVISEUR_AENF', 'DIRECTEUR')
WHERE f.code_fonctionnalite IN ('ACTIVITES_CENTRE_PARTENARIAT', 'ACTIVITES_CENTRE_PERFORMANCE', 'ACTIVITES_CENTRE_CONTROLE', 'ACTIVITES_CENTRE_EVALUATION', 'ACTIVITES_CENTRE_INFOS')
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

-- Lecture du workflow générique par les niveaux de validation.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission = 'LIRE'
JOIN app_role r ON r.code_role IN ('COORDONNATEUR', 'SUPERVISEUR', 'IEPP', 'SUPERVISEUR_AENF', 'DIRECTEUR')
WHERE f.code_fonctionnalite = 'SAISIE_DONNEES'
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

-- Validation hiérarchique uniquement pour performance, contrôle et évaluation.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission = 'VALIDER'
JOIN app_role r ON r.code_role IN ('COORDONNATEUR', 'SUPERVISEUR', 'SUPERVISEUR_AENF', 'DIRECTEUR')
WHERE f.code_fonctionnalite IN ('ACTIVITES_CENTRE_PERFORMANCE', 'ACTIVITES_CENTRE_CONTROLE', 'ACTIVITES_CENTRE_EVALUATION', 'SAISIE_DONNEES')
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

-- COORDONNATEUR : valider les visites conseiller avant visibilité IEPP.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission IN ('LIRE', 'VALIDER')
JOIN app_role r ON r.code_role = 'COORDONNATEUR'
WHERE f.code_fonctionnalite = 'VALIDATION_VISITES_CONSEILLER'
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

-- SUPERVISEUR : créer/modifier/valider le suivi superviseur.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission IN ('LIRE', 'CREER', 'MODIFIER', 'VALIDER')
JOIN app_role r ON r.code_role = 'SUPERVISEUR'
WHERE f.code_fonctionnalite = 'SUIVI_SUPERVISEUR'
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

-- IEPP : créer/modifier/valider le suivi IEPP.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission IN ('LIRE', 'CREER', 'MODIFIER', 'VALIDER')
JOIN app_role r ON r.code_role = 'IEPP'
WHERE f.code_fonctionnalite = 'SUIVI_IEPP'
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );

-- SUPERVISEUR_AENF : visibilité centrale après validation superviseur.
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, r.id_role
FROM fonctionnalite f
JOIN permission p ON p.code_permission = 'LIRE'
JOIN app_role r ON r.code_role IN ('SUPERVISEUR_AENF', 'DIRECTEUR')
WHERE f.code_fonctionnalite = 'SUIVI_CENTRALE'
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
WHERE f.code_fonctionnalite IN (
  'POINTS_VISITES',
  'VALIDATION_VISITES_CONSEILLER',
  'SUIVI_CONSEILLER',
  'SUIVI_SUPERVISEUR',
  'SUIVI_IEPP',
  'SUIVI_CENTRALE',
  'ACTIVITES_CENTRE_PARTENARIAT',
  'ACTIVITES_CENTRE_PERFORMANCE',
  'ACTIVITES_CENTRE_CONTROLE',
  'ACTIVITES_CENTRE_EVALUATION',
  'ACTIVITES_CENTRE_INFOS',
  'SAISIE_DONNEES'
)
  AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission existing
    WHERE existing.id_fonctionnalite = f.id_fonctionnalite
      AND existing.id_permission = p.id_permission
      AND existing.id_role = r.id_role
  );
