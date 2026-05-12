-- Referentiel reel pour le sous-menu "Evaluation periodique".
-- Les themes du livret sont stockes dans la table unique theme_evaluation,
-- avec la colonne niveau pour distinguer Niveau 1, Niveau 2 et Post Alpha.

START TRANSACTION;

UPDATE periode_evaluation
SET code_periode_evaluation = 'PE-MENSUELLE',
    libelle_periode_evaluation = 'Mensuelle'
WHERE code_periode_evaluation = 'PE-T'
  AND libelle_periode_evaluation = 'T1';

UPDATE periode_evaluation
SET code_periode_evaluation = 'PE-MENSUELLE',
    libelle_periode_evaluation = 'Mensuelle'
WHERE code_periode_evaluation = 'PE-MOIS'
  AND libelle_periode_evaluation = 'Mois';

INSERT INTO periode_evaluation (code_periode_evaluation, libelle_periode_evaluation)
SELECT 'PE-JOURNALIERE', 'Journalière'
WHERE NOT EXISTS (
  SELECT 1 FROM periode_evaluation
  WHERE code_periode_evaluation = 'PE-JOURNALIERE'
     OR libelle_periode_evaluation = 'Journalière'
);

INSERT INTO periode_evaluation (code_periode_evaluation, libelle_periode_evaluation)
SELECT 'PE-HEBDOMADAIRE', 'Hebdomadaire'
WHERE NOT EXISTS (
  SELECT 1 FROM periode_evaluation
  WHERE code_periode_evaluation = 'PE-HEBDOMADAIRE'
     OR libelle_periode_evaluation = 'Hebdomadaire'
);

INSERT INTO periode_evaluation (code_periode_evaluation, libelle_periode_evaluation)
SELECT 'PE-MENSUELLE', 'Mensuelle'
WHERE NOT EXISTS (
  SELECT 1 FROM periode_evaluation
  WHERE code_periode_evaluation = 'PE-MENSUELLE'
     OR libelle_periode_evaluation = 'Mensuelle'
);

INSERT INTO periode_evaluation (code_periode_evaluation, libelle_periode_evaluation)
SELECT 'PE-TRIMESTRIELLE', 'Trimestrielle'
WHERE NOT EXISTS (
  SELECT 1 FROM periode_evaluation
  WHERE code_periode_evaluation = 'PE-TRIMESTRIELLE'
     OR libelle_periode_evaluation = 'Trimestrielle'
);

INSERT INTO periode_evaluation (code_periode_evaluation, libelle_periode_evaluation)
SELECT 'PE-SEMESTRE', 'Semestre'
WHERE NOT EXISTS (
  SELECT 1 FROM periode_evaluation
  WHERE code_periode_evaluation = 'PE-SEMESTRE'
     OR libelle_periode_evaluation = 'Semestre'
);

INSERT INTO periode_evaluation (code_periode_evaluation, libelle_periode_evaluation)
SELECT 'PE-ANNUELLE', 'Annuelle'
WHERE NOT EXISTS (
  SELECT 1 FROM periode_evaluation
  WHERE code_periode_evaluation = 'PE-ANNUELLE'
     OR libelle_periode_evaluation = 'Annuelle'
);

UPDATE niveau_evaluation
SET code_niveau_evaluation = 'NE-N1',
    libelle_niveau_evaluation = 'Niveau 1'
WHERE code_niveau_evaluation = 'NE-T'
  AND libelle_niveau_evaluation = 'N1';

UPDATE niveau_evaluation
SET code_niveau_evaluation = 'NE-N2',
    libelle_niveau_evaluation = 'Niveau 2'
WHERE code_niveau_evaluation = 'NE-P2'
  AND libelle_niveau_evaluation = 'N1 P2';

INSERT INTO niveau_evaluation (code_niveau_evaluation, libelle_niveau_evaluation)
SELECT 'NE-N1', 'Niveau 1'
WHERE NOT EXISTS (
  SELECT 1 FROM niveau_evaluation
  WHERE code_niveau_evaluation = 'NE-N1'
     OR libelle_niveau_evaluation = 'Niveau 1'
);

INSERT INTO niveau_evaluation (code_niveau_evaluation, libelle_niveau_evaluation)
SELECT 'NE-N2', 'Niveau 2'
WHERE NOT EXISTS (
  SELECT 1 FROM niveau_evaluation
  WHERE code_niveau_evaluation = 'NE-N2'
     OR libelle_niveau_evaluation = 'Niveau 2'
);

INSERT INTO niveau_evaluation (code_niveau_evaluation, libelle_niveau_evaluation)
SELECT 'NE-PA', 'Post Alpha'
WHERE NOT EXISTS (
  SELECT 1 FROM niveau_evaluation
  WHERE code_niveau_evaluation = 'NE-PA'
     OR libelle_niveau_evaluation = 'Post Alpha'
);

