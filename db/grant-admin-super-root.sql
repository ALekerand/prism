-- Attache l'utilisateur `admin` au rôle SUPER_ROOT avec toutes les combinaisons
-- (fonctionnalité × permission) présentes en base — aligné sur DataInitializer Java.
-- Mot de passe : admin123 (BCrypt, 10 rounds).
-- Idempotent : ré-exécutable sans doublons sur role_fonctionnalite_permission / user_role.
--
-- Usage (exemple) :
--   mysql -u root -p prism_bd < db/grant-admin-super-root.sql

SET @super_root_code := 'SUPER_ROOT';

-- Rôle SUPER_ROOT
INSERT INTO app_role (code_role, libelle_role, description_role)
SELECT @super_root_code, 'Super Root', 'Accès total (toutes fonctionnalités × permissions)'
WHERE NOT EXISTS (SELECT 1 FROM app_role r WHERE r.code_role = @super_root_code);

SET @id_super_root := (SELECT id_role FROM app_role WHERE code_role = @super_root_code LIMIT 1);

-- Toutes les paires (fonctionnalité, permission) pour SUPER_ROOT
INSERT INTO role_fonctionnalite_permission (id_fonctionnalite, id_permission, id_role)
SELECT f.id_fonctionnalite, p.id_permission, @id_super_root
FROM fonctionnalite f
CROSS JOIN permission p
WHERE NOT EXISTS (
  SELECT 1
  FROM role_fonctionnalite_permission x
  WHERE x.id_fonctionnalite = f.id_fonctionnalite
    AND x.id_permission = p.id_permission
    AND x.id_role = @id_super_root
);

-- Utilisateur admin : uniquement SUPER_ROOT (supprime les autres rôles pour éviter les surprises)
SET @id_admin := (SELECT id_user FROM app_user WHERE username = 'admin' LIMIT 1);

DELETE FROM user_role WHERE id_user = @id_admin;

INSERT INTO user_role (id_user, id_role)
SELECT @id_admin, @id_super_root
WHERE @id_admin IS NOT NULL AND @id_super_root IS NOT NULL;

-- Mot de passe admin123 (BCrypt $2b$ — Spring Security)
UPDATE app_user
SET password_hash = '$2b$10$vJeLCrI2Axabee2JFN7npeBG4ZvH94XfKj/nre.snp7YyZE1aU.Y.'
WHERE username = 'admin'
  AND @id_admin IS NOT NULL;
