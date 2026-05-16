-- Lie les activités centre à periode_activite (entités Controle, Visite, Performance, Suivi*).
-- Exécuter une fois sur prism_bd avant redéploiement (staging : ddl-auto=validate).

ALTER TABLE controle
  ADD COLUMN IF NOT EXISTS id_periode_activite INT NULL;

ALTER TABLE visite
  ADD COLUMN IF NOT EXISTS id_periode_activite INT NULL;

ALTER TABLE performance
  ADD COLUMN IF NOT EXISTS id_periode_activite INT NULL;

ALTER TABLE suivi_iepp
  ADD COLUMN IF NOT EXISTS id_periode_activite INT NULL;

ALTER TABLE suivi_superviseur
  ADD COLUMN IF NOT EXISTS id_periode_activite INT NULL;
