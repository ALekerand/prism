-- Compte créé par un admin : changement de mot de passe obligatoire à la première connexion.
CALL prism_add_column_if_missing('app_user', 'MUST_CHANGE_PASSWORD', 'TINYINT(1) NOT NULL DEFAULT 0');