UPDATE theme_evaluation
SET code_theme_evaluation = 'TEV-N1-SAIT-LIRE',
    libelle_theme_evaluation = 'Sait lire',
    niveau = 'NIVEAU_1'
WHERE code_theme_evaluation = 'TEV0000001'
  AND libelle_theme_evaluation = 'Comprehension';

UPDATE theme_evaluation
SET code_theme_evaluation = 'TEV-N2-SAIT-LIRE-TEXTE',
    libelle_theme_evaluation = 'Sait lire un texte',
    niveau = 'NIVEAU_2'
WHERE code_theme_evaluation = 'TEV0000002'
  AND libelle_theme_evaluation = 'libelle 2';

UPDATE theme_evaluation
SET code_theme_evaluation = 'TEV-PA-SAIT-LIRE-TEXTE',
    libelle_theme_evaluation = 'Sait lire un texte',
    niveau = 'POST_ALPHA'
WHERE code_theme_evaluation = 'TEV0000003'
  AND libelle_theme_evaluation = 'libelle 3';

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N1-SAIT-LIRE', 'Sait lire', 'NIVEAU_1'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_1' AND libelle_theme_evaluation = 'Sait lire'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N1-NE-SAIT-PAS-LIRE', 'Ne sait pas lire', 'NIVEAU_1'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_1' AND libelle_theme_evaluation = 'Ne sait pas lire'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N1-SAIT-ECRIRE', 'Sait écrire', 'NIVEAU_1'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_1' AND libelle_theme_evaluation = 'Sait écrire'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N1-NE-SAIT-PAS-ECRIRE', 'Ne sait pas écrire', 'NIVEAU_1'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_1' AND libelle_theme_evaluation = 'Ne sait pas écrire'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N1-SAIT-CALCULER', 'Sait calculer', 'NIVEAU_1'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_1' AND libelle_theme_evaluation = 'Sait calculer'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N1-NE-SAIT-CALCULER', 'Ne sait calculer', 'NIVEAU_1'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_1' AND libelle_theme_evaluation = 'Ne sait calculer'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N2-SAIT-LIRE-TEXTE', 'Sait lire un texte', 'NIVEAU_2'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_2' AND libelle_theme_evaluation = 'Sait lire un texte'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N2-NE-SAIT-PAS-LIRE-TEXTE', 'Ne sait pas lire un texte', 'NIVEAU_2'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_2' AND libelle_theme_evaluation = 'Ne sait pas lire un texte'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N2-COMPREND-TEXTE', 'Comprend un texte', 'NIVEAU_2'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_2' AND libelle_theme_evaluation = 'Comprend un texte'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N2-NE-COMPREND-PAS-TEXTE', 'Ne comprend pas un texte', 'NIVEAU_2'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_2' AND libelle_theme_evaluation = 'Ne comprend pas un texte'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N2-RESOUD-SITUATION', 'Résoud une situation problème', 'NIVEAU_2'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_2' AND libelle_theme_evaluation = 'Résoud une situation problème'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-N2-NE-RESOUD-SITUATION', 'Ne Résoud une situation problème', 'NIVEAU_2'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'NIVEAU_2' AND libelle_theme_evaluation = 'Ne Résoud une situation problème'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-PA-SAIT-LIRE-TEXTE', 'Sait lire un texte', 'POST_ALPHA'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'POST_ALPHA' AND libelle_theme_evaluation = 'Sait lire un texte'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-PA-NE-SAIT-PAS-LIRE-TEXTE', 'Ne sait pas lire un texte', 'POST_ALPHA'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'POST_ALPHA' AND libelle_theme_evaluation = 'Ne sait pas lire un texte'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-PA-COMPREND-TEXTE', 'Comprend un texte', 'POST_ALPHA'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'POST_ALPHA' AND libelle_theme_evaluation = 'Comprend un texte'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-PA-NE-COMPREND-PAS-TEXTE', 'Ne comprend pas un texte', 'POST_ALPHA'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'POST_ALPHA' AND libelle_theme_evaluation = 'Ne comprend pas un texte'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-PA-RESOUD-SITUATION', 'Résoud une situation problème', 'POST_ALPHA'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'POST_ALPHA' AND libelle_theme_evaluation = 'Résoud une situation problème'
);

INSERT INTO theme_evaluation (code_theme_evaluation, libelle_theme_evaluation, niveau)
SELECT 'TEV-PA-NE-RESOUD-SITUATION', 'Ne Résoud une situation problème', 'POST_ALPHA'
WHERE NOT EXISTS (
  SELECT 1 FROM theme_evaluation
  WHERE niveau = 'POST_ALPHA' AND libelle_theme_evaluation = 'Ne Résoud une situation problème'
);

COMMIT;
