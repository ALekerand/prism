-- Traçabilité de l'auteur d'enregistrement d'un centre (filtrage conseiller « mes données »).
CALL prism_add_column_if_missing('centre', 'ENREGISTRE_PAR', 'VARCHAR(150) NULL');
