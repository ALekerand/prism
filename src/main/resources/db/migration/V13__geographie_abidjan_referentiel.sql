-- Référentiel géographique Abidjan : région, couverture DRENA/département, communes et localités
-- Idempotent : complète les jeux existants sans écraser les codes localité déjà générés.

INSERT INTO region (code_region, libelle_region)
SELECT 'DAA', 'District Autonome d''Abidjan'
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM region
  WHERE code_region = 'DAA' OR libelle_region = 'District Autonome d''Abidjan'
);

SET @id_region := (
  SELECT id_region FROM region
  WHERE code_region = 'DAA' OR libelle_region = 'District Autonome d''Abidjan'
  ORDER BY id_region LIMIT 1
);

INSERT INTO departement (code_departement, nom_departement, id_region)
SELECT 'ABJ', 'Abidjan', @id_region
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM departement WHERE code_departement = 'ABJ' OR nom_departement = 'Abidjan');

UPDATE departement
SET id_region = @id_region, nom_departement = 'Abidjan'
WHERE code_departement = 'ABJ' OR nom_departement = 'Abidjan';

SET @id_departement := (
  SELECT id_departement FROM departement
  WHERE code_departement = 'ABJ' OR nom_departement = 'Abidjan'
  ORDER BY id_departement LIMIT 1
);

INSERT INTO milieu_implantation (code_milieu_implentation, libelle_type_implentation_)
SELECT 'URB', 'URBAIN'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM milieu_implantation WHERE code_milieu_implentation = 'URB');

SET @id_milieu_urbain := (
  SELECT id_milieu_implentation FROM milieu_implantation
  WHERE code_milieu_implentation = 'URB'
  ORDER BY id_milieu_implentation LIMIT 1
);

INSERT INTO drena (code_drena, nom_drena, mail_drena, telephone_drena)
SELECT 'DABJ', 'DRENA Abidjan', 'drena-abj@prism.local', '0102030405'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM drena WHERE code_drena = 'DABJ' OR nom_drena = 'DRENA Abidjan');

UPDATE drena SET nom_drena = 'DRENA Abidjan' WHERE code_drena = 'DABJ';

SET @id_drena := (
  SELECT id_drena FROM drena
  WHERE code_drena = 'DABJ' OR nom_drena = 'DRENA Abidjan'
  ORDER BY id_drena LIMIT 1
);

INSERT INTO drena_departement (id_drena, id_departement)
SELECT @id_drena, @id_departement
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM drena_departement
  WHERE id_drena = @id_drena AND id_departement = @id_departement
);

INSERT INTO iep (id_drena, code_iep, nom_iep, mail_iep, telephone_iep)
SELECT @id_drena, 'IEP1', 'IEP Cocody', 'iep-cocody@prism.local', '0708091011'
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM iep WHERE id_drena = @id_drena AND (code_iep = 'IEP1' OR nom_iep = 'IEP Cocody')
);

INSERT INTO iep (id_drena, code_iep, nom_iep, mail_iep, telephone_iep)
SELECT @id_drena, 'IEPYOP', 'IEP Yopougon', 'iep-yop@prism.local', '0708091012'
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM iep WHERE id_drena = @id_drena AND (code_iep = 'IEPYOP' OR nom_iep = 'IEP Yopougon')
);

-- Communes Abidjan
INSERT INTO commune (code_commune, nom_commune) SELECT 'COC', 'Cocody' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM commune WHERE nom_commune = 'Cocody');
INSERT INTO commune (code_commune, nom_commune) SELECT 'YOP', 'Yopougon' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM commune WHERE nom_commune = 'Yopougon');
INSERT INTO commune (code_commune, nom_commune) SELECT 'ADJ', 'Adjamé' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM commune WHERE nom_commune = 'Adjamé');
INSERT INTO commune (code_commune, nom_commune) SELECT 'PLA', 'Plateau' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM commune WHERE nom_commune = 'Plateau');
INSERT INTO commune (code_commune, nom_commune) SELECT 'MAR', 'Marcory' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM commune WHERE nom_commune = 'Marcory');
INSERT INTO commune (code_commune, nom_commune) SELECT 'TRE', 'Treichville' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM commune WHERE nom_commune = 'Treichville');
INSERT INTO commune (code_commune, nom_commune) SELECT 'KOU', 'Koumassi' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM commune WHERE nom_commune = 'Koumassi');
INSERT INTO commune (code_commune, nom_commune) SELECT 'ABO', 'Abobo' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM commune WHERE nom_commune = 'Abobo');

