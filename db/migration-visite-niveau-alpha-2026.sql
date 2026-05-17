-- Niveau Alpha sur les points de visite (aligné sur controle).
ALTER TABLE visite
  ADD COLUMN IF NOT EXISTS id_niveau_alpha INT NULL;
