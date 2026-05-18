-- Évaluation : bascule de periode_evaluation vers periode_activite.
-- Exécuter une fois (MySQL 8, phpMyAdmin ou mysql client).

-- 1) Insérer dans periode_activite les libellés absents (codes tronqués à 10 caractères max).
INSERT INTO periode_activite (code_periode_activite, libelle_periode_activite)
SELECT LEFT(pe.code_periode_evaluation, 10), pe.libelle_periode_evaluation
FROM periode_evaluation pe
WHERE pe.libelle_periode_evaluation IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM periode_activite pa
    WHERE UPPER(TRIM(pa.libelle_periode_activite)) = UPPER(TRIM(pe.libelle_periode_evaluation))
  );

-- 2) Colonne cible (ignorer l'erreur si la colonne existe déjà)
ALTER TABLE evaluation ADD COLUMN id_periode_activite INT NULL;

-- 3) Migrer les FK logiques via libellé
UPDATE evaluation e
INNER JOIN periode_evaluation pe ON e.id_periode_evaluation = pe.id_periode_evaluation
INNER JOIN periode_activite pa ON UPPER(TRIM(pa.libelle_periode_activite)) = UPPER(TRIM(pe.libelle_periode_evaluation))
SET e.id_periode_activite = pa.id_periode_activite
WHERE e.id_periode_evaluation IS NOT NULL;

-- 4) Ancienne colonne (nom de contrainte issu de prism_bd.sql)
ALTER TABLE evaluation DROP FOREIGN KEY FKi39puo7761exdvti27kgg74d8;
ALTER TABLE evaluation DROP COLUMN id_periode_evaluation;

-- 5) (Optionnel) clé étrangère vers periode_activite
-- ALTER TABLE evaluation
--   ADD CONSTRAINT fk_evaluation_periode_activite
--   FOREIGN KEY (id_periode_activite) REFERENCES periode_activite (id_periode_activite);
