-- Pour l'utilisateur `admin` : accorde à **chaque rôle** déjà lié dans `user_role`
-- toutes les combinaisons (fonctionnalité × permission) présentes en base.
-- Idempotent (NOT EXISTS). À exécuter après les scripts qui créent `fonctionnalite` et `permission`.
--
-- Exemple : mysql -u root prism_bd < db/grant-all-permissions-to-admin-user-roles.sql

INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, ur.id_role
FROM app_user u
JOIN user_role ur ON ur.id_user = u.id_user
CROSS JOIN fonctionnalite f
CROSS JOIN permission p
WHERE u.username = 'admin'
  AND NOT EXISTS (
    SELECT 1
    FROM role_fonctionnalite_permission x
    WHERE x.id_fonctionnalite = f.id_fonctionnalite
      AND x.id_permission = p.id_permission
      AND x.id_role = ur.id_role
  );
