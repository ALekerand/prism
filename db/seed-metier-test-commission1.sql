-- =============================================================================
-- PRISM — Jeu de test métier minimal Commission 1 (IEP Cocody)
-- 2 centres Alpha distincts, 1 conseiller propriétaire chacun.
-- Prérequis : purge-centres-et-activites.sql + référentiels (IEP, campagne, etc.)
-- =============================================================================

SET NAMES utf8mb4;
START TRANSACTION;

SET @id_iep := (
    SELECT id_iep FROM iep
    WHERE UPPER(nom_iep) LIKE '%COCODY%'
    ORDER BY id_iep LIMIT 1
);
SET @id_localite := (SELECT id_localite FROM localite_d_implantation ORDER BY id_localite LIMIT 1);
SET @id_nature := (SELECT id_naturecentre FROM naturecentre ORDER BY id_naturecentre LIMIT 1);
SET @id_promoteur := (SELECT id_promoteur FROM promoteur ORDER BY id_promoteur LIMIT 1);
SET @id_campagne := (SELECT id_compagne FROM campagne ORDER BY id_compagne LIMIT 1);
SET @id_cat_alpha := (SELECT id_categorie_centre_alpha FROM categorie_centre_alpha ORDER BY id_categorie_centre_alpha LIMIT 1);
SET @id_type_alpha := (SELECT id_type_alpha FROM type_alpha ORDER BY id_type_alpha LIMIT 1);
SET @id_regime := (SELECT id_regime_alpha FROM regimealphabetisation ORDER BY id_regime_alpha LIMIT 1);
SET @id_niveau_alpha := (SELECT id_niveau_alpha FROM niveau_alpha ORDER BY id_niveau_alpha LIMIT 1);

-- Centre 1 — commission_1_conseiller_1
INSERT INTO centre (
    id_iep, id_localite, id_naturecentre, id_promoteur,
    code_centre, localisation_centre, nom_milieu_implentation,
    autorisation, encadrer_par_mena, est_electrifie, a_de_leau,
    actif, enregistre_par, date_enregistrement
)
SELECT @id_iep, @id_localite, @id_nature, @id_promoteur,
       'TEST-C1-A01', 'Centre test conseiller 1', 'Urbain',
       b'1', b'1', b'1', b'1',
       b'1', 'commission_1_conseiller_1', CURDATE()
WHERE @id_iep IS NOT NULL AND @id_localite IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM centre WHERE code_centre = 'TEST-C1-A01');

SET @centre1 := (SELECT id_centre FROM centre WHERE code_centre = 'TEST-C1-A01' LIMIT 1);

INSERT INTO alpha (
    id_centre, id_iep, id_localite, id_naturecentre, id_promoteur,
    id_compagne, id_categorie_centre_alpha, id_type_alpha, id_regime_alpha,
    code_centre, code_alpha, libelle_alpha, localisation_centre,
    autorisation, encadrer_par_mena, est_electrifie, a_de_leau
)
SELECT @centre1, @id_iep, @id_localite, @id_nature, @id_promoteur,
       @id_campagne, @id_cat_alpha, @id_type_alpha, @id_regime,
       'TEST-C1-A01', 'TEST-C1-A01', 'Centre test conseiller 1', 'Centre test conseiller 1',
       b'1', b'1', b'1', b'1'
WHERE @centre1 IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM alpha WHERE id_centre = @centre1);

INSERT INTO alpha_niveau (id_centre, id_niveau_alpha)
SELECT @centre1, @id_niveau_alpha
WHERE @centre1 IS NOT NULL AND @id_niveau_alpha IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM alpha_niveau
      WHERE id_centre = @centre1 AND id_niveau_alpha = @id_niveau_alpha
  );

-- Centre 2 — commission_1_conseiller_2
INSERT INTO centre (
    id_iep, id_localite, id_naturecentre, id_promoteur,
    code_centre, localisation_centre, nom_milieu_implentation,
    autorisation, encadrer_par_mena, est_electrifie, a_de_leau,
    actif, enregistre_par, date_enregistrement
)
SELECT @id_iep, @id_localite, @id_nature, @id_promoteur,
       'TEST-C1-A02', 'Centre test conseiller 2', 'Urbain',
       b'1', b'1', b'1', b'1',
       b'1', 'commission_1_conseiller_2', CURDATE()
WHERE @id_iep IS NOT NULL AND @id_localite IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM centre WHERE code_centre = 'TEST-C1-A02');

SET @centre2 := (SELECT id_centre FROM centre WHERE code_centre = 'TEST-C1-A02' LIMIT 1);

INSERT INTO alpha (
    id_centre, id_iep, id_localite, id_naturecentre, id_promoteur,
    id_compagne, id_categorie_centre_alpha, id_type_alpha, id_regime_alpha,
    code_centre, code_alpha, libelle_alpha, localisation_centre,
    autorisation, encadrer_par_mena, est_electrifie, a_de_leau
)
SELECT @centre2, @id_iep, @id_localite, @id_nature, @id_promoteur,
       @id_campagne, @id_cat_alpha, @id_type_alpha, @id_regime,
       'TEST-C1-A02', 'TEST-C1-A02', 'Centre test conseiller 2', 'Centre test conseiller 2',
       b'1', b'1', b'1', b'1'
WHERE @centre2 IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM alpha WHERE id_centre = @centre2);

INSERT INTO alpha_niveau (id_centre, id_niveau_alpha)
SELECT @centre2, @id_niveau_alpha
WHERE @centre2 IS NOT NULL AND @id_niveau_alpha IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM alpha_niveau
      WHERE id_centre = @centre2 AND id_niveau_alpha = @id_niveau_alpha
  );

COMMIT;

SELECT code_centre, enregistre_par, id_iep
FROM centre
WHERE code_centre LIKE 'TEST-C1-%'
ORDER BY code_centre;

SELECT a.id_centre, a.code_alpha, c.enregistre_par
FROM alpha a
JOIN centre c ON c.id_centre = a.id_centre
WHERE a.code_alpha LIKE 'TEST-C1-%'
ORDER BY a.id_centre;
