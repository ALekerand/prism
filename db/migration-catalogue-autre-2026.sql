-- Entrées catalogue « Autre » pour dossier centre (précision libre à la sélection)
INSERT INTO `infrastructure` (`code_infrastructure`, `libelle_infrastructure`)
SELECT 'AUTRE', 'Autre'
WHERE NOT EXISTS (
  SELECT 1 FROM `infrastructure`
  WHERE LOWER(TRIM(`libelle_infrastructure`)) IN ('autre', 'autres')
     OR LOWER(TRIM(`code_infrastructure`)) = 'autre'
);

INSERT INTO `materiels_pedagogique` (`code_materiel_pedagogique`, `libelle_materiel_pedagogique`)
SELECT 'AUTRE', 'Autre'
WHERE NOT EXISTS (
  SELECT 1 FROM `materiels_pedagogique`
  WHERE LOWER(TRIM(`libelle_materiel_pedagogique`)) IN ('autre', 'autres')
     OR LOWER(TRIM(`code_materiel_pedagogique`)) = 'autre'
);

INSERT INTO `designation` (`code_designation`, `libelle_designation`)
SELECT 'AUTRE', 'Autre'
WHERE NOT EXISTS (
  SELECT 1 FROM `designation`
  WHERE LOWER(TRIM(`libelle_designation`)) IN ('autre', 'autres')
     OR LOWER(TRIM(`code_designation`)) = 'autre'
);
