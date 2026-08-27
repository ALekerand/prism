CREATE TABLE IF NOT EXISTS organisation_faitiere (
  ID_ORGANISATION_FAITIERE INT NOT NULL AUTO_INCREMENT,
  CODE_ORGANISATION_FAITIERE VARCHAR(20) NULL,
  LIBELLE_ORGANISATION_FAITIERE VARCHAR(250) NULL,
  SIGLE_ORGANISATION_FAITIERE VARCHAR(20) NULL,
  POINT_FOCAL VARCHAR(150) NULL,
  FONCTION_POINT_FOCAL VARCHAR(150) NULL,
  CONTACTS VARCHAR(250) NULL,
  COURRIEL VARCHAR(250) NULL,
  PRIMARY KEY (ID_ORGANISATION_FAITIERE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CALL prism_add_column_if_missing('personnephysique', 'ID_ORGANISATION_FAITIERE', 'INT NULL');

INSERT INTO organisation_faitiere (
  CODE_ORGANISATION_FAITIERE,
  LIBELLE_ORGANISATION_FAITIERE,
  SIGLE_ORGANISATION_FAITIERE,
  POINT_FOCAL,
  FONCTION_POINT_FOCAL,
  CONTACTS,
  COURRIEL
)
SELECT 'PFACI',
       'Plateforme des fédérations des Acteurs de l''alphabétisation de Côte d''Ivoire (PFACI)',
       'PFACI',
       'SIOTO Patrice Teh',
       'Président du Conseil d''Administration (PCA)',
       '(+225) 05 76 91 76 18',
       'siotosioto17@gmail.com'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM organisation_faitiere WHERE SIGLE_ORGANISATION_FAITIERE = 'PFACI');

INSERT INTO organisation_faitiere (
  CODE_ORGANISATION_FAITIERE,
  LIBELLE_ORGANISATION_FAITIERE,
  SIGLE_ORGANISATION_FAITIERE,
  POINT_FOCAL,
  FONCTION_POINT_FOCAL,
  CONTACTS,
  COURRIEL
)
SELECT 'FIOPA',
       'Fédération Ivoirienne des Opérateurs en Alphabétisation (FIOPA)',
       'FIOPA',
       'N''DRI Elisabeth',
       'Directrice Exécutive',
       '(+225) 07 57 10 04 93',
       'fioparobert@gmail.com'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM organisation_faitiere WHERE SIGLE_ORGANISATION_FAITIERE = 'FIOPA');

INSERT INTO organisation_faitiere (
  CODE_ORGANISATION_FAITIERE,
  LIBELLE_ORGANISATION_FAITIERE,
  SIGLE_ORGANISATION_FAITIERE,
  POINT_FOCAL,
  FONCTION_POINT_FOCAL,
  CONTACTS,
  COURRIEL
)
SELECT 'FIPME',
       'Fédération Ivoirienne des Petites et Moyennes Entreprises (FIPME)',
       'FIPME',
       'AGNERO Loyou Pélagie',
       'Membre du Bureau du Conseil',
       '(+225) 07 07 09 15 90 Groupe Scolaire de la Riviera Golf',
       'pelagnero@gmail.com'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM organisation_faitiere WHERE SIGLE_ORGANISATION_FAITIERE = 'FIPME');

INSERT INTO organisation_faitiere (
  CODE_ORGANISATION_FAITIERE,
  LIBELLE_ORGANISATION_FAITIERE,
  SIGLE_ORGANISATION_FAITIERE,
  POINT_FOCAL,
  FONCTION_POINT_FOCAL,
  CONTACTS,
  COURRIEL
)
SELECT 'FIACU',
       'Fédération Ivoirienne des Associations Clubs UNESCO (FIACU)',
       'FIACU',
       'ANY Bertin',
       'Président FIACU, Secrétaire National Alpha',
       '(+225) 05 05 89 86 26 / 07 07 69 93 35',
       'tkoutoua@gmail.com ; any_bertin@yahoo.fr'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM organisation_faitiere WHERE SIGLE_ORGANISATION_FAITIERE = 'FIACU');

INSERT INTO organisation_faitiere (
  CODE_ORGANISATION_FAITIERE,
  LIBELLE_ORGANISATION_FAITIERE,
  SIGLE_ORGANISATION_FAITIERE,
  POINT_FOCAL,
  FONCTION_POINT_FOCAL,
  CONTACTS,
  COURRIEL
)
SELECT 'PSIE',
       'Plateforme Des Structures Islamiques d''Education (PSIE)',
       'PSIE',
       'OUATTARA Soualiho',
       'Professeur',
       '(+225) 07 59 90 60 04',
       'ouattarasoualiho6@gmail.com'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM organisation_faitiere WHERE SIGLE_ORGANISATION_FAITIERE = 'PSIE');

INSERT INTO organisation_faitiere (
  CODE_ORGANISATION_FAITIERE,
  LIBELLE_ORGANISATION_FAITIERE,
  SIGLE_ORGANISATION_FAITIERE,
  POINT_FOCAL,
  FONCTION_POINT_FOCAL,
  CONTACTS,
  COURRIEL
)
SELECT 'ROECI',
       'Réseau des Organisations de la sociétés civiles pour l''Éducation en Côte d''Ivoire (ROECI)',
       'ROECI',
       'GNAGBO Christophe',
       'Président du Conseil d''Administration (PCA)',
       '(+225) 05 66 00 03 04',
       'Christophegnagbo05@gmail.com'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM organisation_faitiere WHERE SIGLE_ORGANISATION_FAITIERE = 'ROECI');

INSERT INTO organisation_faitiere (
  CODE_ORGANISATION_FAITIERE,
  LIBELLE_ORGANISATION_FAITIERE,
  SIGLE_ORGANISATION_FAITIERE,
  POINT_FOCAL,
  FONCTION_POINT_FOCAL,
  CONTACTS,
  COURRIEL
)
SELECT 'UPASC',
       'Union des Promoteurs et Alphabétiseurs du Sud-Comoé (UPASC)',
       'UPASC',
       'KOUASSI Blé Mathias',
       'Président',
       '(+225) 0505373421',
       'kouassilenoir22@gmail.com'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM organisation_faitiere WHERE SIGLE_ORGANISATION_FAITIERE = 'UPASC');
