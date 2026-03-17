-- =============================================================================
-- Script : Création du rôle SUPER_ROOT avec toutes les permissions
--          et de l'utilisateur nebdev (mot de passe : nebdev123)
--
-- Option 1 (recommandée) : Lancer l'application Spring Boot une fois.
--          L'initialiseur Java crée automatiquement nebdev + SUPER_ROOT.
--
-- Option 2 : Exécuter ce script manuellement si la base existe déjà
--            (rôle + liaisons RFP). L'utilisateur nebdev sera créé au
--            prochain démarrage de l'application.
-- =============================================================================

USE prism_bd;

-- 1) Créer le rôle SUPER_ROOT s'il n'existe pas
INSERT INTO app_role (CODE_ROLE, LIBELLE_ROLE, DESCRIPTION_ROLE)
SELECT 'SUPER_ROOT', 'Super Root', 'Accès total à toutes les fonctionnalités (CRUD partout)'
FROM (SELECT 1) t
WHERE NOT EXISTS (SELECT 1 FROM app_role WHERE CODE_ROLE = 'SUPER_ROOT');

-- 2) Accorder toutes les permissions (toutes fonctionnalités × tous droits) au rôle SUPER_ROOT
INSERT INTO role_fonctionnalite_permission (ID_ROLE, ID_FONCTIONNALITE, ID_PERMISSION)
SELECT r.ID_ROLE, f.ID_FONCTIONNALITE, p.ID_PERMISSION
FROM app_role r
CROSS JOIN fonctionnalite f
CROSS JOIN permission p
WHERE r.CODE_ROLE = 'SUPER_ROOT'
AND NOT EXISTS (
    SELECT 1 FROM role_fonctionnalite_permission rfp
    WHERE rfp.ID_ROLE = r.ID_ROLE
      AND rfp.ID_FONCTIONNALITE = f.ID_FONCTIONNALITE
      AND rfp.ID_PERMISSION = p.ID_PERMISSION
);

-- 3) Créer l'utilisateur nebdev (mot de passe : nebdev123, hash BCrypt)
--    À exécuter seulement si vous n'utilisez pas l'initialiseur Java.
--    Sinon, supprimez ou commentez ce bloc et laissez l'app créer nebdev.
/*
INSERT INTO app_user (USERNAME, PASSWORD_HASH, EMAIL, ACTIF, DATE_CREATION)
SELECT 'nebdev',
       '$2a$10$YourBcryptHashHereReplaceWithRealHash',
       'nebdev@prism.local',
       1,
       NOW(3)
FROM (SELECT 1) t
WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE USERNAME = 'nebdev');

INSERT INTO user_role (ID_USER, ID_ROLE)
SELECT u.ID_USER, r.ID_ROLE
FROM app_user u, app_role r
WHERE u.USERNAME = 'nebdev' AND r.CODE_ROLE = 'SUPER_ROOT'
AND NOT EXISTS (
    SELECT 1 FROM user_role ur WHERE ur.ID_USER = u.ID_USER AND ur.ID_ROLE = r.ID_ROLE
);
*/