-- Sous-préfectures (arrondissements) rattachées au département Abidjan
INSERT INTO sous_prefecture (id_departement, code_sous_prefecture, nom_sous_prefecture)
SELECT @id_departement, 'SPCOC', 'Cocody' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Cocody');
INSERT INTO sous_prefecture (id_departement, code_sous_prefecture, nom_sous_prefecture)
SELECT @id_departement, 'SPYOP', 'Yopougon' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Yopougon');
INSERT INTO sous_prefecture (id_departement, code_sous_prefecture, nom_sous_prefecture)
SELECT @id_departement, 'SPADJ', 'Adjamé' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Adjamé');
INSERT INTO sous_prefecture (id_departement, code_sous_prefecture, nom_sous_prefecture)
SELECT @id_departement, 'SPPLA', 'Plateau' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Plateau');
INSERT INTO sous_prefecture (id_departement, code_sous_prefecture, nom_sous_prefecture)
SELECT @id_departement, 'SPMAR', 'Marcory' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Marcory');
INSERT INTO sous_prefecture (id_departement, code_sous_prefecture, nom_sous_prefecture)
SELECT @id_departement, 'SPTRE', 'Treichville' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Treichville');
INSERT INTO sous_prefecture (id_departement, code_sous_prefecture, nom_sous_prefecture)
SELECT @id_departement, 'SPKOU', 'Koumassi' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Koumassi');
INSERT INTO sous_prefecture (id_departement, code_sous_prefecture, nom_sous_prefecture)
SELECT @id_departement, 'SPABO', 'Abobo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Abobo');

-- Localités (code_localite laissé NULL : généré côté application via @AutoCode au prochain insert JPA)
-- Cocody
SET @id_commune := (SELECT id_commune FROM commune WHERE nom_commune = 'Cocody' ORDER BY id_commune LIMIT 1);
SET @id_sp := (SELECT id_sous_prefecture FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Cocody' ORDER BY id_sous_prefecture LIMIT 1);
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Angré' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Angré');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Riviera 3' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Riviera 3');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Deux Plateaux' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Deux Plateaux');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Bonoumin' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Bonoumin');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Cocody Centre' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Cocody Centre');

-- Yopougon
SET @id_commune := (SELECT id_commune FROM commune WHERE nom_commune = 'Yopougon' ORDER BY id_commune LIMIT 1);
SET @id_sp := (SELECT id_sous_prefecture FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Yopougon' ORDER BY id_sous_prefecture LIMIT 1);
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Siporex' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Siporex');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Niangon' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Niangon');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Selmer' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Selmer');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Toits Rouges' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Toits Rouges');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Wassakara' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Wassakara');

-- Adjamé
SET @id_commune := (SELECT id_commune FROM commune WHERE nom_commune = 'Adjamé' ORDER BY id_commune LIMIT 1);
SET @id_sp := (SELECT id_sous_prefecture FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Adjamé' ORDER BY id_sous_prefecture LIMIT 1);
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Bracodi' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Bracodi');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, '220 Logements' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = '220 Logements');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Williamsville' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Williamsville');

-- Plateau
SET @id_commune := (SELECT id_commune FROM commune WHERE nom_commune = 'Plateau' ORDER BY id_commune LIMIT 1);
SET @id_sp := (SELECT id_sous_prefecture FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Plateau' ORDER BY id_sous_prefecture LIMIT 1);
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Plateau Dokui' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Plateau Dokui');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Indénié' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Indénié');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Commerce' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Commerce');

-- Marcory
SET @id_commune := (SELECT id_commune FROM commune WHERE nom_commune = 'Marcory' ORDER BY id_commune LIMIT 1);
SET @id_sp := (SELECT id_sous_prefecture FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Marcory' ORDER BY id_sous_prefecture LIMIT 1);
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Marcory Zone 4' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Marcory Zone 4');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Anoumabo' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Anoumabo');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Biétry' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Biétry');

-- Treichville
SET @id_commune := (SELECT id_commune FROM commune WHERE nom_commune = 'Treichville' ORDER BY id_commune LIMIT 1);
SET @id_sp := (SELECT id_sous_prefecture FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Treichville' ORDER BY id_sous_prefecture LIMIT 1);
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Arras' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Arras');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Belleville' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Belleville');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Zone 3' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Zone 3');

-- Koumassi
SET @id_commune := (SELECT id_commune FROM commune WHERE nom_commune = 'Koumassi' ORDER BY id_commune LIMIT 1);
SET @id_sp := (SELECT id_sous_prefecture FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Koumassi' ORDER BY id_sous_prefecture LIMIT 1);
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Grand Campement' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Grand Campement');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Remblai' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Remblai');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Prodomo' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Prodomo');

-- Abobo
SET @id_commune := (SELECT id_commune FROM commune WHERE nom_commune = 'Abobo' ORDER BY id_commune LIMIT 1);
SET @id_sp := (SELECT id_sous_prefecture FROM sous_prefecture WHERE id_departement = @id_departement AND nom_sous_prefecture = 'Abobo' ORDER BY id_sous_prefecture LIMIT 1);
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Abobo Baoulé' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Abobo Baoulé');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Avocatier' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Avocatier');
INSERT INTO localite_d_implantation (id_commune, id_milieu_implentation, id_sous_prefecture, nom_localite)
SELECT @id_commune, @id_milieu_urbain, @id_sp, 'Sagbé' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM localite_d_implantation WHERE id_sous_prefecture = @id_sp AND nom_localite = 'Sagbé');

-- Harmonisation libellé historique
UPDATE localite_d_implantation SET nom_localite = 'Angré'
WHERE nom_localite = 'Cocody Angré';
