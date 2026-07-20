-- Activer / désactiver un centre (statistiques, listes filtrées).
CALL prism_add_column_if_missing('centre', 'ACTIF', 'TINYINT(1) NULL DEFAULT 1');

UPDATE centre SET ACTIF = 1 WHERE ACTIF IS NULL;
