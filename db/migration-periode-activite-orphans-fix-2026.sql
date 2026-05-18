-- Corrige les id_periode_activite pointant vers une ligne supprimée (ex. ancien id 3 « Annuel »).
-- À exécuter après migration-evaluation-periode-activite-2026.sql si besoin.

SET @fallback_periode := (
  SELECT id_periode_activite FROM periode_activite
  WHERE UPPER(libelle_periode_activite) LIKE '%ANNUEL%'
     OR code_periode_activite IN ('AN', 'PE-ANNUELL')
  ORDER BY id_periode_activite
  LIMIT 1
);

UPDATE controle
SET id_periode_activite = @fallback_periode
WHERE id_periode_activite IS NOT NULL
  AND id_periode_activite NOT IN (SELECT id_periode_activite FROM periode_activite);

UPDATE visite
SET id_periode_activite = @fallback_periode
WHERE id_periode_activite IS NOT NULL
  AND id_periode_activite NOT IN (SELECT id_periode_activite FROM periode_activite);

UPDATE performance
SET id_periode_activite = @fallback_periode
WHERE id_periode_activite IS NOT NULL
  AND id_periode_activite NOT IN (SELECT id_periode_activite FROM periode_activite);

UPDATE evaluation
SET id_periode_activite = @fallback_periode
WHERE id_periode_activite IS NOT NULL
  AND id_periode_activite NOT IN (SELECT id_periode_activite FROM periode_activite);

UPDATE suivi_iepp
SET id_periode_activite = @fallback_periode
WHERE id_periode_activite IS NOT NULL
  AND id_periode_activite NOT IN (SELECT id_periode_activite FROM periode_activite);

UPDATE suivi_superviseur
SET id_periode_activite = @fallback_periode
WHERE id_periode_activite IS NOT NULL
  AND id_periode_activite NOT IN (SELECT id_periode_activite FROM periode_activite);
