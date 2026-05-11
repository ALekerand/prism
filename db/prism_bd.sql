-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : jeu. 07 mai 2026 à 11:31
-- Version du serveur : 8.0.30
-- Version de PHP : 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `prism_bd`
--

-- --------------------------------------------------------

--
-- Structure de la table `alpha`
--

CREATE TABLE `alpha` (
  `a_de_leau` bit(1) DEFAULT NULL,
  `autorisation` bit(1) DEFAULT NULL,
  `encadrer_par_mena` bit(1) DEFAULT NULL,
  `est_electrifie` bit(1) DEFAULT NULL,
  `id_autorite_autorisation` int DEFAULT NULL,
  `id_categorie_centre_alpha` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_compagne` int NOT NULL,
  `id_iep` int DEFAULT NULL,
  `id_localite` int DEFAULT NULL,
  `id_naturecentre` int DEFAULT NULL,
  `id_periodicite` int DEFAULT NULL,
  `id_promoteur` int DEFAULT NULL,
  `id_regime_alpha` int NOT NULL,
  `id_type_alpha` int NOT NULL,
  `nombre_visite` int DEFAULT NULL,
  `code_alpha` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `code_centre` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_alpha` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `localisation_centre` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_milieu_implentation` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `encadreur_non_mena` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `alpha`
--

INSERT INTO `alpha` (`a_de_leau`, `autorisation`, `encadrer_par_mena`, `est_electrifie`, `id_autorite_autorisation`, `id_categorie_centre_alpha`, `id_centre`, `id_compagne`, `id_iep`, `id_localite`, `id_naturecentre`, `id_periodicite`, `id_promoteur`, `id_regime_alpha`, `id_type_alpha`, `nombre_visite`, `code_alpha`, `code_centre`, `libelle_alpha`, `localisation_centre`, `nom_milieu_implentation`, `encadreur_non_mena`) VALUES
(b'1', b'1', b'1', b'1', 1, 1, 7, 1, 1, 1, 1, 1, 2, 1, 1, 2, 'ALP0000002', 'CEN0000001', 'CENTRE DU COTE SUD', 'laba', 'ici', 'SOUBA'),
(b'1', b'1', b'1', b'1', 1, 1, 11, 1, 1, 1, 1, 1, 6, 1, 1, 0, 'ALP0000003', 'CEN-ALPHA-FULL-MOR', 'Alpha Full Morale', 'Zone test', 'Village test', NULL),
(b'1', b'1', b'1', b'1', 1, 1, 12, 1, 1, 1, 1, 1, 7, 1, 1, 0, 'ALP0000004', 'CEN0000004', 'Alpha Full Morale', 'Zone test', 'Village test', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `anne_scolaire`
--

CREATE TABLE `anne_scolaire` (
  `debut_annee_scolaire` date DEFAULT NULL,
  `etat_annee_scolaire` bit(1) DEFAULT NULL,
  `fin_annee_scolaire` date DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `code_annee_scolaire` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `anne_scolaire`
--

INSERT INTO `anne_scolaire` (`debut_annee_scolaire`, `etat_annee_scolaire`, `fin_annee_scolaire`, `id_annee_scolaire`, `code_annee_scolaire`) VALUES
('2024-09-01', b'0', '2025-06-30', 1, '2024-2025'),
('2025-09-01', b'1', '2026-06-30', 2, '2025-2026');

-- --------------------------------------------------------

--
-- Structure de la table `appui_partenaire`
--

CREATE TABLE `appui_partenaire` (
  `id_appui_partenaire` int NOT NULL,
  `id_categorie_appui` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_partenaire` int NOT NULL,
  `code_appui_partenaire` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_appui_partenaire` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `appui_partenaire`
--

INSERT INTO `appui_partenaire` (`id_appui_partenaire`, `id_categorie_appui`, `id_centre`, `id_partenaire`, `code_appui_partenaire`, `libelle_appui_partenaire`) VALUES
(1, 1, 1, 1, 'APA0000001', 'Appui test'),
(2, 1, 1, 1, 'APA0000002', 'Appui test');

-- --------------------------------------------------------

--
-- Structure de la table `app_role`
--

CREATE TABLE `app_role` (
  `id_role` int NOT NULL,
  `code_role` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `libelle_role` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `description_role` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `app_role`
--

INSERT INTO `app_role` (`id_role`, `code_role`, `libelle_role`, `description_role`) VALUES
(1, 'ADMIN', 'Administrateur', 'Accès complet à l\'application'),
(2, 'LECTEUR', 'Lecteur', 'Consultation seule'),
(3, 'SUPER_ROOT', 'Super Root', 'Accès total à toutes les fonctionnalités (CRUD partout)'),
(4, 'CONSEILLER', 'Conseiller', 'Niveau 1'),
(5, 'COORDONNATEUR', 'Coordonnateur', 'Niveau 2'),
(6, 'SUPERVISEUR', 'Superviseur', 'Niveau 3'),
(7, 'DIRECTEUR', 'Directeur', 'Niveau 4'),
(8, 'CABINET', 'Cabinet', 'Niveau 5'),
(9, 'AGENT_ARCHIVE', 'Agent archive', 'Niveau 6'),
(10, 'SUPER_ADMIN', 'Super admin', 'Niveau 7');

-- --------------------------------------------------------

--
-- Structure de la table `app_user`
--

CREATE TABLE `app_user` (
  `actif` bit(1) DEFAULT NULL,
  `id_user` int NOT NULL,
  `username` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `app_user`
--

INSERT INTO `app_user` (`actif`, `id_user`, `username`, `email`, `password_hash`) VALUES
(b'1', 1, 'admin', 'admin@prism.local', '$2a$10$CgZ8Jv4iY0TT6AxLfkwN7O0aGGM9pkygCTZNMLQIdmMm8LoNTgBCS'),
(b'1', 2, 'nebdev', 'nebdev@prism.local', '$2a$10$aFkvET5jeN/PcJCHjG7blumPO.X2J47OlLXCcJJMrEpctPMJ.eG4O'),
(b'1', 3, 'gg', 'gdf@hgfx', '$2a$10$l3WnLtlisxErP3mMwC3/J.bqObcPI.9PZHDemKiZ/IboImi7mVr1e');

-- --------------------------------------------------------

--
-- Structure de la table `aspect_a_ameliorer`
--

CREATE TABLE `aspect_a_ameliorer` (
  `id_aspect_a_ameliorer` int NOT NULL,
  `code_aspect_a_ameliorer` varchar(50) DEFAULT NULL,
  `libelle_aspect_a_ameliorer` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `aspect_a_ameliorer`
--

INSERT INTO `aspect_a_ameliorer` (`id_aspect_a_ameliorer`, `code_aspect_a_ameliorer`, `libelle_aspect_a_ameliorer`) VALUES
(1, 'ASP-T', 'Ponctualite'),
(2, 'ASP-P2', 'Assiduite');

-- --------------------------------------------------------

--
-- Structure de la table `autorite_autorisation`
--

CREATE TABLE `autorite_autorisation` (
  `id_autorite_autorisation` int NOT NULL,
  `code_autorisation` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_autorite_autorisation` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `autorite_autorisation`
--

INSERT INTO `autorite_autorisation` (`id_autorite_autorisation`, `code_autorisation`, `libelle_autorite_autorisation`) VALUES
(1, 'PREF', 'Préfecture'),
(2, 'SOUS-PREF', 'Sous-préfecture'),
(3, 'MAIRIE', 'Mairie / commune');

-- --------------------------------------------------------

--
-- Structure de la table `campagne`
--

CREATE TABLE `campagne` (
  `date_debut_campagne` date DEFAULT NULL,
  `date_fin_campagne` date DEFAULT NULL,
  `etat_campagne` bit(1) DEFAULT NULL,
  `id_compagne` int NOT NULL,
  `code_campagne` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `campagne`
--

INSERT INTO `campagne` (`date_debut_campagne`, `date_fin_campagne`, `etat_campagne`, `id_compagne`, `code_campagne`) VALUES
('2026-03-06', '2026-05-01', b'1', 1, 'CAM0000001'),
('2026-03-19', '2026-04-05', b'0', 2, 'CAM0000002'),
('2026-03-03', '2026-03-04', b'1', 3, 'CAM0000003');

-- --------------------------------------------------------

--
-- Structure de la table `categorie_appui`
--

CREATE TABLE `categorie_appui` (
  `id_categorie_appui` int NOT NULL,
  `code_categorie_appui` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_categorie_appui` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `categorie_appui`
--

INSERT INTO `categorie_appui` (`id_categorie_appui`, `code_categorie_appui`, `libelle_categorie_appui`) VALUES
(1, 'FIN', 'Appui financier'),
(2, 'MAT', 'Appui matériel'),
(3, 'TECH', 'Appui technique');

-- --------------------------------------------------------

--
-- Structure de la table `categorie_centre_alpha`
--

CREATE TABLE `categorie_centre_alpha` (
  `id_categorie_centre_alpha` int NOT NULL,
  `code_categorie_centre_alpha` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_categorie_centre_alpha` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `categorie_centre_alpha`
--

INSERT INTO `categorie_centre_alpha` (`id_categorie_centre_alpha`, `code_categorie_centre_alpha`, `libelle_categorie_centre_alpha`) VALUES
(1, 'CAT1', 'JEUNE'),
(2, 'CAT2', 'ADULTE');

-- --------------------------------------------------------

--
-- Structure de la table `cec`
--

CREATE TABLE `cec` (
  `a_de_leau` bit(1) DEFAULT NULL,
  `autorisation` bit(1) DEFAULT NULL,
  `encadrer_par_mena` bit(1) DEFAULT NULL,
  `est_electrifie` bit(1) DEFAULT NULL,
  `id_autorite_autorisation` int DEFAULT NULL,
  `id_centre` int NOT NULL,
  `id_iep` int DEFAULT NULL,
  `id_localite` int DEFAULT NULL,
  `id_naturecentre` int DEFAULT NULL,
  `id_periodicite` int DEFAULT NULL,
  `id_promoteur` int DEFAULT NULL,
  `nombre_visite` int DEFAULT NULL,
  `code_centre` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_cec` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `localisation_centre` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_milieu_implentation` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `encadreur_non_mena` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `cec`
--

INSERT INTO `cec` (`a_de_leau`, `autorisation`, `encadrer_par_mena`, `est_electrifie`, `id_autorite_autorisation`, `id_centre`, `id_iep`, `id_localite`, `id_naturecentre`, `id_periodicite`, `id_promoteur`, `nombre_visite`, `code_centre`, `libelle_cec`, `localisation_centre`, `nom_milieu_implentation`, `encadreur_non_mena`) VALUES
(NULL, NULL, NULL, NULL, NULL, 4, NULL, NULL, NULL, NULL, NULL, NULL, 'CTR-CEC-01', 'CEC - CTR-CEC-01', 'Centre CEC Démo', 'Urbain', NULL),
(b'0', b'1', b'1', b'0', 3, 8, 1, 1, 2, 3, 3, 0, 'CEN0000002', 'lib cec', '', '', ''),
(b'1', b'1', b'1', b'1', 1, 10, 1, 1, 1, 1, 5, 0, 'CEN-CEC-FULL-MOR', 'CEC Full', 'Zone test', 'Village test', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `cec_niveau`
--

CREATE TABLE `cec_niveau` (
  `id_annee_scolaire` int NOT NULL,
  `id_cec_niveau` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_niveau_sie` int NOT NULL,
  `nombre_salle_cec` int DEFAULT NULL,
  `code_niveau_cec` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `centre`
--

CREATE TABLE `centre` (
  `a_de_leau` bit(1) DEFAULT NULL,
  `autorisation` bit(1) DEFAULT NULL,
  `encadrer_par_mena` bit(1) DEFAULT NULL,
  `est_electrifie` bit(1) DEFAULT NULL,
  `id_autorite_autorisation` int DEFAULT NULL,
  `id_centre` int NOT NULL,
  `id_iep` int NOT NULL,
  `id_localite` int NOT NULL,
  `id_naturecentre` int NOT NULL,
  `id_periodicite` int DEFAULT NULL,
  `id_promoteur` int NOT NULL,
  `nombre_visite` int DEFAULT NULL,
  `code_centre` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `localisation_centre` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_milieu_implentation` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `encadreur_non_mena` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `centre`
--

INSERT INTO `centre` (`a_de_leau`, `autorisation`, `encadrer_par_mena`, `est_electrifie`, `id_autorite_autorisation`, `id_centre`, `id_iep`, `id_localite`, `id_naturecentre`, `id_periodicite`, `id_promoteur`, `nombre_visite`, `code_centre`, `localisation_centre`, `nom_milieu_implentation`, `encadreur_non_mena`) VALUES
(b'1', b'1', b'1', b'1', NULL, 1, 1, 1, 1, NULL, 1, 2, 'CTR-ABJ-01', 'Abidjan - Cocody', 'Urbain', NULL),
(b'1', b'1', b'0', b'0', NULL, 2, 1, 1, 1, NULL, 1, 0, 'CTR-ABJ-02', 'Abidjan - Yopougon', 'Urbain', NULL),
(b'1', b'1', b'1', b'1', NULL, 3, 1, 1, 1, NULL, 1, 0, 'CTR-ALPHA-01', 'Centre Alpha Démo', 'Urbain', NULL),
(b'1', b'1', b'1', b'1', NULL, 4, 1, 1, 1, NULL, 1, 0, 'CTR-CEC-01', 'Centre CEC Démo', 'Urbain', NULL),
(b'1', b'1', b'1', b'1', NULL, 5, 1, 1, 1, NULL, 1, 0, 'CTR-CP-01', 'Centre CP Démo', 'Urbain', NULL),
(b'1', b'1', b'1', b'1', NULL, 6, 1, 1, 1, NULL, 1, 0, 'CTR-SIE-01', 'Centre SIE Démo', 'Urbain', NULL),
(b'1', b'1', b'1', b'1', 1, 7, 1, 1, 1, 1, 2, 2, 'CEN0000001', 'laba', 'ici', 'SOUBA'),
(b'0', b'1', b'1', b'0', 3, 8, 1, 1, 2, 3, 3, 0, 'CEN0000002', '', '', ''),
(b'1', b'1', b'1', b'1', 1, 9, 1, 1, 1, 1, 4, 3, 'CEN0000003', 'fg', 'frgt', ''),
(b'1', b'1', b'1', b'1', 1, 10, 1, 1, 1, 1, 5, 0, 'CEN-CEC-FULL-MOR', 'Zone test', 'Village test', NULL),
(b'1', b'1', b'1', b'1', 1, 11, 1, 1, 1, 1, 6, 0, 'CEN-ALPHA-FULL-MOR', 'Zone test', 'Village test', NULL),
(b'1', b'1', b'1', b'1', 1, 12, 1, 1, 1, 1, 7, 0, 'CEN0000004', 'Zone test', 'Village test', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `civilite`
--

CREATE TABLE `civilite` (
  `id_civilite` int NOT NULL,
  `code_civilite` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_civilite` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `civilite`
--

INSERT INTO `civilite` (`id_civilite`, `code_civilite`, `libelle_civilite`) VALUES
(1, 'M', 'M.'),
(2, 'MME', 'Mme'),
(3, 'MLLE', 'Mlle'),
(4, 'CIV0000001', 'AITRE');

-- --------------------------------------------------------

--
-- Structure de la table `code_sequence`
--

CREATE TABLE `code_sequence` (
  `next_value` bigint NOT NULL,
  `prefix` varchar(10) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `code_sequence`
--

INSERT INTO `code_sequence` (`next_value`, `prefix`) VALUES
(5, 'ALP'),
(3, 'APA'),
(5, 'CAM'),
(5, 'CEN'),
(3, 'CIV'),
(2, 'EAA'),
(2, 'EAL'),
(3, 'ESH'),
(3, 'FON'),
(13, 'NAL'),
(13, 'NSC'),
(5, 'PRO'),
(2, 'TDO'),
(4, 'TEV');

-- --------------------------------------------------------

--
-- Structure de la table `communaute`
--

CREATE TABLE `communaute` (
  `id_promoteur` int NOT NULL,
  `contact` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `code_promoteur` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `boite_postale` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `denomination` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_communaute` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_promoteur` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mail` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_programme` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_representant_legal_structure` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `commune`
--

CREATE TABLE `commune` (
  `id_commune` int NOT NULL,
  `code_commune` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_commune` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `commune`
--

INSERT INTO `commune` (`id_commune`, `code_commune`, `nom_commune`) VALUES
(1, 'COC', 'Cocody');

-- --------------------------------------------------------

--
-- Structure de la table `competence`
--

CREATE TABLE `competence` (
  `id_competence` int NOT NULL,
  `code_competence` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_competence` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `competence`
--

INSERT INTO `competence` (`id_competence`, `code_competence`, `libelle_competence`) VALUES
(1, 'LECT', 'Lecture'),
(2, 'CALC', 'Calcul mental'),
(3, 'EXPR', 'Expression orale');

-- --------------------------------------------------------

--
-- Structure de la table `competence_centre`
--

CREATE TABLE `competence_centre` (
  `id_centre` int NOT NULL,
  `id_competence` int NOT NULL,
  `id_competence_centre` int NOT NULL,
  `code_competence_centre` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `competence_centre`
--

INSERT INTO `competence_centre` (`id_centre`, `id_competence`, `id_competence_centre`, `code_competence_centre`) VALUES
(7, 1, 1, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `controle`
--

CREATE TABLE `controle` (
  `id_controle` int NOT NULL,
  `conformite_programme` bit(1) DEFAULT NULL,
  `date_demarrage_appren` date DEFAULT NULL,
  `jour_heure_formation` varchar(100) DEFAULT NULL,
  `nombre_kit_autre` int DEFAULT NULL,
  `nombre_kit_manuels_calculaire` int DEFAULT NULL,
  `nombre_kit_manuels_cvc` int DEFAULT NULL,
  `nombre_kit_manuels_syllabaire` int DEFAULT NULL,
  `id_alpha` int NOT NULL,
  `id_discipline` int DEFAULT NULL,
  `id_manuel` int DEFAULT NULL,
  `id_niveau_controle` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `controle`
--

INSERT INTO `controle` (`id_controle`, `conformite_programme`, `date_demarrage_appren`, `jour_heure_formation`, `nombre_kit_autre`, `nombre_kit_manuels_calculaire`, `nombre_kit_manuels_cvc`, `nombre_kit_manuels_syllabaire`, `id_alpha`, `id_discipline`, `id_manuel`, `id_niveau_controle`) VALUES
(2, b'1', NULL, 'Lundi 08h', 2, 20, 20, 20, 7, 1, 1, 1),
(3, b'1', '2026-05-06', 'Lundi 08h', 2, 20, 20, 20, 7, 1, 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `cp`
--

CREATE TABLE `cp` (
  `a_de_leau` bit(1) DEFAULT NULL,
  `autorisation` bit(1) DEFAULT NULL,
  `encadrer_par_mena` bit(1) DEFAULT NULL,
  `est_electrifie` bit(1) DEFAULT NULL,
  `id_autorite_autorisation` int DEFAULT NULL,
  `id_centre` int NOT NULL,
  `id_iep` int DEFAULT NULL,
  `id_localite` int DEFAULT NULL,
  `id_naturecentre` int DEFAULT NULL,
  `id_periodicite` int DEFAULT NULL,
  `id_promoteur` int DEFAULT NULL,
  `nombre_visite` int DEFAULT NULL,
  `code_centre` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libellle_cp` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `localisation_centre` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_milieu_implentation` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `encadreur_non_mena` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `cp`
--

INSERT INTO `cp` (`a_de_leau`, `autorisation`, `encadrer_par_mena`, `est_electrifie`, `id_autorite_autorisation`, `id_centre`, `id_iep`, `id_localite`, `id_naturecentre`, `id_periodicite`, `id_promoteur`, `nombre_visite`, `code_centre`, `libellle_cp`, `localisation_centre`, `nom_milieu_implentation`, `encadreur_non_mena`) VALUES
(NULL, NULL, NULL, NULL, NULL, 5, NULL, NULL, NULL, NULL, NULL, NULL, 'CTR-CP-01', 'CP - CTR-CP-01', 'Centre CP Démo', 'Urbain', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `cp_niveau`
--

CREATE TABLE `cp_niveau` (
  `id_annee_scolaire` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_cp_niveau` int NOT NULL,
  `id_niveau_cp` int NOT NULL,
  `nombre_salle_cp` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `departement`
--

CREATE TABLE `departement` (
  `id_departement` int NOT NULL,
  `code_departement` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_departement` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `departement`
--

INSERT INTO `departement` (`id_departement`, `code_departement`, `nom_departement`) VALUES
(1, 'ABJ', 'Abidjan');

-- --------------------------------------------------------

--
-- Structure de la table `designation`
--

CREATE TABLE `designation` (
  `id_designation` int NOT NULL,
  `code_designation` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_designation` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `designation`
--

INSERT INTO `designation` (`id_designation`, `code_designation`, `libelle_designation`) VALUES
(1, 'ANIM', 'Animateur'),
(2, 'SUP', 'Superviseur'),
(3, 'CONS', 'Conseiller pédagogique');

-- --------------------------------------------------------

--
-- Structure de la table `difficulte`
--

CREATE TABLE `difficulte` (
  `id_difficulte` int NOT NULL,
  `code_difficulte` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_difficulte` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `difficulte`
--

INSERT INTO `difficulte` (`id_difficulte`, `code_difficulte`, `libelle_difficulte`) VALUES
(1, 'DIST', 'Éloignement géographique'),
(2, 'MAT', 'Manque de matériel'),
(3, 'LANG', 'Barrière linguistique');

-- --------------------------------------------------------

--
-- Structure de la table `difficulte_alpha`
--

CREATE TABLE `difficulte_alpha` (
  `id_centre` int NOT NULL,
  `id_difficulte` int NOT NULL,
  `id_difficulte_alpha` int NOT NULL,
  `code_difficulte_alpha` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `diplome`
--

CREATE TABLE `diplome` (
  `id_diplome` int NOT NULL,
  `code_diplome` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_diplome` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `diplome`
--

INSERT INTO `diplome` (`id_diplome`, `code_diplome`, `libelle_diplome`) VALUES
(1, 'CEPE', 'CEPE'),
(2, 'BEPC', 'BEPC'),
(3, 'BAC', 'Baccalauréat');

-- --------------------------------------------------------

--
-- Structure de la table `diplome_personnel`
--

CREATE TABLE `diplome_personnel` (
  `id_diplome` int NOT NULL,
  `id_diplome_personnel` int NOT NULL,
  `id_personnel` int NOT NULL,
  `libelle_autre_diplome` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `discipline`
--

CREATE TABLE `discipline` (
  `id_discipline` int NOT NULL,
  `code_discipline` varchar(50) DEFAULT NULL,
  `libelle_discipline` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `discipline`
--

INSERT INTO `discipline` (`id_discipline`, `code_discipline`, `libelle_discipline`) VALUES
(1, 'DIS-T', 'Lecture'),
(2, 'DIS-P2', 'Lecture P2'),
(3, 'DIS-TEST', 'Lecture');

-- --------------------------------------------------------

--
-- Structure de la table `document`
--

CREATE TABLE `document` (
  `id_centre` int NOT NULL,
  `id_document` int NOT NULL,
  `id_nature_document` int NOT NULL,
  `id_type_document` int NOT NULL,
  `ajour` varchar(5) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `bienrensigne` varchar(5) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `bientenu` varchar(5) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `existe` varchar(5) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `respmethode` varchar(5) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `code_document` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `domaine_activite`
--

CREATE TABLE `domaine_activite` (
  `id_domaine_activite` int NOT NULL,
  `code_domaine_activite` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_domaine_activite` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `domaine_activite`
--

INSERT INTO `domaine_activite` (`id_domaine_activite`, `code_domaine_activite`, `libelle_domaine_activite`) VALUES
(1, 'ALPHA', 'Alphabétisation'),
(2, 'POST', 'Post-alphabétisation'),
(3, 'NUM', 'Inclusion numérique');

-- --------------------------------------------------------

--
-- Structure de la table `domaine_activite_alpha`
--

CREATE TABLE `domaine_activite_alpha` (
  `id_centre` int NOT NULL,
  `id_domaine_activite` int NOT NULL,
  `id_domaine_activite_alpha` int NOT NULL,
  `libelle_domaine_activite_alpha` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `drena`
--

CREATE TABLE `drena` (
  `id_drena` int NOT NULL,
  `code_drena` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `telephone_drena` varchar(15) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mail_drena` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_drena` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `drena`
--

INSERT INTO `drena` (`id_drena`, `code_drena`, `telephone_drena`, `mail_drena`, `nom_drena`) VALUES
(1, 'DABJ', '0102030405', 'drena-abj@prism.local', 'DRENA Abidjan');

-- --------------------------------------------------------

--
-- Structure de la table `drena_departement`
--

CREATE TABLE `drena_departement` (
  `id_departement` int NOT NULL,
  `id_drena` int NOT NULL,
  `id_drena_depart` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_abandon_alpha`
--

CREATE TABLE `effectif_abandon_alpha` (
  `effectif_abandon_alpha_15_24_f` int DEFAULT NULL,
  `effectif_abandon_alpha_15_24_h` int DEFAULT NULL,
  `effectif_abandon_alpha_15_24_handicap_f` int DEFAULT NULL,
  `effectif_abandon_alpha_15_24_handicap_h` int DEFAULT NULL,
  `effectif_abandon_alpha_15_24_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_alpha_15_24_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_alpha_25_49_f` int DEFAULT NULL,
  `effectif_abandon_alpha_25_49_h` int DEFAULT NULL,
  `effectif_abandon_alpha_25_49_handicap_f` int DEFAULT NULL,
  `effectif_abandon_alpha_25_49_handicap_h` int DEFAULT NULL,
  `effectif_abandon_alpha_25_49_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_alpha_25_49_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_alpha_50_plus_f` int DEFAULT NULL,
  `effectif_abandon_alpha_50_plus_h` int DEFAULT NULL,
  `effectif_abandon_alpha_50_plus_handicap_f` int DEFAULT NULL,
  `effectif_abandon_alpha_50_plus_handicap_h` int DEFAULT NULL,
  `effectif_abandon_alpha_50_plus_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_alpha_50_plus_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_alpha_moins_15_f` int DEFAULT NULL,
  `effectif_abandon_alpha_moins_15_h` int DEFAULT NULL,
  `effectif_abandon_alpha_moins_15_handicap_f` int DEFAULT NULL,
  `effectif_abandon_alpha_moins_15_handicap_h` int DEFAULT NULL,
  `effectif_abandon_alpha_moins_15_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_alpha_moins_15_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_alpha_niveau_femme` int DEFAULT NULL,
  `effectif_abandon_alpha_niveau_homme` int DEFAULT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_abandon_alpha` int NOT NULL,
  `id_periode_activite` int NOT NULL,
  `code_effectif_abandon_alpha` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cause_abandon_alpha` tinytext COLLATE utf8mb4_general_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `effectif_abandon_alpha`
--

INSERT INTO `effectif_abandon_alpha` (`effectif_abandon_alpha_15_24_f`, `effectif_abandon_alpha_15_24_h`, `effectif_abandon_alpha_15_24_handicap_f`, `effectif_abandon_alpha_15_24_handicap_h`, `effectif_abandon_alpha_15_24_ivoirien_f`, `effectif_abandon_alpha_15_24_ivoirien_h`, `effectif_abandon_alpha_25_49_f`, `effectif_abandon_alpha_25_49_h`, `effectif_abandon_alpha_25_49_handicap_f`, `effectif_abandon_alpha_25_49_handicap_h`, `effectif_abandon_alpha_25_49_ivoirien_f`, `effectif_abandon_alpha_25_49_ivoirien_h`, `effectif_abandon_alpha_50_plus_f`, `effectif_abandon_alpha_50_plus_h`, `effectif_abandon_alpha_50_plus_handicap_f`, `effectif_abandon_alpha_50_plus_handicap_h`, `effectif_abandon_alpha_50_plus_ivoirien_f`, `effectif_abandon_alpha_50_plus_ivoirien_h`, `effectif_abandon_alpha_moins_15_f`, `effectif_abandon_alpha_moins_15_h`, `effectif_abandon_alpha_moins_15_handicap_f`, `effectif_abandon_alpha_moins_15_handicap_h`, `effectif_abandon_alpha_moins_15_ivoirien_f`, `effectif_abandon_alpha_moins_15_ivoirien_h`, `effectif_abandon_alpha_niveau_femme`, `effectif_abandon_alpha_niveau_homme`, `id_centre`, `id_effectif_abandon_alpha`, `id_periode_activite`, `code_effectif_abandon_alpha`, `cause_abandon_alpha`) VALUES
(6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 1, 1, 'EAA0000001', '6');

-- --------------------------------------------------------

--
-- Structure de la table `effectif_abandon_cec`
--

CREATE TABLE `effectif_abandon_cec` (
  `effectif_abandon_cec_12_16_f` int DEFAULT NULL,
  `effectif_abandon_cec_12_16_h` int DEFAULT NULL,
  `effectif_abandon_cec_12_16_handicap_f` int DEFAULT NULL,
  `effectif_abandon_cec_12_16_handicap_h` int DEFAULT NULL,
  `effectif_abandon_cec_12_16_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_cec_12_16_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_cec_3_5_f` int DEFAULT NULL,
  `effectif_abandon_cec_3_5_h` int DEFAULT NULL,
  `effectif_abandon_cec_3_5_handicap_f` int DEFAULT NULL,
  `effectif_abandon_cec_3_5_handicap_h` int DEFAULT NULL,
  `effectif_abandon_cec_3_5_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_cec_3_5_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_cec_6_8_f` int DEFAULT NULL,
  `effectif_abandon_cec_6_8_h` int DEFAULT NULL,
  `effectif_abandon_cec_6_8_handicap_f` int DEFAULT NULL,
  `effectif_abandon_cec_6_8_handicap_h` int DEFAULT NULL,
  `effectif_abandon_cec_6_8_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_cec_6_8_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_cec_9_11_f` int DEFAULT NULL,
  `effectif_abandon_cec_9_11_h` int DEFAULT NULL,
  `effectif_abandon_cec_9_11_handicap_f` int DEFAULT NULL,
  `effectif_abandon_cec_9_11_handicap_h` int DEFAULT NULL,
  `effectif_abandon_cec_9_11_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_cec_9_11_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_cec_moins_3_f` int DEFAULT NULL,
  `effectif_abandon_cec_moins_3_h` int DEFAULT NULL,
  `effectif_abandon_cec_moins_3_handicap_f` int DEFAULT NULL,
  `effectif_abandon_cec_moins_3_handicap_h` int DEFAULT NULL,
  `effectif_abandon_cec_moins_3_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_cec_moins_3_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_cec_niveau_cec` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_debut10` int NOT NULL,
  `id_niveau_sie` int NOT NULL,
  `code_effectif_abandon_cec` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cause_abandon_cec` tinytext COLLATE utf8mb4_general_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_abandon_cp`
--

CREATE TABLE `effectif_abandon_cp` (
  `effectif_abandon_cp_12_13_handicap_f` int DEFAULT NULL,
  `effectif_abandon_cp_12_13_handicap_h` int DEFAULT NULL,
  `effectif_abandon_cp_12_13_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_cp_12_13_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_cp_12_13_non_ivoiriien_h` int DEFAULT NULL,
  `effectif_abandon_cp_14_handicap_f` int DEFAULT NULL,
  `effectif_abandon_cp_14_handicap_h` int DEFAULT NULL,
  `effectif_abandon_cp_14_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_cp_14_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_cp_14_non_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_cp_14_non_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_cp_9_11_handicap_f` int DEFAULT NULL,
  `effectif_abandon_cp_9_11_handicap_h` int DEFAULT NULL,
  `effectif_abandon_cp_9_11_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_cp_9_11_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_cp_9_11_non_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_cp_9_11_non_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_cp_niveau_cp` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_debut15` int NOT NULL,
  `id_niveau_cp` int NOT NULL,
  `code_effectif_abandon_cp` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cause_abandon_cp` tinytext COLLATE utf8mb4_general_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_abondan_sie`
--

CREATE TABLE `effectif_abondan_sie` (
  `effectif_abandon_sie_10_12_handicap_f` int DEFAULT NULL,
  `effectif_abandon_sie_10_12_handicap_h` int DEFAULT NULL,
  `effectif_abandon_sie_10_12_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_sie_10_12_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_sie_10_12_non_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_sie_10_12_non_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_sie_13_14_et_plus_handicap_f` int DEFAULT NULL,
  `effectif_abandon_sie_13_14_et_plus_handicap_h` int DEFAULT NULL,
  `effectif_abandon_sie_13_14_et_plus_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_sie_13_14_et_plus_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_sie_13_14_et_plus_non_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_sie_13_14_et_plus_non_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_sie_3_handicap_f` int DEFAULT NULL,
  `effectif_abandon_sie_3_handicap_h` int DEFAULT NULL,
  `effectif_abandon_sie_3_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_sie_3_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_sie_3_non_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_sie_3_non_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_sie_4_6_handicap_f` int DEFAULT NULL,
  `effectif_abandon_sie_4_6_handicap_h` int DEFAULT NULL,
  `effectif_abandon_sie_4_6_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_sie_4_6_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_sie_4_6_non_ivoiriien_f` int DEFAULT NULL,
  `effectif_abandon_sie_4_6_non_ivoiriien_h` int DEFAULT NULL,
  `effectif_abandon_sie_7_9_handicap_f` int DEFAULT NULL,
  `effectif_abandon_sie_7_9_handicap_h` int DEFAULT NULL,
  `effectif_abandon_sie_7_9_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_sie_7_9_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_sie_7_9_non_ivoirien_f` int DEFAULT NULL,
  `effectif_abandon_sie_7_9_non_ivoirien_h` int DEFAULT NULL,
  `effectif_abandon_sie_niveau_sie` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_effectif_debut21` int NOT NULL,
  `id_niveau_sie` int NOT NULL,
  `code_abandon_effectif_sie` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cause_abandon_sie` tinytext COLLATE utf8mb4_general_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_admis_integration_cp`
--

CREATE TABLE `effectif_admis_integration_cp` (
  `effectif_admis_integration_cp_12_13_handicap_f` int DEFAULT NULL,
  `effectif_admis_integration_cp_12_13_handicap_h` int DEFAULT NULL,
  `effectif_admis_integration_cp_12_13_ivoirien_f` int DEFAULT NULL,
  `effectif_admis_integration_cp_12_13_ivoirien_h` int DEFAULT NULL,
  `effectif_admis_integration_cp_12_13_non_ivoiriien_f` int DEFAULT NULL,
  `effectif_admis_integration_cp_12_13_non_ivoiriien_h` int DEFAULT NULL,
  `effectif_admis_integration_cp_14_handicap_f` int DEFAULT NULL,
  `effectif_admis_integration_cp_14_handicap_h` int DEFAULT NULL,
  `effectif_admis_integration_cp_14_ivoirien_f` int DEFAULT NULL,
  `effectif_admis_integration_cp_14_ivoirien_h` int DEFAULT NULL,
  `effectif_admis_integration_cp_14_non_ivoirien_f` int DEFAULT NULL,
  `effectif_admis_integration_cp_14_non_ivoirien_h` int DEFAULT NULL,
  `effectif_admis_integration_cp_9_11_handicap_f` int DEFAULT NULL,
  `effectif_admis_integration_cp_9_11_handicap_h` int DEFAULT NULL,
  `effectif_admis_integration_cp_9_11_ivoirien_f` int DEFAULT NULL,
  `effectif_admis_integration_cp_9_11_ivoirien_h` int DEFAULT NULL,
  `effectif_admis_integration_cp_9_11_non_ivoirien_f` int DEFAULT NULL,
  `effectif_admis_integration_cp_9_11_non_ivoirien_h` int DEFAULT NULL,
  `effectif_admis_integration_cp_niveau_cp` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_debut16` int NOT NULL,
  `id_niveau_cp` int NOT NULL,
  `code_effectif_admis_integration_cp` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_alpha`
--

CREATE TABLE `effectif_alpha` (
  `effectif_alpha_15_24_f` int DEFAULT NULL,
  `effectif_alpha_15_24_h` int DEFAULT NULL,
  `effectif_alpha_15_24_handicap_f` int DEFAULT NULL,
  `effectif_alpha_15_24_handicap_h` int DEFAULT NULL,
  `effectif_alpha_15_24_ivoirien_f` int DEFAULT NULL,
  `effectif_alpha_15_24_ivoirien_h` int DEFAULT NULL,
  `effectif_alpha_25_49_f` int DEFAULT NULL,
  `effectif_alpha_25_49_h` int DEFAULT NULL,
  `effectif_alpha_25_49_handicap_f` int DEFAULT NULL,
  `effectif_alpha_25_49_handicap_h` int DEFAULT NULL,
  `effectif_alpha_25_49_ivoirien_f` int DEFAULT NULL,
  `effectif_alpha_25_49_ivoirien_h` int DEFAULT NULL,
  `effectif_alpha_50_plus_f` int DEFAULT NULL,
  `effectif_alpha_50_plus_h` int DEFAULT NULL,
  `effectif_alpha_50_plus_handicap_f` int DEFAULT NULL,
  `effectif_alpha_50_plus_handicap_h` int DEFAULT NULL,
  `effectif_alpha_50_plus_ivoirien_f` int DEFAULT NULL,
  `effectif_alpha_50_plus_ivoirien_h` int DEFAULT NULL,
  `effectif_alpha_moins_15_f` int DEFAULT NULL,
  `effectif_alpha_moins_15_h` int DEFAULT NULL,
  `effectif_alpha_moins_15_handicap_f` int DEFAULT NULL,
  `effectif_alpha_moins_15_handicap_h` int DEFAULT NULL,
  `effectif_alpha_moins_15_ivoirien_f` int DEFAULT NULL,
  `effectif_alpha_moins_15_ivoirien_h` int DEFAULT NULL,
  `effectif_alpha_niveau_f` int DEFAULT NULL,
  `effectif_alpha_niveau_h` int DEFAULT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_alpha` int NOT NULL,
  `id_periode_activite` int NOT NULL,
  `code_effectif_alpha` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_niveau_alpha` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `effectif_alpha`
--

INSERT INTO `effectif_alpha` (`effectif_alpha_15_24_f`, `effectif_alpha_15_24_h`, `effectif_alpha_15_24_handicap_f`, `effectif_alpha_15_24_handicap_h`, `effectif_alpha_15_24_ivoirien_f`, `effectif_alpha_15_24_ivoirien_h`, `effectif_alpha_25_49_f`, `effectif_alpha_25_49_h`, `effectif_alpha_25_49_handicap_f`, `effectif_alpha_25_49_handicap_h`, `effectif_alpha_25_49_ivoirien_f`, `effectif_alpha_25_49_ivoirien_h`, `effectif_alpha_50_plus_f`, `effectif_alpha_50_plus_h`, `effectif_alpha_50_plus_handicap_f`, `effectif_alpha_50_plus_handicap_h`, `effectif_alpha_50_plus_ivoirien_f`, `effectif_alpha_50_plus_ivoirien_h`, `effectif_alpha_moins_15_f`, `effectif_alpha_moins_15_h`, `effectif_alpha_moins_15_handicap_f`, `effectif_alpha_moins_15_handicap_h`, `effectif_alpha_moins_15_ivoirien_f`, `effectif_alpha_moins_15_ivoirien_h`, `effectif_alpha_niveau_f`, `effectif_alpha_niveau_h`, `id_centre`, `id_effectif_alpha`, `id_periode_activite`, `code_effectif_alpha`, `id_niveau_alpha`) VALUES
(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 7, 1, 1, 'EAL0000001', 1);

-- --------------------------------------------------------

--
-- Structure de la table `effectif_cec`
--

CREATE TABLE `effectif_cec` (
  `effectif_cec_12_16_f` int DEFAULT NULL,
  `effectif_cec_12_16_h` int DEFAULT NULL,
  `effectif_cec_12_16_handicap_f` int DEFAULT NULL,
  `effectif_cec_12_16_handicap_h` int DEFAULT NULL,
  `effectif_cec_12_16_ivoirien_f` int DEFAULT NULL,
  `effectif_cec_12_16_ivoirien_h` int DEFAULT NULL,
  `effectif_cec_3_5_f` int DEFAULT NULL,
  `effectif_cec_3_5_h` int DEFAULT NULL,
  `effectif_cec_3_5_handicap_f` int DEFAULT NULL,
  `effectif_cec_3_5_handicap_h` int DEFAULT NULL,
  `effectif_cec_3_5_ivoirien_f` int DEFAULT NULL,
  `effectif_cec_3_5_ivoirien_h` int DEFAULT NULL,
  `effectif_cec_6_8_f` int DEFAULT NULL,
  `effectif_cec_6_8_h` int DEFAULT NULL,
  `effectif_cec_6_8_handicap_f` int DEFAULT NULL,
  `effectif_cec_6_8_handicap_h` int DEFAULT NULL,
  `effectif_cec_6_8_ivoirien_f` int DEFAULT NULL,
  `effectif_cec_6_8_ivoirien_h` int DEFAULT NULL,
  `effectif_cec_9_11_f` int DEFAULT NULL,
  `effectif_cec_9_11_h` int DEFAULT NULL,
  `effectif_cec_9_11_handicap_f` int DEFAULT NULL,
  `effectif_cec_9_11_handicap_h` int DEFAULT NULL,
  `effectif_cec_9_11_ivoirien_f` int DEFAULT NULL,
  `effectif_cec_9_11_ivoirien_h` int DEFAULT NULL,
  `effectif_cec_moins_3_f` int DEFAULT NULL,
  `effectif_cec_moins_3_h` int DEFAULT NULL,
  `effectif_cec_moins_3_handicap_f` int DEFAULT NULL,
  `effectif_cec_moins_3_handicap_h` int DEFAULT NULL,
  `effectif_cec_moins_3_ivoirien_f` int DEFAULT NULL,
  `effectif_cec_moins_3_ivoirien_h` int DEFAULT NULL,
  `effectif_cec_niveau_cec` int DEFAULT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_cec` int NOT NULL,
  `id_niveau_sie` int NOT NULL,
  `id_periode_activite` int NOT NULL,
  `code_effectif_cec` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `effectif_cec`
--

INSERT INTO `effectif_cec` (`effectif_cec_12_16_f`, `effectif_cec_12_16_h`, `effectif_cec_12_16_handicap_f`, `effectif_cec_12_16_handicap_h`, `effectif_cec_12_16_ivoirien_f`, `effectif_cec_12_16_ivoirien_h`, `effectif_cec_3_5_f`, `effectif_cec_3_5_h`, `effectif_cec_3_5_handicap_f`, `effectif_cec_3_5_handicap_h`, `effectif_cec_3_5_ivoirien_f`, `effectif_cec_3_5_ivoirien_h`, `effectif_cec_6_8_f`, `effectif_cec_6_8_h`, `effectif_cec_6_8_handicap_f`, `effectif_cec_6_8_handicap_h`, `effectif_cec_6_8_ivoirien_f`, `effectif_cec_6_8_ivoirien_h`, `effectif_cec_9_11_f`, `effectif_cec_9_11_h`, `effectif_cec_9_11_handicap_f`, `effectif_cec_9_11_handicap_h`, `effectif_cec_9_11_ivoirien_f`, `effectif_cec_9_11_ivoirien_h`, `effectif_cec_moins_3_f`, `effectif_cec_moins_3_h`, `effectif_cec_moins_3_handicap_f`, `effectif_cec_moins_3_handicap_h`, `effectif_cec_moins_3_ivoirien_f`, `effectif_cec_moins_3_ivoirien_h`, `effectif_cec_niveau_cec`, `id_centre`, `id_effectif_cec`, `id_niveau_sie`, `id_periode_activite`, `code_effectif_cec`) VALUES
(27, 28, 32, 31, 30, 29, 5, 6, 16, 15, 14, 13, 7, 8, 20, 19, 17, 18, 21, 22, 26, 25, 24, 23, 3, 4, 12, 11, 10, 9, 60, 4, 1, 5, 1, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `effectif_cepe_cec`
--

CREATE TABLE `effectif_cepe_cec` (
  `cec_id_centre` int NOT NULL,
  `effectif_cepe_admis_fille_cec` int DEFAULT NULL,
  `effectif_cepe_admis_garcon_cec` int DEFAULT NULL,
  `effectif_cepe_admis_handicap_fille_cec` int DEFAULT NULL,
  `effectif_cepe_admis_handicap_garcon_cec` int DEFAULT NULL,
  `effectif_cepe_admis_ivoirien_cec` int DEFAULT NULL,
  `effectif_cepe_candidat_fille_cec` int DEFAULT NULL,
  `effectif_cepe_candidat_garcon_cec` int DEFAULT NULL,
  `effectif_cepe_candidat_handicap_fille_cec` int DEFAULT NULL,
  `effectif_cepe_candidat_handicap_garcon_cec` int DEFAULT NULL,
  `effectif_cepe_candidat_ivoirien_cec` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_debut12` int NOT NULL,
  `code_effectif_cepe_cec` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_cepe_cp`
--

CREATE TABLE `effectif_cepe_cp` (
  `effectif_cepe_admis_f_cp` int DEFAULT NULL,
  `effectif_cepe_admis_h_cp` int DEFAULT NULL,
  `effectif_cepe_admis_handicap_f_cp` int DEFAULT NULL,
  `effectif_cepe_admis_handicap_h_cp` int DEFAULT NULL,
  `effectif_cepe_admis_ivoirien_cp` int DEFAULT NULL,
  `effectif_cepe_candidat_f_cp` int DEFAULT NULL,
  `effectif_cepe_candidat_h_cp` int DEFAULT NULL,
  `effectif_cepe_candidat_handicap_f_cp` int DEFAULT NULL,
  `effectif_cepe_candidat_handicap_h_cp` int DEFAULT NULL,
  `effectif_cepe_candidat_ivoirien_cp` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_debut18` int NOT NULL,
  `code_effectif_cepe_cp` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_cp`
--

CREATE TABLE `effectif_cp` (
  `effectif_cp_12_13_handicap_f` int DEFAULT NULL,
  `effectif_cp_12_13_handicap_h` int DEFAULT NULL,
  `effectif_cp_12_13_ivoirien_f` int DEFAULT NULL,
  `effectif_cp_12_13_ivoirien_h` int DEFAULT NULL,
  `effectif_cp_12_13_non_ivoiriien_f` int DEFAULT NULL,
  `effectif_cp_12_13_non_ivoiriien_h` int DEFAULT NULL,
  `effectif_cp_14_handicap_f` int DEFAULT NULL,
  `effectif_cp_14_handicap_h` int DEFAULT NULL,
  `effectif_cp_14_ivoirien_f` int DEFAULT NULL,
  `effectif_cp_14_ivoirien_h` int DEFAULT NULL,
  `effectif_cp_14_non_ivoirien_f` int DEFAULT NULL,
  `effectif_cp_14_non_ivoirien_h` int DEFAULT NULL,
  `effectif_cp_9_11_handicap_f` int DEFAULT NULL,
  `effectif_cp_9_11_handicap_h` int DEFAULT NULL,
  `effectif_cp_9_11_ivoirien_f` int DEFAULT NULL,
  `effectif_cp_9_11_ivoirien_h` int DEFAULT NULL,
  `effectif_cp_9_11_non_ivoirien_f` int DEFAULT NULL,
  `effectif_cp_9_11_non_ivoirien_h` int DEFAULT NULL,
  `effectif_cp_niveau_cp` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_debut14` int NOT NULL,
  `id_niveau_cp` int NOT NULL,
  `code_effectif_cp` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `effectif_cp`
--

INSERT INTO `effectif_cp` (`effectif_cp_12_13_handicap_f`, `effectif_cp_12_13_handicap_h`, `effectif_cp_12_13_ivoirien_f`, `effectif_cp_12_13_ivoirien_h`, `effectif_cp_12_13_non_ivoiriien_f`, `effectif_cp_12_13_non_ivoiriien_h`, `effectif_cp_14_handicap_f`, `effectif_cp_14_handicap_h`, `effectif_cp_14_ivoirien_f`, `effectif_cp_14_ivoirien_h`, `effectif_cp_14_non_ivoirien_f`, `effectif_cp_14_non_ivoirien_h`, `effectif_cp_9_11_handicap_f`, `effectif_cp_9_11_handicap_h`, `effectif_cp_9_11_ivoirien_f`, `effectif_cp_9_11_ivoirien_h`, `effectif_cp_9_11_non_ivoirien_f`, `effectif_cp_9_11_non_ivoirien_h`, `effectif_cp_niveau_cp`, `id_annee_scolaire`, `id_centre`, `id_effectif_debut14`, `id_niveau_cp`, `code_effectif_cp`) VALUES
(23, 22, 15, 9, 25, 24, 26, 26, 17, 16, 27, 28, 19, 18, 11, 10, 20, 21, 29, 1, 5, 1, 8, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `effectif_integration_formel_cp`
--

CREATE TABLE `effectif_integration_formel_cp` (
  `effectif_integration_formel_cp_12_13_handicap_f` int DEFAULT NULL,
  `effectif_integration_formel_cp_12_13_handicap_h` int DEFAULT NULL,
  `effectif_integration_formel_cp_12_13_ivoirien_f` int DEFAULT NULL,
  `effectif_integration_formel_cp_12_13_ivoirien_h` int DEFAULT NULL,
  `effectif_integration_formel_cp_12_13_non_ivoiriien_f` int DEFAULT NULL,
  `effectif_integration_formel_cp_12_13_non_ivoiriien_h` int DEFAULT NULL,
  `effectif_integration_formel_cp_14_handicap_f` int DEFAULT NULL,
  `effectif_integration_formel_cp_14_handicap_h` int DEFAULT NULL,
  `effectif_integration_formel_cp_14_ivoirien_f` int DEFAULT NULL,
  `effectif_integration_formel_cp_14_ivoirien_h` int DEFAULT NULL,
  `effectif_integration_formel_cp_14_non_ivoirien_f` int DEFAULT NULL,
  `effectif_integration_formel_cp_14_non_ivoirien_h` int DEFAULT NULL,
  `effectif_integration_formel_cp_9_11_handicap_f` int DEFAULT NULL,
  `effectif_integration_formel_cp_9_11_handicap_h` int DEFAULT NULL,
  `effectif_integration_formel_cp_9_11_ivoirien_f` int DEFAULT NULL,
  `effectif_integration_formel_cp_9_11_ivoirien_h` int DEFAULT NULL,
  `effectif_integration_formel_cp_9_11_non_ivoirien_f` int DEFAULT NULL,
  `effectif_integration_formel_cp_9_11_non_ivoirien_h` int DEFAULT NULL,
  `effectif_integration_formel_cp_niveau_cp` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_debut17` int NOT NULL,
  `id_niveau_cp` int NOT NULL,
  `code_effectif_integration_formel_cp` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_passage_alpha`
--

CREATE TABLE `effectif_passage_alpha` (
  `effectif_apassage_alpha_50_plus_ivoirien_f` int DEFAULT NULL,
  `effectif_passage__alpha_moins_15_ivoirien_f` int DEFAULT NULL,
  `effectif_passage_alpha_15_24_f` int DEFAULT NULL,
  `effectif_passage_alpha_15_24_h` int DEFAULT NULL,
  `effectif_passage_alpha_15_24_handicap_f` int DEFAULT NULL,
  `effectif_passage_alpha_15_24_handicap_h` int DEFAULT NULL,
  `effectif_passage_alpha_15_24_ivoirien_f` int DEFAULT NULL,
  `effectif_passage_alpha_15_24_ivoirien_h` int DEFAULT NULL,
  `effectif_passage_alpha_25_49_f` int DEFAULT NULL,
  `effectif_passage_alpha_25_49_h` int DEFAULT NULL,
  `effectif_passage_alpha_25_49_handicap_f` int DEFAULT NULL,
  `effectif_passage_alpha_25_49_handicap_h` int DEFAULT NULL,
  `effectif_passage_alpha_25_49_ivoirien_f` int DEFAULT NULL,
  `effectif_passage_alpha_25_49_ivoirien_h` int DEFAULT NULL,
  `effectif_passage_alpha_50_plus_f` int DEFAULT NULL,
  `effectif_passage_alpha_50_plus_h` int DEFAULT NULL,
  `effectif_passage_alpha_50_plus_handicap_f` int DEFAULT NULL,
  `effectif_passage_alpha_50_plus_handicap_h` int DEFAULT NULL,
  `effectif_passage_alpha_50_plus_ivoirien_h` int DEFAULT NULL,
  `effectif_passage_alpha_moins_15_f` int DEFAULT NULL,
  `effectif_passage_alpha_moins_15_h` int DEFAULT NULL,
  `effectif_passage_alpha_moins_15_handicap_f` int DEFAULT NULL,
  `effectif_passage_alpha_moins_15_handicap_h` int DEFAULT NULL,
  `effectif_passage_alpha_moins_15_ivoirien_h` int DEFAULT NULL,
  `effectif_passage_alpha_niveau_femme` int DEFAULT NULL,
  `effectif_passage_alpha_niveau_homme` int DEFAULT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_passage_alpha` int NOT NULL,
  `id_periode_activite` int NOT NULL,
  `code_effectif_passage_alpha` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_promu_cec`
--

CREATE TABLE `effectif_promu_cec` (
  `effectif_promu_cec_12_16_f` int DEFAULT NULL,
  `effectif_promu_cec_12_16_h` int DEFAULT NULL,
  `effectif_promu_cec_12_16_handicap_f` int DEFAULT NULL,
  `effectif_promu_cec_12_16_handicap_h` int DEFAULT NULL,
  `effectif_promu_cec_12_16_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_cec_12_16_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_cec_3_5_f` int DEFAULT NULL,
  `effectif_promu_cec_3_5_h` int DEFAULT NULL,
  `effectif_promu_cec_3_5_handicap_f` int DEFAULT NULL,
  `effectif_promu_cec_3_5_handicap_h` int DEFAULT NULL,
  `effectif_promu_cec_3_5_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_cec_3_5_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_cec_6_8_f` int DEFAULT NULL,
  `effectif_promu_cec_6_8_h` int DEFAULT NULL,
  `effectif_promu_cec_6_8_handicap_f` int DEFAULT NULL,
  `effectif_promu_cec_6_8_handicap_h` int DEFAULT NULL,
  `effectif_promu_cec_6_8_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_cec_6_8_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_cec_9_11_f` int DEFAULT NULL,
  `effectif_promu_cec_9_11_h` int DEFAULT NULL,
  `effectif_promu_cec_9_11_handicap_f` int DEFAULT NULL,
  `effectif_promu_cec_9_11_handicap_h` int DEFAULT NULL,
  `effectif_promu_cec_9_11_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_cec_9_11_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_cec_moins_3_f` int DEFAULT NULL,
  `effectif_promu_cec_moins_3_h` int DEFAULT NULL,
  `effectif_promu_cec_moins_3_handicap_f` int DEFAULT NULL,
  `effectif_promu_cec_moins_3_handicap_h` int DEFAULT NULL,
  `effectif_promu_cec_moins_3_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_cec_moins_3_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_cec_niveau_cec` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_debut11` int NOT NULL,
  `id_niveau_sie` int NOT NULL,
  `code_effectif_promu_cec` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_promu_sie`
--

CREATE TABLE `effectif_promu_sie` (
  `effectif_promu_sie_10_12_handicap_f` int DEFAULT NULL,
  `effectif_promu_sie_10_12_handicap_h` int DEFAULT NULL,
  `effectif_promu_sie_10_12_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_sie_10_12_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_sie_10_12_non_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_sie_10_12_non_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_sie_13_14_et_plus_handicap_f` int DEFAULT NULL,
  `effectif_promu_sie_13_14_et_plus_handicap_h` int DEFAULT NULL,
  `effectif_promu_sie_13_14_et_plus_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_sie_13_14_et_plus_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_sie_3_handicap_f` int DEFAULT NULL,
  `effectif_promu_sie_3_handicap_h` int DEFAULT NULL,
  `effectif_promu_sie_3_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_sie_3_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_sie_3_non_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_sie_3_non_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_sie_4_6_handicap_f` int DEFAULT NULL,
  `effectif_promu_sie_4_6_handicap_h` int DEFAULT NULL,
  `effectif_promu_sie_4_6_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_sie_4_6_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_sie_4_6_non_ivoiriien_f` int DEFAULT NULL,
  `effectif_promu_sie_4_6_non_ivoiriien_h` int DEFAULT NULL,
  `effectif_promu_sie_7_9_handicap_f` int DEFAULT NULL,
  `effectif_promu_sie_7_9_handicap_h` int DEFAULT NULL,
  `effectif_promu_sie_7_9_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_sie_7_9_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_sie_7_9_non_ivoirien_f` int DEFAULT NULL,
  `effectif_promu_sie_7_9_non_ivoirien_h` int DEFAULT NULL,
  `effectif_promu_sie_niveau_sie` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_effectif_debut22` int NOT NULL,
  `id_niveau_sie` int NOT NULL,
  `code_effectif_promu_sie` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_reverse_formel_sie`
--

CREATE TABLE `effectif_reverse_formel_sie` (
  `effectif_reverse_formel_sie_10_12_handicap_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_10_12_handicap_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_10_12_ivoirien_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_10_12_ivoirien_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_10_12_non_ivoirien_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_10_12_non_ivoirien_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_13_14_et_plus_handicap_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_13_14_et_plus_handicap_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_13_14_et_plus_ivoirien_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_13_14_et_plus_ivoirien_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_3_handicap_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_3_handicap_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_3_ivoirien_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_3_ivoirien_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_3_non_ivoirien_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_3_non_ivoirien_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_4_6_handicap_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_4_6_handicap_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_4_6_ivoirien_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_4_6_ivoirien_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_4_6_non_ivoiriien_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_4_6_non_ivoiriien_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_7_9_handicap_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_7_9_handicap_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_7_9_ivoirien_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_7_9_ivoirien_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_7_9_non_ivoirien_f` int DEFAULT NULL,
  `effectif_reverse_formel_sie_7_9_non_ivoirien_h` int DEFAULT NULL,
  `effectif_reverse_formel_sie_niveau_sie` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_effectif_debut23` int NOT NULL,
  `id_niveau_sie` int NOT NULL,
  `code_effectif_reverse_formel_sie` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_sie`
--

CREATE TABLE `effectif_sie` (
  `effectif_sie_10_12_handicap_f` int DEFAULT NULL,
  `effectif_sie_10_12_handicap_h` int DEFAULT NULL,
  `effectif_sie_10_12_ivoirien_f` int DEFAULT NULL,
  `effectif_sie_10_12_ivoirien_h` int DEFAULT NULL,
  `effectif_sie_10_12_non_ivoirien_f` int DEFAULT NULL,
  `effectif_sie_10_12_non_ivoirien_h` int DEFAULT NULL,
  `effectif_sie_13_14_et_plus_handicap_f` int DEFAULT NULL,
  `effectif_sie_13_14_et_plus_handicap_h` int DEFAULT NULL,
  `effectif_sie_13_14_et_plus_ivoirien_f` int DEFAULT NULL,
  `effectif_sie_13_14_et_plus_ivoirien_h` int DEFAULT NULL,
  `effectif_sie_3_handicap_f` int DEFAULT NULL,
  `effectif_sie_3_handicap_h` int DEFAULT NULL,
  `effectif_sie_3_ivoirien_f` int DEFAULT NULL,
  `effectif_sie_3_ivoirien_h` int DEFAULT NULL,
  `effectif_sie_3_non_ivoirien_f` int DEFAULT NULL,
  `effectif_sie_3_non_ivoirien_h` int DEFAULT NULL,
  `effectif_sie_4_6_handicap_f` int DEFAULT NULL,
  `effectif_sie_4_6_handicap_h` int DEFAULT NULL,
  `effectif_sie_4_6_ivoirien_f` int DEFAULT NULL,
  `effectif_sie_4_6_ivoirien_h` int DEFAULT NULL,
  `effectif_sie_4_6_non_ivoiriien_f` int DEFAULT NULL,
  `effectif_sie_4_6_non_ivoiriien_h` int DEFAULT NULL,
  `effectif_sie_7_9_handicap_f` int DEFAULT NULL,
  `effectif_sie_7_9_handicap_h` int DEFAULT NULL,
  `effectif_sie_7_9_ivoirien_f` int DEFAULT NULL,
  `effectif_sie_7_9_ivoirien_h` int DEFAULT NULL,
  `effectif_sie_7_9_non_ivoirien_f` int DEFAULT NULL,
  `effectif_sie_7_9_non_ivoirien_h` int DEFAULT NULL,
  `effectif_sie_niveau_sie` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_effectif_debut20` int NOT NULL,
  `id_niveau_sie` int NOT NULL,
  `code_effectif_sie` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_situation_handicap_alpha`
--

CREATE TABLE `effectif_situation_handicap_alpha` (
  `effectif_situation_handicap__alpha_moins_15_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_15_24_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_15_24_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_15_24_handicap_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_15_24_handicap_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_15_24_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_15_24_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_25_49_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_25_49_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_25_49_handicap_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_25_49_handicap_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_25_49_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_25_49_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_50_plus_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_50_plus_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_50_plus_handicap_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_50_plus_handicap_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_50_plus_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_50_plus_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_moins_15_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_moins_15_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_moins_15_handicap_f` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_moins_15_handicap_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_moins_15_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_alpha_niveau_homme` int DEFAULT NULL,
  `effectif_situation_handicapalpha_niveau_femme` int DEFAULT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_situation_handicap_alpha` int NOT NULL,
  `id_periode_activite` int NOT NULL,
  `code_effectif_situation_handicap_alpha` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_situation_handicap_cec`
--

CREATE TABLE `effectif_situation_handicap_cec` (
  `effectif_situation_handicap_cec_12_16_f` int DEFAULT NULL,
  `effectif_situation_handicap_cec_12_16_h` int DEFAULT NULL,
  `effectif_situation_handicap_cec_12_16_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_cec_12_16_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_cec_3_5_f` int DEFAULT NULL,
  `effectif_situation_handicap_cec_3_5_h` int DEFAULT NULL,
  `effectif_situation_handicap_cec_3_5_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_cec_3_5_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_cec_6_8_f` int DEFAULT NULL,
  `effectif_situation_handicap_cec_6_8_h` int DEFAULT NULL,
  `effectif_situation_handicap_cec_6_8_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_cec_6_8_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_cec_9_11_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_cec_9_11_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_cec_moins_3_f` int DEFAULT NULL,
  `effectif_situation_handicap_cec_moins_3_h` int DEFAULT NULL,
  `effectif_situation_handicap_cec_moins_3_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_cec_moins_3_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_cec_niveau_cec` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_effectif_debut13` int NOT NULL,
  `id_niveau_sie` int NOT NULL,
  `code_effectif_situation_handicap_cec` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `effectif_situation_handicap_cp`
--

CREATE TABLE `effectif_situation_handicap_cp` (
  `effectif_situation_handicap_cp_12_13_handicap_f` int DEFAULT NULL,
  `effectif_situation_handicap_cp_12_13_handicap_h` int DEFAULT NULL,
  `effectif_situation_handicap_cp_12_13_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_cp_12_13_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_cp_12_13_non_ivoiriien_f` int DEFAULT NULL,
  `effectif_situation_handicap_cp_12_13_non_ivoiriien_h` int DEFAULT NULL,
  `effectif_situation_handicap_cp_14_handicap_f` int DEFAULT NULL,
  `effectif_situation_handicap_cp_14_handicap_h` int DEFAULT NULL,
  `effectif_situation_handicap_cp_14_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_cp_14_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_cp_14_non_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_cp_14_non_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_cp_9_11_handicap_f` int DEFAULT NULL,
  `effectif_situation_handicap_cp_9_11_handicap_h` int DEFAULT NULL,
  `effectif_situation_handicap_cp_9_11_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_cp_9_11_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_cp_9_11_non_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_cp_9_11_non_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_cp_niveau_cp` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_effectif_debut19` int NOT NULL,
  `id_niveau_cp` int NOT NULL,
  `code_effectif_situation_handicap_cp` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `effectif_situation_handicap_cp`
--

INSERT INTO `effectif_situation_handicap_cp` (`effectif_situation_handicap_cp_12_13_handicap_f`, `effectif_situation_handicap_cp_12_13_handicap_h`, `effectif_situation_handicap_cp_12_13_ivoirien_f`, `effectif_situation_handicap_cp_12_13_ivoirien_h`, `effectif_situation_handicap_cp_12_13_non_ivoiriien_f`, `effectif_situation_handicap_cp_12_13_non_ivoiriien_h`, `effectif_situation_handicap_cp_14_handicap_f`, `effectif_situation_handicap_cp_14_handicap_h`, `effectif_situation_handicap_cp_14_ivoirien_f`, `effectif_situation_handicap_cp_14_ivoirien_h`, `effectif_situation_handicap_cp_14_non_ivoirien_f`, `effectif_situation_handicap_cp_14_non_ivoirien_h`, `effectif_situation_handicap_cp_9_11_handicap_f`, `effectif_situation_handicap_cp_9_11_handicap_h`, `effectif_situation_handicap_cp_9_11_ivoirien_f`, `effectif_situation_handicap_cp_9_11_ivoirien_h`, `effectif_situation_handicap_cp_9_11_non_ivoirien_f`, `effectif_situation_handicap_cp_9_11_non_ivoirien_h`, `effectif_situation_handicap_cp_niveau_cp`, `id_annee_scolaire`, `id_centre`, `id_effectif_debut19`, `id_niveau_cp`, `code_effectif_situation_handicap_cp`) VALUES
(11, 11, 11, 11, 11, 11, 11, NULL, 11, 11, 11, 11, 11, 11, 11, 19, 11, 11, 11, 1, 5, 1, 2, 'ESH0000001'),
(11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 1, 5, 2, 6, 'ESH0000002');

-- --------------------------------------------------------

--
-- Structure de la table `effectif_situation_handicap_sie`
--

CREATE TABLE `effectif_situation_handicap_sie` (
  `effectif_situation_handicap_sie_10_12_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_sie_10_12_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_sie_10_12_non_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_sie_13_14_et_plus_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_sie_13_14_et_plus_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_sie_3_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_sie_3_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_sie_3_non_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_sie_3_non_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_sie_4_6_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_sie_4_6_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_sie_7_9_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_sie_7_9_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_sie_7_9_non_ivoirien_f` int DEFAULT NULL,
  `effectif_situation_handicap_sie_7_9_non_ivoirien_h` int DEFAULT NULL,
  `effectif_situation_handicap_sie_niveau_sie` int DEFAULT NULL,
  `effectif_situation_handicapl_sie_10_12_non_ivoirien_h` int DEFAULT NULL,
  `id_annee_scolaire` int NOT NULL,
  `id_effectif_debut24` int NOT NULL,
  `id_niveau_sie` int NOT NULL,
  `code_effectif_situation_handicap_sie` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `evaluation`
--

CREATE TABLE `evaluation` (
  `id_evaluation` int NOT NULL,
  `id_alpha` int NOT NULL,
  `id_niveau_evaluation` int DEFAULT NULL,
  `id_periode_evaluation` int DEFAULT NULL,
  `id_taux_evaluation` int DEFAULT NULL,
  `id_theme_evaluation` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `evaluation`
--

INSERT INTO `evaluation` (`id_evaluation`, `id_alpha`, `id_niveau_evaluation`, `id_periode_evaluation`, `id_taux_evaluation`, `id_theme_evaluation`) VALUES
(1, 7, 1, 1, 1, 1),
(2, 7, 1, 1, 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `fonction`
--

CREATE TABLE `fonction` (
  `id_fonction` int NOT NULL,
  `code_fonction` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_fonction` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `fonction`
--

INSERT INTO `fonction` (`id_fonction`, `code_fonction`, `libelle_fonction`) VALUES
(1, 'DIR', 'Directeur de centre'),
(2, 'ADJOINT', 'Adjoint'),
(3, 'ENSEIG', 'Enseignant');

-- --------------------------------------------------------

--
-- Structure de la table `fonctionnalite`
--

CREATE TABLE `fonctionnalite` (
  `id_fonctionnalite` int NOT NULL,
  `code_fonctionnalite` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `module` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_fonctionnalite` varchar(150) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `fonctionnalite`
--

INSERT INTO `fonctionnalite` (`id_fonctionnalite`, `code_fonctionnalite`, `module`, `libelle_fonctionnalite`) VALUES
(1, 'CAMPAGNE', 'Campagnes', 'Gestion des campagnes'),
(2, 'CENTRE', 'Centres', 'Gestion des centres'),
(3, 'ALPHA', 'Alpha', 'Gestion des dispositifs Alpha'),
(4, 'PERSONNEL', 'Personnel', 'Gestion du personnel'),
(5, 'UTILISATEUR', 'Sécurité', 'Gestion des utilisateurs'),
(6, 'CIVILITE', 'API', 'Gestion civilite'),
(7, 'EFFECTIF-CEPE-CEC', 'API', 'Gestion effectif-cepe-cec'),
(8, 'PERMISSION', 'API', 'Gestion permission'),
(9, 'SUPPORTDIDACTIQUES', 'API', 'Gestion supportdidactiques'),
(10, 'EFFECTIF-INTEGRATION-FORMEL-CP', 'API', 'Gestion effectif-integration-formel-cp'),
(11, 'NATUREDOCUMENT', 'API', 'Gestion naturedocument'),
(12, 'DOCUMENTS', 'API', 'Gestion documents'),
(13, 'AUTORITEAUTORISATION', 'API', 'Gestion autoriteautorisation'),
(14, 'EFFECTIF-ABANDON-CEC', 'API', 'Gestion effectif-abandon-cec'),
(15, 'EFFECTIF-REVERSE-FORMEL-SIE', 'API', 'Gestion effectif-reverse-formel-sie'),
(16, 'ONG', 'API', 'Gestion ong'),
(17, 'CP-NIVEAU', 'API', 'Gestion cp-niveau'),
(18, 'DOMAINE-ACTIVITE', 'API', 'Gestion domaine-activite'),
(19, 'DEPARTEMENT', 'API', 'Gestion departement'),
(20, 'EFFECTIF-SITUATION-HANDICAP-ALPHA', 'API', 'Gestion effectif-situation-handicap-alpha'),
(21, 'SOCIETE-CIVILE', 'API', 'Gestion societe-civile'),
(22, 'SOUS-PREFECTURE', 'API', 'Gestion sous-prefecture'),
(23, 'MODEALPHABETISATIONS', 'API', 'Gestion modealphabetisations'),
(24, 'EFFECTIF-SITUATION-HANDICAP-CP', 'API', 'Gestion effectif-situation-handicap-cp'),
(25, 'PERSONNEMORALE', 'API', 'Gestion personnemorale'),
(26, 'CAMPAGNES', 'API', 'Gestion campagnes'),
(27, 'IMPACT', 'API', 'Gestion impact'),
(28, 'CATEGORIE-CENTRE-ALPHA', 'API', 'Gestion categorie-centre-alpha'),
(29, 'PARTICULIER', 'API', 'Gestion particulier'),
(30, 'ROLE-FONCTIONNALITE-PERMISSION', 'API', 'Gestion role-fonctionnalite-permission'),
(31, 'INFRASTRUCTURE-CENTRE', 'API', 'Gestion infrastructure-centre'),
(32, 'COMPETENCE', 'API', 'Gestion competence'),
(33, 'TYPEALPHAS', 'API', 'Gestion typealphas'),
(34, 'DIFFICULTE', 'API', 'Gestion difficulte'),
(35, 'COMMUNAUTES', 'API', 'Gestion communautes'),
(36, 'APP-ROLE', 'API', 'Gestion app-role'),
(37, 'PROMOTEUR', 'API', 'Gestion promoteur'),
(38, 'LANGUEAPPRENTISSAGES', 'API', 'Gestion langueapprentissages'),
(39, 'NIVEAU-PERSONNEL', 'API', 'Gestion niveau-personnel'),
(40, 'PARTENAIRES', 'API', 'Gestion partenaires'),
(41, 'PERSONNEPHYSIQUE', 'API', 'Gestion personnephysique'),
(42, 'EFFECTIF-ADMIS-INTEGRATION-CP', 'API', 'Gestion effectif-admis-integration-cp'),
(43, 'IEP', 'API', 'Gestion iep'),
(44, 'DESIGNATION', 'API', 'Gestion designation'),
(45, 'COMPETENCE-CENTRE', 'API', 'Gestion competence-centre'),
(46, 'PERIODICITES', 'API', 'Gestion periodicites'),
(47, 'NIVEAUSIECEC', 'API', 'Gestion niveausiecec'),
(48, 'SIE-NIVEAU', 'API', 'Gestion sie-niveau'),
(49, 'CEC', 'API', 'Gestion cec'),
(50, 'APPUI-PARTENAIRE', 'API', 'Gestion appui-partenaire'),
(51, 'CATEGORIEAPPUIS', 'API', 'Gestion categorieappuis'),
(52, 'EFFECTIF-ABONDAN-SIE', 'API', 'Gestion effectif-abondan-sie'),
(53, 'CP', 'API', 'Gestion cp'),
(54, 'PTF', 'API', 'Gestion ptf'),
(55, 'MODEALPHA', 'API', 'Gestion modealpha'),
(56, 'CEC-NIVEAU', 'API', 'Gestion cec-niveau'),
(57, 'DIPLOME', 'API', 'Gestion diplome'),
(58, 'DRENA-DEPARTEMENT', 'API', 'Gestion drena-departement'),
(59, 'NIVEAUALPHA', 'API', 'Gestion niveaualpha'),
(60, 'EFFECTIF-PROMU-CEC', 'API', 'Gestion effectif-promu-cec'),
(61, 'RESSOURCE-FINANCIERE-MATERIEL', 'API', 'Gestion ressource-financiere-materiel'),
(62, 'PROGRAMME-ALPHA', 'API', 'Gestion programme-alpha'),
(63, 'EFFECTIF-CEC', 'API', 'Gestion effectif-cec'),
(64, 'EFFECTIF-ABANDON-ALPHA', 'API', 'Gestion effectif-abandon-alpha'),
(65, 'REGIMEALPHABETISATIONS', 'API', 'Gestion regimealphabetisations'),
(66, 'DRENA', 'API', 'Gestion drena'),
(67, 'ANNEESCOLAIRE', 'API', 'Gestion anneescolaire'),
(68, 'EFFECTIF-ALPHA', 'API', 'Gestion effectif-alpha'),
(69, 'SUPPORT-DIDACTIQUE-ALPHA', 'API', 'Gestion support-didactique-alpha'),
(70, 'EFFECTIF-PASSAGE-ALPHA', 'API', 'Gestion effectif-passage-alpha'),
(71, 'LOCALITE-D-IMPLANTATION', 'API', 'Gestion localite-d-implantation'),
(72, 'EFFECTIF-ABANDON-CP', 'API', 'Gestion effectif-abandon-cp'),
(73, 'STATUTPERSONNELS', 'API', 'Gestion statutpersonnels'),
(74, 'INFRASTRUCTURE', 'API', 'Gestion infrastructure'),
(75, 'DIPLOME-PERSONNEL', 'API', 'Gestion diplome-personnel'),
(76, 'EFFECTIF-SITUATION-HANDICAP-CEC', 'API', 'Gestion effectif-situation-handicap-cec'),
(77, 'EFFECTIF-CP', 'API', 'Gestion effectif-cp'),
(78, 'TYPEDOCUMENTS', 'API', 'Gestion typedocuments'),
(79, 'STRUCTURE-FORMATION-CERTIFICATION', 'API', 'Gestion structure-formation-certification'),
(80, 'PERIODEACTIVITES', 'API', 'Gestion periodeactivites'),
(81, 'FONCTIONS', 'API', 'Gestion fonctions'),
(82, 'EFFECTIF-SIE', 'API', 'Gestion effectif-sie'),
(83, 'MATERIELPEDAGOGIQUES', 'API', 'Gestion materielpedagogiques'),
(84, 'SIE', 'API', 'Gestion sie'),
(85, 'MILIEU-IMPLANTATION', 'API', 'Gestion milieu-implantation'),
(86, 'MATERIELALPHA', 'API', 'Gestion materielalpha'),
(87, 'DOMAINE-ACTIVITE-ALPHA', 'API', 'Gestion domaine-activite-alpha'),
(88, 'DIFFICULTE-ALPHA', 'API', 'Gestion difficulte-alpha'),
(89, 'EFFECTIF-PROMU-SIE', 'API', 'Gestion effectif-promu-sie'),
(90, 'PROGRAMME', 'API', 'Gestion programme'),
(91, 'EFFECTIF-SITUATION-HANDICAP-SIE', 'API', 'Gestion effectif-situation-handicap-sie'),
(92, 'FONCTIONNALITE', 'API', 'Gestion fonctionnalite'),
(93, 'NATURECENTRE', 'API', 'Gestion naturecentre'),
(94, 'MINISTERES', 'API', 'Gestion ministeres'),
(95, 'NIVEAUCP', 'API', 'Gestion niveaucp'),
(96, 'IMPACT-ALPHA', 'API', 'Gestion impact-alpha'),
(97, 'COMMUNE', 'API', 'Gestion commune'),
(98, 'CENTRES', 'API', 'Gestion centres'),
(99, 'EFFECTIF-CEPE-CP', 'API', 'Gestion effectif-cepe-cp'),
(100, 'APP-USERS', 'API', 'Gestion app-users'),
(101, 'ADMIN', 'API', 'Gestion admin'),
(102, 'VISITES', 'API', 'Gestion visites'),
(103, 'TYPE-PERSONNE-MORALE', 'API', 'Gestion type-personne-morale'),
(104, 'ASPECTS-A-AMELIORER', 'API', 'Gestion aspects-a-ameliorer'),
(105, 'NIVEAUX-EVALUATION', 'API', 'Gestion niveaux-evaluation'),
(106, 'VISITE', 'API', 'Gestion visite'),
(107, 'MANUELS', 'API', 'Gestion manuels'),
(108, 'TAUX-EVALUATION', 'API', 'Gestion taux-evaluation'),
(109, 'EVALUATION', 'API', 'Gestion evaluation'),
(110, 'THEMES-EVALUATION-NIVEAU1', 'API', 'Gestion themes-evaluation-niveau1'),
(111, 'DISCIPLINES', 'API', 'Gestion disciplines'),
(112, 'PERIODES-EVALUATION', 'API', 'Gestion periodes-evaluation'),
(113, 'THEMES-EVALUATION-NIVEAU2-POST-ALPHA', 'API', 'Gestion themes-evaluation-niveau2-post-alpha'),
(114, 'NIVEAUX-CONTROLE', 'API', 'Gestion niveaux-controle'),
(115, 'PERFORMANCE', 'API', 'Gestion performance'),
(116, 'CONTROLE', 'API', 'Gestion controle'),
(117, 'THEMES-EVALUATION', 'API', 'Gestion themes-evaluation');

-- --------------------------------------------------------

--
-- Structure de la table `iep`
--

CREATE TABLE `iep` (
  `id_drena` int NOT NULL,
  `id_iep` int NOT NULL,
  `code_iep` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `telephone_iep` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mail_iep` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_iep` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `iep`
--

INSERT INTO `iep` (`id_drena`, `id_iep`, `code_iep`, `telephone_iep`, `mail_iep`, `nom_iep`) VALUES
(1, 1, 'IEP1', '0708091011', 'iep-cocody@prism.local', 'IEP Cocody');

-- --------------------------------------------------------

--
-- Structure de la table `impact`
--

CREATE TABLE `impact` (
  `id_impact` int NOT NULL,
  `code_impact` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_impact` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `impact`
--

INSERT INTO `impact` (`id_impact`, `code_impact`, `libelle_impact`) VALUES
(1, 'SOC', 'Impact social'),
(2, 'ECO', 'Impact économique'),
(3, 'EDU', 'Impact éducatif');

-- --------------------------------------------------------

--
-- Structure de la table `impact_alpha`
--

CREATE TABLE `impact_alpha` (
  `id_centre` int NOT NULL,
  `id_impact` int NOT NULL,
  `id_impact_alpha` int NOT NULL,
  `code_impact_alpha` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `infrastructure`
--

CREATE TABLE `infrastructure` (
  `id_infrastructure` int NOT NULL,
  `code_infrastructure` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_infrastructure` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `infrastructure`
--

INSERT INTO `infrastructure` (`id_infrastructure`, `code_infrastructure`, `libelle_infrastructure`) VALUES
(1, 'SALLE', 'Salle de classe couverte'),
(2, 'LAT', 'Latrines'),
(3, 'FOR', 'Forage / point d’eau');

-- --------------------------------------------------------

--
-- Structure de la table `infrastructure_centre`
--

CREATE TABLE `infrastructure_centre` (
  `id_centre` int NOT NULL,
  `id_infrastructure` int NOT NULL,
  `id_infrastructure_centre` int NOT NULL,
  `libelle_autre_infrastructure` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `langue_apprentissage`
--

CREATE TABLE `langue_apprentissage` (
  `id_centre` int NOT NULL,
  `id_langue` int NOT NULL,
  `libelle_langue` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `langue_apprentissage`
--

INSERT INTO `langue_apprentissage` (`id_centre`, `id_langue`, `libelle_langue`) VALUES
(1, 1, 'Français');

-- --------------------------------------------------------

--
-- Structure de la table `localite_d_implantation`
--

CREATE TABLE `localite_d_implantation` (
  `id_commune` int DEFAULT NULL,
  `id_localite` int NOT NULL,
  `id_milieu_implentation` int NOT NULL,
  `id_sous_prefecture` int NOT NULL,
  `code_localite` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_localite` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `localite_d_implantation`
--

INSERT INTO `localite_d_implantation` (`id_commune`, `id_localite`, `id_milieu_implentation`, `id_sous_prefecture`, `code_localite`, `nom_localite`) VALUES
(1, 1, 1, 1, 'LOC1', 'Cocody Angré');

-- --------------------------------------------------------

--
-- Structure de la table `manuel`
--

CREATE TABLE `manuel` (
  `id_manuel` int NOT NULL,
  `code_manuel` varchar(50) DEFAULT NULL,
  `libelle_manuel` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `manuel`
--

INSERT INTO `manuel` (`id_manuel`, `code_manuel`, `libelle_manuel`) VALUES
(1, 'MAN-T', 'Syllabaire'),
(2, 'MAN-P2', 'Syllabaire P2');

-- --------------------------------------------------------

--
-- Structure de la table `materiels_pedagogique`
--

CREATE TABLE `materiels_pedagogique` (
  `id_materiel_pedagogique` int NOT NULL,
  `code_materiel_pedagogique` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_materiel_pedagogique` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `materiels_pedagogique`
--

INSERT INTO `materiels_pedagogique` (`id_materiel_pedagogique`, `code_materiel_pedagogique`, `libelle_materiel_pedagogique`) VALUES
(1, 'ARD', 'Ardoises'),
(2, 'LIV', 'Livrets pédagogiques'),
(3, 'TAB', 'Tableau mural');

-- --------------------------------------------------------

--
-- Structure de la table `materiel_alpha`
--

CREATE TABLE `materiel_alpha` (
  `id_centre` int NOT NULL,
  `id_materiel_alpha` int NOT NULL,
  `id_materiel_pedagogique` int NOT NULL,
  `libelle_autre_materiel` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `milieu_implantation`
--

CREATE TABLE `milieu_implantation` (
  `id_milieu_implentation` int NOT NULL,
  `code_milieu_implentation` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_type_implentation_` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `milieu_implantation`
--

INSERT INTO `milieu_implantation` (`id_milieu_implentation`, `code_milieu_implentation`, `libelle_type_implentation_`) VALUES
(1, 'URB', 'URBAIN'),
(2, 'RUR', 'RURAL'),
(3, 'ABJ', 'ABIDJAN');

-- --------------------------------------------------------

--
-- Structure de la table `ministere`
--

CREATE TABLE `ministere` (
  `id_promoteur` int NOT NULL,
  `contact` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `code_promoteur` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `boite_postale` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `denomination` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_ministere` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_promoteur` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mail` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_programme` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_representant_legal_structure` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `modealphabetisation`
--

CREATE TABLE `modealphabetisation` (
  `id_centre` int NOT NULL,
  `id_modealpha` int NOT NULL,
  `code_modealpha` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_modealpha` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `naturecentre`
--

CREATE TABLE `naturecentre` (
  `id_naturecentre` int NOT NULL,
  `code_nature_centre` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_nature_centre` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `naturecentre`
--

INSERT INTO `naturecentre` (`id_naturecentre`, `code_nature_centre`, `libelle_nature_centre`) VALUES
(1, 'FIX', 'Centre fixe'),
(2, 'MOB', 'Centre mobile'),
(3, 'REL', 'Relais communautaire');

-- --------------------------------------------------------

--
-- Structure de la table `nature_document`
--

CREATE TABLE `nature_document` (
  `id_nature_document` int NOT NULL,
  `libelle_nature_document_` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `nature_document`
--

INSERT INTO `nature_document` (`id_nature_document`, `libelle_nature_document_`) VALUES
(1, 'Document administratif'),
(2, 'Document pédagogique'),
(3, 'Document financier');

-- --------------------------------------------------------

--
-- Structure de la table `niveau_alpha`
--

CREATE TABLE `niveau_alpha` (
  `id_centre` int NOT NULL,
  `id_niveau_alpha` int NOT NULL,
  `code_niveau_alpha` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_niveau_alpha` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `niveau_alpha`
--

INSERT INTO `niveau_alpha` (`id_centre`, `id_niveau_alpha`, `code_niveau_alpha`, `libelle_niveau_alpha`) VALUES
(7, 1, 'NAL0000001', 'Niveau 1'),
(7, 2, 'NAL0000002', 'Niveau 2'),
(7, 3, 'NAL0000003', 'Niveau 3'),
(7, 6, 'NAL0000006', 'Post alpha'),

-- --------------------------------------------------------

--
-- Structure de la table `niveau_controle`
--

CREATE TABLE `niveau_controle` (
  `id_niveau_controle` int NOT NULL,
  `code_niveau_controle` varchar(50) DEFAULT NULL,
  `libelle_niveau_controle` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `niveau_controle`
--

INSERT INTO `niveau_controle` (`id_niveau_controle`, `code_niveau_controle`, `libelle_niveau_controle`) VALUES
(1, 'NC-T', 'N1'),
(2, 'NC-P2', 'N1 P2');

-- --------------------------------------------------------

--
-- Structure de la table `niveau_cp`
--

CREATE TABLE `niveau_cp` (
  `id_niveau_cp` int NOT NULL,
  `libelle_niveau_cp` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `niveau_cp`
--

INSERT INTO `niveau_cp` (`id_niveau_cp`, `libelle_niveau_cp`) VALUES
(1, 'CP1'),
(2, 'CP2'),
(3, 'CE1'),
(4, 'CE2'),
(5, 'CM1'),
(6, 'CM2'),
(7, 'CPU'),
(8, 'CEU'),
(9, 'CMU');

-- --------------------------------------------------------

--
-- Structure de la table `niveau_evaluation`
--

CREATE TABLE `niveau_evaluation` (
  `id_niveau_evaluation` int NOT NULL,
  `code_niveau_evaluation` varchar(50) DEFAULT NULL,
  `libelle_niveau_evaluation` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `niveau_evaluation`
--

INSERT INTO `niveau_evaluation` (`id_niveau_evaluation`, `code_niveau_evaluation`, `libelle_niveau_evaluation`) VALUES
(1, 'NE-T', 'N1'),
(2, 'NE-P2', 'N1 P2');

-- --------------------------------------------------------

--
-- Structure de la table `niveau_personnel`
--

CREATE TABLE `niveau_personnel` (
  `id_niveau_personnel` int NOT NULL,
  `code_niveau_personnel` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_niveau_personnel` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `niveau_personnel`
--

INSERT INTO `niveau_personnel` (`id_niveau_personnel`, `code_niveau_personnel`, `libelle_niveau_personnel`) VALUES
(1, 'N1', 'Niveau 1'),
(2, 'N2', 'Niveau 2');

-- --------------------------------------------------------

--
-- Structure de la table `niveau_sie_cec`
--

CREATE TABLE `niveau_sie_cec` (
  `id_niveau_sie` int NOT NULL,
  `code_niveau_sie` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_niveau_sie` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `niveau_sie_cec`
--

INSERT INTO `niveau_sie_cec` (`id_niveau_sie`, `code_niveau_sie`, `libelle_niveau_sie`) VALUES
(4, 'NSC0000001', 'Niveau 4'),
(5, 'NSC0000002', 'Niveau 5'),
(6, 'NSC0000003', 'PS'),
(7, 'NSC0000004', 'MS'),
(8, 'NSC0000005', 'GS'),
(9, 'NSC0000006', 'PRE_PRIMAIRE'),
(10, 'NSC0000007', 'CP1'),
(11, 'NSC0000008', 'CP2'),
(12, 'NSC0000009', 'CE1'),
(13, 'NSC0000010', 'CE2'),
(14, 'NSC0000011', 'CM1'),
(15, 'NSC0000012', 'CM2');

-- --------------------------------------------------------

--
-- Structure de la table `ong`
--

CREATE TABLE `ong` (
  `id_promoteur` int NOT NULL,
  `contact` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `code_promoteur` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `boite_postale` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `denomination` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_ong` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_promoteur` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mail` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_programme` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_representant_legal_structure` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `partenaire`
--

CREATE TABLE `partenaire` (
  `id_partenaire` int NOT NULL,
  `code_partenaire` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_partenaire` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `partenaire`
--

INSERT INTO `partenaire` (`id_partenaire`, `code_partenaire`, `libelle_partenaire`) VALUES
(1, 'ONG-01', 'ONG Éducation pour tous'),
(2, 'PTF-02', 'Partenaire technique financier'),
(3, 'COM-03', 'Collectivité locale');

-- --------------------------------------------------------

--
-- Structure de la table `particulier`
--

CREATE TABLE `particulier` (
  `id_promoteur` int NOT NULL,
  `contact` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `code_promoteur` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `boite_postale` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `denomination` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_particulier` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_promoteur` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mail` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_programme` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_representant_legal_structure` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `performance`
--

CREATE TABLE `performance` (
  `id_performance` int NOT NULL,
  `taux_frequentation_par_mois` varchar(255) DEFAULT NULL,
  `taux_progression_apprentissage_calcul` varchar(255) DEFAULT NULL,
  `taux_progression_apprentissage_cvc` varchar(255) DEFAULT NULL,
  `taux_progression_apprentissage_ecriture` varchar(255) DEFAULT NULL,
  `taux_progression_apprentissage_lecture` varchar(255) DEFAULT NULL,
  `id_alpha` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `performance`
--

INSERT INTO `performance` (`id_performance`, `taux_frequentation_par_mois`, `taux_progression_apprentissage_calcul`, `taux_progression_apprentissage_cvc`, `taux_progression_apprentissage_ecriture`, `taux_progression_apprentissage_lecture`, `id_alpha`) VALUES
(2, '85', '60', '75', '65', '70', 7);

-- --------------------------------------------------------

--
-- Structure de la table `periode_activite`
--

CREATE TABLE `periode_activite` (
  `id_periode_activite` int NOT NULL,
  `code_periode_activite` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_periode_activite` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `periode_activite`
--

INSERT INTO `periode_activite` (`id_periode_activite`, `code_periode_activite`, `libelle_periode_activite`) VALUES
(1, 'T1', 'Trimestre 1'),
(2, 'T2', 'Trimestre 2'),
(3, 'AN', 'Annuel');

-- --------------------------------------------------------

--
-- Structure de la table `periode_evaluation`
--

CREATE TABLE `periode_evaluation` (
  `id_periode_evaluation` int NOT NULL,
  `code_periode_evaluation` varchar(50) DEFAULT NULL,
  `libelle_periode_evaluation` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `periode_evaluation`
--

INSERT INTO `periode_evaluation` (`id_periode_evaluation`, `code_periode_evaluation`, `libelle_periode_evaluation`) VALUES
(1, 'PE-T', 'T1'),
(2, 'PE-P2', 'T1 P2');

-- --------------------------------------------------------

--
-- Structure de la table `periodicite`
--

CREATE TABLE `periodicite` (
  `id_periodicite` int NOT NULL,
  `code_periodicite` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_periodicite` varchar(15) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `periodicite`
--

INSERT INTO `periodicite` (`id_periodicite`, `code_periodicite`, `libelle_periodicite`) VALUES
(1, 'HEB', 'Hebdomadaire'),
(2, 'MEN', 'Mensuelle'),
(3, 'ANN', 'Annuelle');

-- --------------------------------------------------------

--
-- Structure de la table `permission`
--

CREATE TABLE `permission` (
  `id_permission` int NOT NULL,
  `code_permission` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `libelle_permission` varchar(100) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `permission`
--

INSERT INTO `permission` (`id_permission`, `code_permission`, `libelle_permission`) VALUES
(1, 'CREER', 'Créer'),
(2, 'LIRE', 'Lire'),
(3, 'MODIFIER', 'Modifier'),
(4, 'SUPPRIMER', 'Supprimer'),
(5, 'VALIDER', 'Valider'),
(6, 'EXPORTER', 'Exporter');

-- --------------------------------------------------------

--
-- Structure de la table `personnel`
--

CREATE TABLE `personnel` (
  `ancienne_fonct_promo_pesonnel` int DEFAULT NULL,
  `anne_expe_personnel` int DEFAULT NULL,
  `certifier_personnel` bit(1) DEFAULT NULL,
  `date_naissance_` date DEFAULT NULL,
  `id_centre` int NOT NULL,
  `id_civilite` int NOT NULL,
  `id_fonction` int NOT NULL,
  `id_niveau_personnel` int NOT NULL,
  `id_personnel` int NOT NULL,
  `id_statut_personnel` int NOT NULL,
  `id_structure_formation_certification` int DEFAULT NULL,
  `sexe_personnel` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `boite_postale_personnel` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `contact_personnel` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_du_prgramme` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `code_personnel` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_personnel` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_representant_legal_sturcture` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `denomination_personnel_` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email_personnel` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `prenoms_personnel` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `personnel`
--

INSERT INTO `personnel` (`ancienne_fonct_promo_pesonnel`, `anne_expe_personnel`, `certifier_personnel`, `date_naissance_`, `id_centre`, `id_civilite`, `id_fonction`, `id_niveau_personnel`, `id_personnel`, `id_statut_personnel`, `id_structure_formation_certification`, `sexe_personnel`, `boite_postale_personnel`, `contact_personnel`, `nom_du_prgramme`, `code_personnel`, `nom_personnel`, `nom_representant_legal_sturcture`, `denomination_personnel_`, `email_personnel`, `prenoms_personnel`) VALUES
(NULL, 3, b'0', NULL, 1, 1, 1, 1, 1, 1, NULL, 'F', NULL, '0101010101', NULL, NULL, 'KOUADIO', NULL, NULL, 'aminata.kouadio@prism.local', 'Aminata'),
(NULL, 5, b'1', NULL, 1, 1, 1, 1, 2, 1, NULL, 'M', NULL, '0202020202', NULL, NULL, 'TRAORE', NULL, NULL, 'moussa.traore@prism.local', 'Moussa');

-- --------------------------------------------------------

--
-- Structure de la table `personnemorale`
--

CREATE TABLE `personnemorale` (
  `id_promoteur` int NOT NULL,
  `contact` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `code_promoteur` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `boite_postale` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `denomination` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_promoteur` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mail` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_programme` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_representant_legal_structure` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_type_personne_morale` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `personnemorale`
--

INSERT INTO `personnemorale` (`id_promoteur`, `contact`, `code_promoteur`, `boite_postale`, `denomination`, `libelle_promoteur`, `mail`, `nom_programme`, `nom_representant_legal_structure`, `id_type_personne_morale`) VALUES
(5, '0700000002', 'CEN-CEC-FULL-MOR', '01 BP 100', 'ONG CEC Full', 'Promoteur CEC Full', 'cec full@ong.test', 'Programme CEC Full', 'Mme Coulibaly', 1),
(6, '0700000002', 'PROM-ALPHA-MOR', '01 BP 100', 'ONG Alpha', 'Promoteur Alpha Morale', 'alpha@ong.test', 'Programme Alpha', 'Mme Coulibaly', 1),
(7, '0700000003', 'PRO0000004', '01 BP 101', 'COMMUNAUTE Alpha', NULL, 'alpha2@ong.test', 'Programme Alpha', 'Mme Ladji', 6);

-- --------------------------------------------------------

--
-- Structure de la table `personnephysique`
--

CREATE TABLE `personnephysique` (
  `id_promoteur` int NOT NULL,
  `code_promoteur` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_personne_physique` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_promoteur` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `contact` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fonction` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `prenom` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `programme`
--

CREATE TABLE `programme` (
  `id_programme` int NOT NULL,
  `code_programme` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `programme_alpha`
--

CREATE TABLE `programme_alpha` (
  `id_centre` int NOT NULL,
  `id_programme` int NOT NULL,
  `id_programme_alpha` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `promoteur`
--

CREATE TABLE `promoteur` (
  `id_promoteur` int NOT NULL,
  `code_promoteur` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_promoteur` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type_promoteur` enum('MORALE','PHYSIQUE') COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `promoteur`
--

INSERT INTO `promoteur` (`id_promoteur`, `code_promoteur`, `libelle_promoteur`, `type_promoteur`) VALUES
(1, 'PROMO1', 'Promoteur Démo', NULL),
(2, 'PRO0000001', 'keke', NULL),
(3, 'PRO0000002', 'lib', NULL),
(4, 'PRO0000003', 'dcvf', NULL),
(5, 'CEN-CEC-FULL-MOR', 'Promoteur CEC Full', 'MORALE'),
(6, 'PROM-ALPHA-MOR', 'Promoteur Alpha Morale', 'MORALE'),
(7, 'PRO0000004', NULL, 'MORALE');

-- --------------------------------------------------------

--
-- Structure de la table `ptf`
--

CREATE TABLE `ptf` (
  `id_promoteur` int NOT NULL,
  `contact` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `code_promoteur` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `boite_postale` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `denomination` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_promoteur` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_ptf` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mail` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_programme` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_representant_legal_structure` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `regimealphabetisation`
--

CREATE TABLE `regimealphabetisation` (
  `id_regime_alpha` int NOT NULL,
  `libelle_regime_alpha` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `regimealphabetisation`
--

INSERT INTO `regimealphabetisation` (`id_regime_alpha`, `libelle_regime_alpha`) VALUES
(1, 'Régime accéléré'),
(2, 'Régime classique'),
(3, 'Régime modulaire');

-- --------------------------------------------------------

--
-- Structure de la table `ressource_financiere_materiel`
--

CREATE TABLE `ressource_financiere_materiel` (
  `id_centre` int NOT NULL,
  `id_designation` int NOT NULL,
  `id_ressource_financiere` int NOT NULL,
  `montant` decimal(12,0) DEFAULT NULL,
  `source_financement` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `role_fonctionnalite_permission`
--

CREATE TABLE `role_fonctionnalite_permission` (
  `id_fonctionnalite` int NOT NULL,
  `id_permission` int NOT NULL,
  `id_role` int NOT NULL,
  `id_role_fonctionnalite_permission` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `role_fonctionnalite_permission`
--

INSERT INTO `role_fonctionnalite_permission` (`id_fonctionnalite`, `id_permission`, `id_role`, `id_role_fonctionnalite_permission`) VALUES
(1, 1, 1, 1),
(1, 2, 1, 2),
(1, 3, 1, 3),
(1, 4, 1, 4),
(2, 1, 1, 5),
(2, 2, 1, 6),
(2, 3, 1, 7),
(2, 4, 1, 8),
(3, 1, 1, 9),
(3, 2, 1, 10),
(3, 3, 1, 11),
(3, 4, 1, 12),
(4, 1, 1, 13),
(4, 2, 1, 14),
(4, 3, 1, 15),
(4, 4, 1, 16),
(5, 1, 1, 17),
(5, 2, 1, 18),
(5, 3, 1, 19),
(5, 4, 1, 20),
(1, 2, 2, 21),
(2, 2, 2, 22),
(3, 2, 2, 23),
(4, 2, 2, 24),
(1, 1, 3, 25),
(1, 2, 3, 26),
(1, 3, 3, 27),
(1, 4, 3, 28),
(2, 1, 3, 29),
(2, 2, 3, 30),
(2, 3, 3, 31),
(2, 4, 3, 32),
(3, 1, 3, 33),
(3, 2, 3, 34),
(3, 3, 3, 35),
(3, 4, 3, 36),
(4, 1, 3, 37),
(4, 2, 3, 38),
(4, 3, 3, 39),
(4, 4, 3, 40),
(5, 1, 3, 41),
(5, 2, 3, 42),
(5, 3, 3, 43),
(5, 4, 3, 44),
(6, 1, 3, 45),
(6, 2, 3, 46),
(6, 3, 3, 47),
(6, 4, 3, 48),
(7, 1, 3, 49),
(7, 2, 3, 50),
(7, 3, 3, 51),
(7, 4, 3, 52),
(8, 1, 3, 53),
(8, 2, 3, 54),
(8, 3, 3, 55),
(8, 4, 3, 56),
(9, 1, 3, 57),
(9, 2, 3, 58),
(9, 3, 3, 59),
(9, 4, 3, 60),
(10, 1, 3, 61),
(10, 2, 3, 62),
(10, 3, 3, 63),
(10, 4, 3, 64),
(11, 1, 3, 65),
(11, 2, 3, 66),
(11, 3, 3, 67),
(11, 4, 3, 68),
(12, 1, 3, 69),
(12, 2, 3, 70),
(12, 3, 3, 71),
(12, 4, 3, 72),
(13, 1, 3, 73),
(13, 2, 3, 74),
(13, 3, 3, 75),
(13, 4, 3, 76),
(14, 1, 3, 77),
(14, 2, 3, 78),
(14, 3, 3, 79),
(14, 4, 3, 80),
(15, 1, 3, 81),
(15, 2, 3, 82),
(15, 3, 3, 83),
(15, 4, 3, 84),
(16, 1, 3, 85),
(16, 2, 3, 86),
(16, 3, 3, 87),
(16, 4, 3, 88),
(17, 1, 3, 89),
(17, 2, 3, 90),
(17, 3, 3, 91),
(17, 4, 3, 92),
(18, 1, 3, 93),
(18, 2, 3, 94),
(18, 3, 3, 95),
(18, 4, 3, 96),
(19, 1, 3, 97),
(19, 2, 3, 98),
(19, 3, 3, 99),
(19, 4, 3, 100),
(20, 1, 3, 101),
(20, 2, 3, 102),
(20, 3, 3, 103),
(20, 4, 3, 104),
(21, 1, 3, 105),
(21, 2, 3, 106),
(21, 3, 3, 107),
(21, 4, 3, 108),
(22, 1, 3, 109),
(22, 2, 3, 110),
(22, 3, 3, 111),
(22, 4, 3, 112),
(23, 1, 3, 113),
(23, 2, 3, 114),
(23, 3, 3, 115),
(23, 4, 3, 116),
(24, 1, 3, 117),
(24, 2, 3, 118),
(24, 3, 3, 119),
(24, 4, 3, 120),
(25, 1, 3, 121),
(25, 2, 3, 122),
(25, 3, 3, 123),
(25, 4, 3, 124),
(26, 1, 3, 125),
(26, 2, 3, 126),
(26, 3, 3, 127),
(26, 4, 3, 128),
(27, 1, 3, 129),
(27, 2, 3, 130),
(27, 3, 3, 131),
(27, 4, 3, 132),
(28, 1, 3, 133),
(28, 2, 3, 134),
(28, 3, 3, 135),
(28, 4, 3, 136),
(29, 1, 3, 137),
(29, 2, 3, 138),
(29, 3, 3, 139),
(29, 4, 3, 140),
(30, 1, 3, 141),
(30, 2, 3, 142),
(30, 3, 3, 143),
(30, 4, 3, 144),
(31, 1, 3, 145),
(31, 2, 3, 146),
(31, 3, 3, 147),
(31, 4, 3, 148),
(32, 1, 3, 149),
(32, 2, 3, 150),
(32, 3, 3, 151),
(32, 4, 3, 152),
(33, 1, 3, 153),
(33, 2, 3, 154),
(33, 3, 3, 155),
(33, 4, 3, 156),
(34, 1, 3, 157),
(34, 2, 3, 158),
(34, 3, 3, 159),
(34, 4, 3, 160),
(35, 1, 3, 161),
(35, 2, 3, 162),
(35, 3, 3, 163),
(35, 4, 3, 164),
(36, 1, 3, 165),
(36, 2, 3, 166),
(36, 3, 3, 167),
(36, 4, 3, 168),
(37, 1, 3, 169),
(37, 2, 3, 170),
(37, 3, 3, 171),
(37, 4, 3, 172),
(38, 1, 3, 173),
(38, 2, 3, 174),
(38, 3, 3, 175),
(38, 4, 3, 176),
(39, 1, 3, 177),
(39, 2, 3, 178),
(39, 3, 3, 179),
(39, 4, 3, 180),
(40, 1, 3, 181),
(40, 2, 3, 182),
(40, 3, 3, 183),
(40, 4, 3, 184),
(41, 1, 3, 185),
(41, 2, 3, 186),
(41, 3, 3, 187),
(41, 4, 3, 188),
(42, 1, 3, 189),
(42, 2, 3, 190),
(42, 3, 3, 191),
(42, 4, 3, 192),
(43, 1, 3, 193),
(43, 2, 3, 194),
(43, 3, 3, 195),
(43, 4, 3, 196),
(44, 1, 3, 197),
(44, 2, 3, 198),
(44, 3, 3, 199),
(44, 4, 3, 200),
(45, 1, 3, 201),
(45, 2, 3, 202),
(45, 3, 3, 203),
(45, 4, 3, 204),
(46, 1, 3, 205),
(46, 2, 3, 206),
(46, 3, 3, 207),
(46, 4, 3, 208),
(47, 1, 3, 209),
(47, 2, 3, 210),
(47, 3, 3, 211),
(47, 4, 3, 212),
(48, 1, 3, 213),
(48, 2, 3, 214),
(48, 3, 3, 215),
(48, 4, 3, 216),
(49, 1, 3, 217),
(49, 2, 3, 218),
(49, 3, 3, 219),
(49, 4, 3, 220),
(50, 1, 3, 221),
(50, 2, 3, 222),
(50, 3, 3, 223),
(50, 4, 3, 224),
(51, 1, 3, 225),
(51, 2, 3, 226),
(51, 3, 3, 227),
(51, 4, 3, 228),
(52, 1, 3, 229),
(52, 2, 3, 230),
(52, 3, 3, 231),
(52, 4, 3, 232),
(53, 1, 3, 233),
(53, 2, 3, 234),
(53, 3, 3, 235),
(53, 4, 3, 236),
(54, 1, 3, 237),
(54, 2, 3, 238),
(54, 3, 3, 239),
(54, 4, 3, 240),
(55, 1, 3, 241),
(55, 2, 3, 242),
(55, 3, 3, 243),
(55, 4, 3, 244),
(56, 1, 3, 245),
(56, 2, 3, 246),
(56, 3, 3, 247),
(56, 4, 3, 248),
(57, 1, 3, 249),
(57, 2, 3, 250),
(57, 3, 3, 251),
(57, 4, 3, 252),
(58, 1, 3, 253),
(58, 2, 3, 254),
(58, 3, 3, 255),
(58, 4, 3, 256),
(59, 1, 3, 257),
(59, 2, 3, 258),
(59, 3, 3, 259),
(59, 4, 3, 260),
(60, 1, 3, 261),
(60, 2, 3, 262),
(60, 3, 3, 263),
(60, 4, 3, 264),
(61, 1, 3, 265),
(61, 2, 3, 266),
(61, 3, 3, 267),
(61, 4, 3, 268),
(62, 1, 3, 269),
(62, 2, 3, 270),
(62, 3, 3, 271),
(62, 4, 3, 272),
(63, 1, 3, 273),
(63, 2, 3, 274),
(63, 3, 3, 275),
(63, 4, 3, 276),
(64, 1, 3, 277),
(64, 2, 3, 278),
(64, 3, 3, 279),
(64, 4, 3, 280),
(65, 1, 3, 281),
(65, 2, 3, 282),
(65, 3, 3, 283),
(65, 4, 3, 284),
(66, 1, 3, 285),
(66, 2, 3, 286),
(66, 3, 3, 287),
(66, 4, 3, 288),
(67, 1, 3, 289),
(67, 2, 3, 290),
(67, 3, 3, 291),
(67, 4, 3, 292),
(68, 1, 3, 293),
(68, 2, 3, 294),
(68, 3, 3, 295),
(68, 4, 3, 296),
(69, 1, 3, 297),
(69, 2, 3, 298),
(69, 3, 3, 299),
(69, 4, 3, 300),
(70, 1, 3, 301),
(70, 2, 3, 302),
(70, 3, 3, 303),
(70, 4, 3, 304),
(71, 1, 3, 305),
(71, 2, 3, 306),
(71, 3, 3, 307),
(71, 4, 3, 308),
(72, 1, 3, 309),
(72, 2, 3, 310),
(72, 3, 3, 311),
(72, 4, 3, 312),
(73, 1, 3, 313),
(73, 2, 3, 314),
(73, 3, 3, 315),
(73, 4, 3, 316),
(74, 1, 3, 317),
(74, 2, 3, 318),
(74, 3, 3, 319),
(74, 4, 3, 320),
(75, 1, 3, 321),
(75, 2, 3, 322),
(75, 3, 3, 323),
(75, 4, 3, 324),
(76, 1, 3, 325),
(76, 2, 3, 326),
(76, 3, 3, 327),
(76, 4, 3, 328),
(77, 1, 3, 329),
(77, 2, 3, 330),
(77, 3, 3, 331),
(77, 4, 3, 332),
(78, 1, 3, 333),
(78, 2, 3, 334),
(78, 3, 3, 335),
(78, 4, 3, 336),
(79, 1, 3, 337),
(79, 2, 3, 338),
(79, 3, 3, 339),
(79, 4, 3, 340),
(80, 1, 3, 341),
(80, 2, 3, 342),
(80, 3, 3, 343),
(80, 4, 3, 344),
(81, 1, 3, 345),
(81, 2, 3, 346),
(81, 3, 3, 347),
(81, 4, 3, 348),
(82, 1, 3, 349),
(82, 2, 3, 350),
(82, 3, 3, 351),
(82, 4, 3, 352),
(83, 1, 3, 353),
(83, 2, 3, 354),
(83, 3, 3, 355),
(83, 4, 3, 356),
(84, 1, 3, 357),
(84, 2, 3, 358),
(84, 3, 3, 359),
(84, 4, 3, 360),
(85, 1, 3, 361),
(85, 2, 3, 362),
(85, 3, 3, 363),
(85, 4, 3, 364),
(86, 1, 3, 365),
(86, 2, 3, 366),
(86, 3, 3, 367),
(86, 4, 3, 368),
(87, 1, 3, 369),
(87, 2, 3, 370),
(87, 3, 3, 371),
(87, 4, 3, 372),
(88, 1, 3, 373),
(88, 2, 3, 374),
(88, 3, 3, 375),
(88, 4, 3, 376),
(89, 1, 3, 377),
(89, 2, 3, 378),
(89, 3, 3, 379),
(89, 4, 3, 380),
(90, 1, 3, 381),
(90, 2, 3, 382),
(90, 3, 3, 383),
(90, 4, 3, 384),
(91, 1, 3, 385),
(91, 2, 3, 386),
(91, 3, 3, 387),
(91, 4, 3, 388),
(92, 1, 3, 389),
(92, 2, 3, 390),
(92, 3, 3, 391),
(92, 4, 3, 392),
(93, 1, 3, 393),
(93, 2, 3, 394),
(93, 3, 3, 395),
(93, 4, 3, 396),
(94, 1, 3, 397),
(94, 2, 3, 398),
(94, 3, 3, 399),
(94, 4, 3, 400),
(95, 1, 3, 401),
(95, 2, 3, 402),
(95, 3, 3, 403),
(95, 4, 3, 404),
(96, 1, 3, 405),
(96, 2, 3, 406),
(96, 3, 3, 407),
(96, 4, 3, 408),
(97, 1, 3, 409),
(97, 2, 3, 410),
(97, 3, 3, 411),
(97, 4, 3, 412),
(98, 1, 3, 413),
(98, 2, 3, 414),
(98, 3, 3, 415),
(98, 4, 3, 416),
(99, 1, 3, 417),
(99, 2, 3, 418),
(99, 3, 3, 419),
(99, 4, 3, 420),
(1, 5, 1, 421),
(1, 6, 1, 422),
(2, 5, 1, 423),
(2, 6, 1, 424),
(3, 5, 1, 425),
(3, 6, 1, 426),
(4, 5, 1, 427),
(4, 6, 1, 428),
(5, 5, 1, 429),
(5, 6, 1, 430);

-- --------------------------------------------------------

--
-- Structure de la table `sie`
--

CREATE TABLE `sie` (
  `a_de_leau` bit(1) DEFAULT NULL,
  `autorisation` bit(1) DEFAULT NULL,
  `encadrer_par_mena` bit(1) DEFAULT NULL,
  `est_electrifie` bit(1) DEFAULT NULL,
  `id_autorite_autorisation` int DEFAULT NULL,
  `id_centre` int NOT NULL,
  `id_iep` int DEFAULT NULL,
  `id_localite` int DEFAULT NULL,
  `id_naturecentre` int DEFAULT NULL,
  `id_periodicite` int DEFAULT NULL,
  `id_promoteur` int DEFAULT NULL,
  `nombre_visite` int DEFAULT NULL,
  `code_centre` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_sie` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `localisation_centre` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_milieu_implentation` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `encadreur_non_mena` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `sie`
--

INSERT INTO `sie` (`a_de_leau`, `autorisation`, `encadrer_par_mena`, `est_electrifie`, `id_autorite_autorisation`, `id_centre`, `id_iep`, `id_localite`, `id_naturecentre`, `id_periodicite`, `id_promoteur`, `nombre_visite`, `code_centre`, `libelle_sie`, `localisation_centre`, `nom_milieu_implentation`, `encadreur_non_mena`) VALUES
(NULL, NULL, NULL, NULL, NULL, 6, NULL, NULL, NULL, NULL, NULL, NULL, 'CTR-SIE-01', 'SIE - CTR-SIE-01', 'Centre SIE Démo', 'Urbain', NULL),
(b'1', b'1', b'1', b'1', 1, 9, 1, 1, 1, 1, 4, 3, 'CEN0000003', 'vfbgf', 'fg', 'frgt', 'efrgt');

-- --------------------------------------------------------

--
-- Structure de la table `sie_niveau`
--

CREATE TABLE `sie_niveau` (
  `id_annee_scolaire` int NOT NULL,
  `id_centre` int NOT NULL,
  `id_niveau_sie` int NOT NULL,
  `id_sie_niveau` int NOT NULL,
  `nombre_salle_sie` int DEFAULT NULL,
  `code_sie_niveau` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `societe_civile`
--

CREATE TABLE `societe_civile` (
  `id_promoteur` int NOT NULL,
  `contact` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `code_promoteur` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `boite_postale` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `denomination` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_promoteur` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_societe_civile` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mail` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_programme` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_representant_legal_structure` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `sous_prefecture`
--

CREATE TABLE `sous_prefecture` (
  `id_departement` int NOT NULL,
  `id_sous_prefecture` int NOT NULL,
  `code_sous_prefecture` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom_sous_prefecture` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `sous_prefecture`
--

INSERT INTO `sous_prefecture` (`id_departement`, `id_sous_prefecture`, `code_sous_prefecture`, `nom_sous_prefecture`) VALUES
(1, 1, 'SPABJ', 'Abidjan');

-- --------------------------------------------------------

--
-- Structure de la table `statut_personnel`
--

CREATE TABLE `statut_personnel` (
  `id_statut_personnel` int NOT NULL,
  `code_statut_personnel` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_statut_personnel` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `statut_personnel`
--

INSERT INTO `statut_personnel` (`id_statut_personnel`, `code_statut_personnel`, `libelle_statut_personnel`) VALUES
(1, 'VOL', 'Volontaire'),
(2, 'CONT', 'Contractuel'),
(3, 'FONC', 'Fonctionnaire détaché');

-- --------------------------------------------------------

--
-- Structure de la table `structure_formation_certification`
--

CREATE TABLE `structure_formation_certification` (
  `id_structure_formation_certification` int NOT NULL,
  `code_structure_certification` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_structure_certification` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `support_didactique`
--

CREATE TABLE `support_didactique` (
  `id_support_didactique` int NOT NULL,
  `libelle_support_didactique` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `support_didactique`
--

INSERT INTO `support_didactique` (`id_support_didactique`, `libelle_support_didactique`) VALUES
(1, 'Guide animateur'),
(2, 'Fiches progression'),
(3, 'Évaluation formative');

-- --------------------------------------------------------

--
-- Structure de la table `support_didactique_alpha`
--

CREATE TABLE `support_didactique_alpha` (
  `id_centre` int NOT NULL,
  `id_support_didactique` int NOT NULL,
  `id_support_didactique_alpha` int NOT NULL,
  `libelle_autre_support` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `taux_evaluation`
--

CREATE TABLE `taux_evaluation` (
  `id_taux_evaluation` int NOT NULL,
  `code_taux_evaluation` varchar(50) DEFAULT NULL,
  `libelle_taux_evaluation` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `taux_evaluation`
--

INSERT INTO `taux_evaluation` (`id_taux_evaluation`, `code_taux_evaluation`, `libelle_taux_evaluation`) VALUES
(1, 'TX-T', '80'),
(2, 'TX-P2', '85');

-- --------------------------------------------------------

--
-- Structure de la table `theme_evaluation`
--

CREATE TABLE `theme_evaluation` (
  `id_theme_evaluation` int NOT NULL,
  `code_theme_evaluation` varchar(50) DEFAULT NULL,
  `libelle_theme_evaluation` varchar(200) DEFAULT NULL,
  `niveau` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `theme_evaluation`
--

INSERT INTO `theme_evaluation` (`id_theme_evaluation`, `code_theme_evaluation`, `libelle_theme_evaluation`, `niveau`) VALUES
(1, 'TEV0000001', 'Comprehension', 'NIVEAU_1'),
(2, 'TEV0000002', 'libelle 2', 'NIVEAU_2'),
(3, 'TEV0000003', 'libelle 3', 'POST_ALPHA');

-- --------------------------------------------------------

--
-- Structure de la table `theme_evaluation_niveau1`
--

CREATE TABLE `theme_evaluation_niveau1` (
  `id_theme_evaluation_niveau1` int NOT NULL,
  `code_theme_evaluation_niveau1` varchar(50) DEFAULT NULL,
  `libelle_theme_evaluation_niveau1` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `theme_evaluation_niveau1`
--

INSERT INTO `theme_evaluation_niveau1` (`id_theme_evaluation_niveau1`, `code_theme_evaluation_niveau1`, `libelle_theme_evaluation_niveau1`) VALUES
(1, 'TH1-T', 'Theme1'),
(2, 'TH1-P2', 'Theme1 P2');

-- --------------------------------------------------------

--
-- Structure de la table `theme_evaluation_niveau2_post_alpha`
--

CREATE TABLE `theme_evaluation_niveau2_post_alpha` (
  `id_theme_evaluation_n2_post_alpha` int NOT NULL,
  `code_theme_evaluation_n2_post_alpha` varchar(50) DEFAULT NULL,
  `libelle_theme_evaluation_n2_post_alpha` varchar(120) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `theme_evaluation_niveau2_post_alpha`
--

INSERT INTO `theme_evaluation_niveau2_post_alpha` (`id_theme_evaluation_n2_post_alpha`, `code_theme_evaluation_n2_post_alpha`, `libelle_theme_evaluation_n2_post_alpha`) VALUES
(1, 'TH2-T', 'Theme2'),
(2, 'TH2-P2', 'Theme2 P2');

-- --------------------------------------------------------

--
-- Structure de la table `type_alpha`
--

CREATE TABLE `type_alpha` (
  `id_type_alpha` int NOT NULL,
  `libelle_type_alpha` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `type_alpha`
--

INSERT INTO `type_alpha` (`id_type_alpha`, `libelle_type_alpha`) VALUES
(1, 'Centre communautaire'),
(2, 'Centre intégré'),
(3, 'Poste d’alphabétisation');

-- --------------------------------------------------------

--
-- Structure de la table `type_document`
--

CREATE TABLE `type_document` (
  `id_type_document` int NOT NULL,
  `code_type_document` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_type_document` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `type_document`
--

INSERT INTO `type_document` (`id_type_document`, `code_type_document`, `libelle_type_document`) VALUES
(1, 'PV', 'Procès-verbal'),
(2, 'RAP', 'Rapport d’activité'),
(3, 'CONT', 'Contrat / convention'),
(4, 'TDO0000001', 'rthy');

-- --------------------------------------------------------

--
-- Structure de la table `type_personne_morale`
--

CREATE TABLE `type_personne_morale` (
  `id_type_personne_morale` int NOT NULL,
  `libelle` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `type_personne_morale`
--

INSERT INTO `type_personne_morale` (`id_type_personne_morale`, `libelle`) VALUES
(1, 'PTF'),
(2, 'MINISTERE'),
(3, 'ONG'),
(4, 'SOCIETE CIVILE'),
(5, 'ASSOCIATION'),
(6, 'COMMUNAUTE'),
(7, 'PARTICULIER'),
(8, 'AUTRE'),
(9, 'PRIVE');

-- --------------------------------------------------------

--
-- Structure de la table `user_role`
--

CREATE TABLE `user_role` (
  `id_role` int NOT NULL,
  `id_user` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user_role`
--

INSERT INTO `user_role` (`id_role`, `id_user`) VALUES
(1, 1),
(3, 2),
(3, 3);

-- --------------------------------------------------------

--
-- Structure de la table `visite`
--

CREATE TABLE `visite` (
  `id_visite` int NOT NULL,
  `maitrise_seance_calcul` varchar(255) DEFAULT NULL,
  `maitrise_seance_cvc` varchar(255) DEFAULT NULL,
  `maitrise_seance_ecriture` varchar(255) DEFAULT NULL,
  `maitrise_seance_lecture` varchar(255) DEFAULT NULL,
  `nombre_bulletin_effectue_par_conseiller` int DEFAULT NULL,
  `nombre_reunion_bilan_conseiller_superviseur` int DEFAULT NULL,
  `nombre_reunion_point_activite_alpha` int DEFAULT NULL,
  `nombre_visite_conseiller_superviseur_effectue` int DEFAULT NULL,
  `nombre_visite_effectue_par_iepp` int DEFAULT NULL,
  `nombre_visite_realise_par_conseiller` int DEFAULT NULL,
  `id_alpha` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `alpha`
--
ALTER TABLE `alpha`
  ADD PRIMARY KEY (`id_centre`),
  ADD KEY `FKk21bnk7oaqw3spxujaru89hrm` (`id_categorie_centre_alpha`),
  ADD KEY `FKip3sh04wb1qr6umqtbpfxlod` (`id_compagne`),
  ADD KEY `FKi1d8ygbhpjxquntoj6l64pnxv` (`id_regime_alpha`),
  ADD KEY `FK3k5nywxuk4gpprww0qijwqiit` (`id_type_alpha`);

--
-- Index pour la table `anne_scolaire`
--
ALTER TABLE `anne_scolaire`
  ADD PRIMARY KEY (`id_annee_scolaire`);

--
-- Index pour la table `appui_partenaire`
--
ALTER TABLE `appui_partenaire`
  ADD PRIMARY KEY (`id_appui_partenaire`),
  ADD KEY `FKgrinbg2oejieodm9cduqdjbu` (`id_categorie_appui`),
  ADD KEY `FKjuttgiio5edr0oou4hp61mqdp` (`id_centre`),
  ADD KEY `FKcki4b399611hqdwk43eskc9al` (`id_partenaire`);

--
-- Index pour la table `app_role`
--
ALTER TABLE `app_role`
  ADD PRIMARY KEY (`id_role`),
  ADD UNIQUE KEY `UKdt8o47tx4reeokqus3ccxpadk` (`code_role`);

--
-- Index pour la table `app_user`
--
ALTER TABLE `app_user`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `UK3k4cplvh82srueuttfkwnylq0` (`username`);

--
-- Index pour la table `aspect_a_ameliorer`
--
ALTER TABLE `aspect_a_ameliorer`
  ADD PRIMARY KEY (`id_aspect_a_ameliorer`);

--
-- Index pour la table `autorite_autorisation`
--
ALTER TABLE `autorite_autorisation`
  ADD PRIMARY KEY (`id_autorite_autorisation`);

--
-- Index pour la table `campagne`
--
ALTER TABLE `campagne`
  ADD PRIMARY KEY (`id_compagne`);

--
-- Index pour la table `categorie_appui`
--
ALTER TABLE `categorie_appui`
  ADD PRIMARY KEY (`id_categorie_appui`);

--
-- Index pour la table `categorie_centre_alpha`
--
ALTER TABLE `categorie_centre_alpha`
  ADD PRIMARY KEY (`id_categorie_centre_alpha`);

--
-- Index pour la table `cec`
--
ALTER TABLE `cec`
  ADD PRIMARY KEY (`id_centre`);

--
-- Index pour la table `cec_niveau`
--
ALTER TABLE `cec_niveau`
  ADD PRIMARY KEY (`id_cec_niveau`),
  ADD KEY `FK6gcdm1sdptkb6w099cng6o5n5` (`id_annee_scolaire`),
  ADD KEY `FKpahg441s64a9eqio7ciousov` (`id_centre`),
  ADD KEY `FKs41ybrbdbxk41u1jinrowmkyb` (`id_niveau_sie`);

--
-- Index pour la table `centre`
--
ALTER TABLE `centre`
  ADD PRIMARY KEY (`id_centre`),
  ADD KEY `FKlgeh051xwfpg4uei64caqe68g` (`id_autorite_autorisation`),
  ADD KEY `FKo7uf0dosiawdkql62y61krv4k` (`id_iep`),
  ADD KEY `FKbtly2k8t9vjyrfmihb0d3e3v2` (`id_localite`),
  ADD KEY `FKnn4j6cxs0ptlv4h8pg38my0d2` (`id_naturecentre`),
  ADD KEY `FKfy8n21gn2p94qvlvc16r56p86` (`id_periodicite`),
  ADD KEY `FK4a8bdyicy2idc29p9d2x3dawy` (`id_promoteur`);

--
-- Index pour la table `civilite`
--
ALTER TABLE `civilite`
  ADD PRIMARY KEY (`id_civilite`);

--
-- Index pour la table `code_sequence`
--
ALTER TABLE `code_sequence`
  ADD PRIMARY KEY (`prefix`);

--
-- Index pour la table `communaute`
--
ALTER TABLE `communaute`
  ADD PRIMARY KEY (`id_promoteur`);

--
-- Index pour la table `commune`
--
ALTER TABLE `commune`
  ADD PRIMARY KEY (`id_commune`);

--
-- Index pour la table `competence`
--
ALTER TABLE `competence`
  ADD PRIMARY KEY (`id_competence`);

--
-- Index pour la table `competence_centre`
--
ALTER TABLE `competence_centre`
  ADD PRIMARY KEY (`id_competence_centre`),
  ADD KEY `FKqmieuern0vt8cv26uo0hxaoyd` (`id_centre`),
  ADD KEY `FK7mjj2eof77xgkn1a5mog8jjwn` (`id_competence`);

--
-- Index pour la table `controle`
--
ALTER TABLE `controle`
  ADD PRIMARY KEY (`id_controle`),
  ADD KEY `FKexjgus7qb2j43i237jb4mdfir` (`id_alpha`),
  ADD KEY `FKoqen0imbhs2l6f65xy7je7mad` (`id_discipline`),
  ADD KEY `FKbrkqft2l0wch4bcc7aajjqbfg` (`id_manuel`),
  ADD KEY `FK787wjhuigdw2nj74qk8dx82nw` (`id_niveau_controle`);

--
-- Index pour la table `cp`
--
ALTER TABLE `cp`
  ADD PRIMARY KEY (`id_centre`);

--
-- Index pour la table `cp_niveau`
--
ALTER TABLE `cp_niveau`
  ADD PRIMARY KEY (`id_cp_niveau`),
  ADD KEY `FKnrcjero9q66sxskfvpg5jnmsv` (`id_annee_scolaire`),
  ADD KEY `FKm2g50repm52gotfhc1i7sm9y3` (`id_centre`),
  ADD KEY `FKg4mmwo56m57l2nu1iyl20a9v0` (`id_niveau_cp`);

--
-- Index pour la table `departement`
--
ALTER TABLE `departement`
  ADD PRIMARY KEY (`id_departement`);

--
-- Index pour la table `designation`
--
ALTER TABLE `designation`
  ADD PRIMARY KEY (`id_designation`);

--
-- Index pour la table `difficulte`
--
ALTER TABLE `difficulte`
  ADD PRIMARY KEY (`id_difficulte`);

--
-- Index pour la table `difficulte_alpha`
--
ALTER TABLE `difficulte_alpha`
  ADD PRIMARY KEY (`id_difficulte_alpha`),
  ADD KEY `FKq54rdy7lr4qqwyhd2rqs9hfp9` (`id_centre`),
  ADD KEY `FKsscwl64ryrbtxarry4ha9swia` (`id_difficulte`);

--
-- Index pour la table `diplome`
--
ALTER TABLE `diplome`
  ADD PRIMARY KEY (`id_diplome`);

--
-- Index pour la table `diplome_personnel`
--
ALTER TABLE `diplome_personnel`
  ADD PRIMARY KEY (`id_diplome_personnel`),
  ADD KEY `FK487oep9y1r5958nw0km3n0fw1` (`id_diplome`),
  ADD KEY `FKk1ml29jynyt2l5567la5gk93f` (`id_personnel`);

--
-- Index pour la table `discipline`
--
ALTER TABLE `discipline`
  ADD PRIMARY KEY (`id_discipline`);

--
-- Index pour la table `document`
--
ALTER TABLE `document`
  ADD PRIMARY KEY (`id_document`),
  ADD KEY `FK8bfd998of8d3mledvq68sf0s7` (`id_centre`),
  ADD KEY `FKjmhn0bqec0igvd0ot8r3u39u0` (`id_nature_document`),
  ADD KEY `FK9bmhlm70xd6kmypoglxrun2qj` (`id_type_document`);

--
-- Index pour la table `domaine_activite`
--
ALTER TABLE `domaine_activite`
  ADD PRIMARY KEY (`id_domaine_activite`);

--
-- Index pour la table `domaine_activite_alpha`
--
ALTER TABLE `domaine_activite_alpha`
  ADD PRIMARY KEY (`id_domaine_activite_alpha`),
  ADD KEY `FKjy7bv01he29iy2nxwlgrq324r` (`id_centre`),
  ADD KEY `FK89nlq7yy8whph0527ybynqd59` (`id_domaine_activite`);

--
-- Index pour la table `drena`
--
ALTER TABLE `drena`
  ADD PRIMARY KEY (`id_drena`);

--
-- Index pour la table `drena_departement`
--
ALTER TABLE `drena_departement`
  ADD PRIMARY KEY (`id_drena_depart`),
  ADD KEY `FKamuemov5w4fx8ip5lftfp88jh` (`id_departement`),
  ADD KEY `FKkqf5lsf0xad18t1b4favmepjw` (`id_drena`);

--
-- Index pour la table `effectif_abandon_alpha`
--
ALTER TABLE `effectif_abandon_alpha`
  ADD PRIMARY KEY (`id_effectif_abandon_alpha`),
  ADD KEY `FK4vmf372103udods5c23q4n9tp` (`id_centre`),
  ADD KEY `FKkybynm36jkudwq8geeiqqkyyl` (`id_periode_activite`);

--
-- Index pour la table `effectif_abandon_cec`
--
ALTER TABLE `effectif_abandon_cec`
  ADD PRIMARY KEY (`id_effectif_debut10`),
  ADD KEY `FK1ywbonxpdx7coendpua1dbrl6` (`id_annee_scolaire`),
  ADD KEY `FKkypn5qe50jicyaj56ifkevjv3` (`id_centre`),
  ADD KEY `FKomo1032uoa5t5hk86209imlu7` (`id_niveau_sie`);

--
-- Index pour la table `effectif_abandon_cp`
--
ALTER TABLE `effectif_abandon_cp`
  ADD PRIMARY KEY (`id_effectif_debut15`),
  ADD KEY `FKt9fsxig6nbkimx293nwl9ua23` (`id_annee_scolaire`),
  ADD KEY `FKevltctixx1yd51jnbmmhxxril` (`id_centre`),
  ADD KEY `FKpudnoromtowgoevp81923phxj` (`id_niveau_cp`);

--
-- Index pour la table `effectif_abondan_sie`
--
ALTER TABLE `effectif_abondan_sie`
  ADD PRIMARY KEY (`id_effectif_debut21`),
  ADD KEY `FKq6kcwf66r4f2l381jvc2gdc0t` (`id_annee_scolaire`),
  ADD KEY `FKhu5g8qw96jufd9fv4bejdybmm` (`id_niveau_sie`);

--
-- Index pour la table `effectif_admis_integration_cp`
--
ALTER TABLE `effectif_admis_integration_cp`
  ADD PRIMARY KEY (`id_effectif_debut16`),
  ADD KEY `FKg2slic9676frvjq8sfl5bg7w0` (`id_annee_scolaire`),
  ADD KEY `FKpi67h44534cj2hrlwtsrghg9u` (`id_centre`),
  ADD KEY `FKjg6als64mvdgpc4rx34l4tan2` (`id_niveau_cp`);

--
-- Index pour la table `effectif_alpha`
--
ALTER TABLE `effectif_alpha`
  ADD PRIMARY KEY (`id_effectif_alpha`),
  ADD KEY `FKppwfgq8smgjf6hxo6xuj5bej8` (`id_centre`),
  ADD KEY `FK74f5j85ycklgh9kpmwvlwpj9m` (`id_periode_activite`),
  ADD KEY `FKbt49ubckywul04rvejl580ooy` (`id_niveau_alpha`);

--
-- Index pour la table `effectif_cec`
--
ALTER TABLE `effectif_cec`
  ADD PRIMARY KEY (`id_effectif_cec`),
  ADD KEY `FKaj41h71xtrbmrpao0qsre1i2p` (`id_centre`),
  ADD KEY `FKjkr6l1b695f54qwa6r01khsvr` (`id_niveau_sie`),
  ADD KEY `FKb4044wgbb8yo20rvjdtfmc9ie` (`id_periode_activite`);

--
-- Index pour la table `effectif_cepe_cec`
--
ALTER TABLE `effectif_cepe_cec`
  ADD PRIMARY KEY (`id_effectif_debut12`),
  ADD KEY `FK513bicw2vkutxq5gwpcyhwlkg` (`cec_id_centre`),
  ADD KEY `FK8mgl232dkl26xnu2iuglp6x3k` (`id_annee_scolaire`),
  ADD KEY `FKnola2gquep2eakyq235q75kt1` (`id_centre`);

--
-- Index pour la table `effectif_cepe_cp`
--
ALTER TABLE `effectif_cepe_cp`
  ADD PRIMARY KEY (`id_effectif_debut18`),
  ADD KEY `FKkcug02574vpc0uhgctfj4wah0` (`id_annee_scolaire`),
  ADD KEY `FKqfijvnjwa4773n7i65eloc1ay` (`id_centre`);

--
-- Index pour la table `effectif_cp`
--
ALTER TABLE `effectif_cp`
  ADD PRIMARY KEY (`id_effectif_debut14`),
  ADD KEY `FK8oo80aofr1651t0j6cvcaymeu` (`id_annee_scolaire`),
  ADD KEY `FK57319994d4mlyofk738rdf9tt` (`id_centre`),
  ADD KEY `FKex8t04f3od7idbwynbcukdfx1` (`id_niveau_cp`);

--
-- Index pour la table `effectif_integration_formel_cp`
--
ALTER TABLE `effectif_integration_formel_cp`
  ADD PRIMARY KEY (`id_effectif_debut17`),
  ADD KEY `FK2rgei6kug0uhvhv1u1x2nf27a` (`id_annee_scolaire`),
  ADD KEY `FKa30i9qhjjtlpibhxbxbqy0u39` (`id_centre`),
  ADD KEY `FKpxdudsmf7e8qb25fyyc3wtqpg` (`id_niveau_cp`);

--
-- Index pour la table `effectif_passage_alpha`
--
ALTER TABLE `effectif_passage_alpha`
  ADD PRIMARY KEY (`id_effectif_passage_alpha`),
  ADD KEY `FKbnkhkodv3qkxy7remb6ofruq6` (`id_centre`),
  ADD KEY `FKpn0ok3lvt6idjinwm34i7geo9` (`id_periode_activite`);

--
-- Index pour la table `effectif_promu_cec`
--
ALTER TABLE `effectif_promu_cec`
  ADD PRIMARY KEY (`id_effectif_debut11`),
  ADD KEY `FKe0rs9rceceuo2ll6u21ktpk55` (`id_annee_scolaire`),
  ADD KEY `FKp94rfo6j36qnvujq49mvl1886` (`id_centre`),
  ADD KEY `FKgwoqmrqsygujpjhtku9kyret8` (`id_niveau_sie`);

--
-- Index pour la table `effectif_promu_sie`
--
ALTER TABLE `effectif_promu_sie`
  ADD PRIMARY KEY (`id_effectif_debut22`),
  ADD KEY `FKolq5j9oxd6m6pi9v8gixtsq5u` (`id_annee_scolaire`),
  ADD KEY `FK8et1vvpgqgr4wcfmfyndw0ume` (`id_niveau_sie`);

--
-- Index pour la table `effectif_reverse_formel_sie`
--
ALTER TABLE `effectif_reverse_formel_sie`
  ADD PRIMARY KEY (`id_effectif_debut23`),
  ADD KEY `FK4te9ltjlsn1yufaxs1oieri2j` (`id_annee_scolaire`),
  ADD KEY `FK7mv37mxcycvl16qm5bmjtxro3` (`id_niveau_sie`);

--
-- Index pour la table `effectif_sie`
--
ALTER TABLE `effectif_sie`
  ADD PRIMARY KEY (`id_effectif_debut20`),
  ADD KEY `FKxv5on1h5m62qyuw2iwc97ule` (`id_annee_scolaire`),
  ADD KEY `FKq5erpvjjqi68lvryar7emy9dr` (`id_niveau_sie`);

--
-- Index pour la table `effectif_situation_handicap_alpha`
--
ALTER TABLE `effectif_situation_handicap_alpha`
  ADD PRIMARY KEY (`id_effectif_situation_handicap_alpha`),
  ADD KEY `FKrs5oqk2dei5v3bveig8dhrcfy` (`id_centre`),
  ADD KEY `FK1v0ek2brba87w2n8xtqqylqas` (`id_periode_activite`);

--
-- Index pour la table `effectif_situation_handicap_cec`
--
ALTER TABLE `effectif_situation_handicap_cec`
  ADD PRIMARY KEY (`id_effectif_debut13`),
  ADD KEY `FKfjhqjjtkdto9h4a2qq74ewdjb` (`id_annee_scolaire`),
  ADD KEY `FK3jqugqdvkk1w424xuycr31oer` (`id_niveau_sie`);

--
-- Index pour la table `effectif_situation_handicap_cp`
--
ALTER TABLE `effectif_situation_handicap_cp`
  ADD PRIMARY KEY (`id_effectif_debut19`),
  ADD KEY `FKrovyjl7ecsi1nlpgcgomf3wh6` (`id_annee_scolaire`),
  ADD KEY `FKa2ct88gg0vdsnmsaqh7cd1apy` (`id_centre`),
  ADD KEY `FKav71jft4xd4xaj50ewpqovxcq` (`id_niveau_cp`);

--
-- Index pour la table `effectif_situation_handicap_sie`
--
ALTER TABLE `effectif_situation_handicap_sie`
  ADD PRIMARY KEY (`id_effectif_debut24`),
  ADD KEY `FK4p0qdpiq55t640csflpi1asbs` (`id_annee_scolaire`),
  ADD KEY `FKmq1msy8oq7dmv0bn0i6sdqnva` (`id_niveau_sie`);

--
-- Index pour la table `evaluation`
--
ALTER TABLE `evaluation`
  ADD PRIMARY KEY (`id_evaluation`),
  ADD KEY `FKgvnyiqmggrg6f7jeixtl8sp34` (`id_alpha`),
  ADD KEY `FK31w8jsk6oergpxrqjhgeueli0` (`id_niveau_evaluation`),
  ADD KEY `FKi39puo7761exdvti27kgg74d8` (`id_periode_evaluation`),
  ADD KEY `FKk794f33mpcx0cg8vmhosdr61f` (`id_taux_evaluation`),
  ADD KEY `FK7sgrj7cjx1axd4l3f0axa3lb3` (`id_theme_evaluation`);

--
-- Index pour la table `fonction`
--
ALTER TABLE `fonction`
  ADD PRIMARY KEY (`id_fonction`);

--
-- Index pour la table `fonctionnalite`
--
ALTER TABLE `fonctionnalite`
  ADD PRIMARY KEY (`id_fonctionnalite`),
  ADD UNIQUE KEY `UK8212ls1hchab1722lc6u249vh` (`code_fonctionnalite`);

--
-- Index pour la table `iep`
--
ALTER TABLE `iep`
  ADD PRIMARY KEY (`id_iep`),
  ADD KEY `FKj4xtn0okb9b4u9an42o6lh3b4` (`id_drena`);

--
-- Index pour la table `impact`
--
ALTER TABLE `impact`
  ADD PRIMARY KEY (`id_impact`);

--
-- Index pour la table `impact_alpha`
--
ALTER TABLE `impact_alpha`
  ADD PRIMARY KEY (`id_impact_alpha`),
  ADD KEY `FKhrcnt1ij04ht3l8h6vl10jfqq` (`id_centre`),
  ADD KEY `FKhx7n8lbq1b3hweji91icugqgy` (`id_impact`);

--
-- Index pour la table `infrastructure`
--
ALTER TABLE `infrastructure`
  ADD PRIMARY KEY (`id_infrastructure`);

--
-- Index pour la table `infrastructure_centre`
--
ALTER TABLE `infrastructure_centre`
  ADD PRIMARY KEY (`id_infrastructure_centre`),
  ADD KEY `FKojf3dey8tf7xobjt6u0g9ctem` (`id_centre`),
  ADD KEY `FKhblvm83o70b60qm4g4ttfx37x` (`id_infrastructure`);

--
-- Index pour la table `langue_apprentissage`
--
ALTER TABLE `langue_apprentissage`
  ADD PRIMARY KEY (`id_langue`),
  ADD KEY `FKo42lbljnwwwbbgb9v6dkhul55` (`id_centre`);

--
-- Index pour la table `localite_d_implantation`
--
ALTER TABLE `localite_d_implantation`
  ADD PRIMARY KEY (`id_localite`),
  ADD KEY `FK61o0qysycv8sfbalbc9k2kkb` (`id_commune`),
  ADD KEY `FKkc0qo06dx86pp769w39mqed32` (`id_milieu_implentation`),
  ADD KEY `FKm4fc2kwwx1fd8p0x6nqrw7waf` (`id_sous_prefecture`);

--
-- Index pour la table `manuel`
--
ALTER TABLE `manuel`
  ADD PRIMARY KEY (`id_manuel`);

--
-- Index pour la table `materiels_pedagogique`
--
ALTER TABLE `materiels_pedagogique`
  ADD PRIMARY KEY (`id_materiel_pedagogique`);

--
-- Index pour la table `materiel_alpha`
--
ALTER TABLE `materiel_alpha`
  ADD PRIMARY KEY (`id_materiel_alpha`),
  ADD KEY `FK53ynq5wpoluxprnhxim8otqwq` (`id_centre`),
  ADD KEY `FKcjlxgusf4jc2or0umq7i7yhar` (`id_materiel_pedagogique`);

--
-- Index pour la table `milieu_implantation`
--
ALTER TABLE `milieu_implantation`
  ADD PRIMARY KEY (`id_milieu_implentation`);

--
-- Index pour la table `ministere`
--
ALTER TABLE `ministere`
  ADD PRIMARY KEY (`id_promoteur`);

--
-- Index pour la table `modealphabetisation`
--
ALTER TABLE `modealphabetisation`
  ADD PRIMARY KEY (`id_modealpha`),
  ADD KEY `FKlmqatqx8hytg3y346jvcjml0g` (`id_centre`);

--
-- Index pour la table `naturecentre`
--
ALTER TABLE `naturecentre`
  ADD PRIMARY KEY (`id_naturecentre`);

--
-- Index pour la table `nature_document`
--
ALTER TABLE `nature_document`
  ADD PRIMARY KEY (`id_nature_document`);

--
-- Index pour la table `niveau_alpha`
--
ALTER TABLE `niveau_alpha`
  ADD PRIMARY KEY (`id_niveau_alpha`),
  ADD KEY `FKog45opu9svjrbu60slhq82uuv` (`id_centre`);

--
-- Index pour la table `niveau_controle`
--
ALTER TABLE `niveau_controle`
  ADD PRIMARY KEY (`id_niveau_controle`);

--
-- Index pour la table `niveau_cp`
--
ALTER TABLE `niveau_cp`
  ADD PRIMARY KEY (`id_niveau_cp`);

--
-- Index pour la table `niveau_evaluation`
--
ALTER TABLE `niveau_evaluation`
  ADD PRIMARY KEY (`id_niveau_evaluation`);

--
-- Index pour la table `niveau_personnel`
--
ALTER TABLE `niveau_personnel`
  ADD PRIMARY KEY (`id_niveau_personnel`);

--
-- Index pour la table `niveau_sie_cec`
--
ALTER TABLE `niveau_sie_cec`
  ADD PRIMARY KEY (`id_niveau_sie`);

--
-- Index pour la table `ong`
--
ALTER TABLE `ong`
  ADD PRIMARY KEY (`id_promoteur`);

--
-- Index pour la table `partenaire`
--
ALTER TABLE `partenaire`
  ADD PRIMARY KEY (`id_partenaire`);

--
-- Index pour la table `particulier`
--
ALTER TABLE `particulier`
  ADD PRIMARY KEY (`id_promoteur`);

--
-- Index pour la table `performance`
--
ALTER TABLE `performance`
  ADD PRIMARY KEY (`id_performance`),
  ADD KEY `FKhq553l4y9jlh130ovpiw6inj3` (`id_alpha`);

--
-- Index pour la table `periode_activite`
--
ALTER TABLE `periode_activite`
  ADD PRIMARY KEY (`id_periode_activite`);

--
-- Index pour la table `periode_evaluation`
--
ALTER TABLE `periode_evaluation`
  ADD PRIMARY KEY (`id_periode_evaluation`);

--
-- Index pour la table `periodicite`
--
ALTER TABLE `periodicite`
  ADD PRIMARY KEY (`id_periodicite`);

--
-- Index pour la table `permission`
--
ALTER TABLE `permission`
  ADD PRIMARY KEY (`id_permission`),
  ADD UNIQUE KEY `UKm59hmtoyt55ivhub3xl2r5tlb` (`code_permission`);

--
-- Index pour la table `personnel`
--
ALTER TABLE `personnel`
  ADD PRIMARY KEY (`id_personnel`),
  ADD KEY `FK1v639mhdoua9isl75opnmi2du` (`id_centre`),
  ADD KEY `FKmadesnax9hne2ctcj0lc0id0h` (`id_civilite`),
  ADD KEY `FK2fkprp06tlnn93ojohkffvkfv` (`id_fonction`),
  ADD KEY `FKlomxxqxe73uxls1sw4oypb1e4` (`id_niveau_personnel`),
  ADD KEY `FKr8sdqk9fp2gpvmqlblpd95xkd` (`id_statut_personnel`),
  ADD KEY `FKid9oexngguc9rrt0x2yfjtnh7` (`id_structure_formation_certification`);

--
-- Index pour la table `personnemorale`
--
ALTER TABLE `personnemorale`
  ADD PRIMARY KEY (`id_promoteur`),
  ADD KEY `FK8ilcveja7pm3nyu829sixjy0v` (`id_type_personne_morale`);

--
-- Index pour la table `personnephysique`
--
ALTER TABLE `personnephysique`
  ADD PRIMARY KEY (`id_promoteur`);

--
-- Index pour la table `programme`
--
ALTER TABLE `programme`
  ADD PRIMARY KEY (`id_programme`);

--
-- Index pour la table `programme_alpha`
--
ALTER TABLE `programme_alpha`
  ADD PRIMARY KEY (`id_programme_alpha`),
  ADD KEY `FK9fwwuwniqneewxfs428y3wp6h` (`id_centre`),
  ADD KEY `FK5dcv56uih5hhu6g1gijdhum6b` (`id_programme`);

--
-- Index pour la table `promoteur`
--
ALTER TABLE `promoteur`
  ADD PRIMARY KEY (`id_promoteur`);

--
-- Index pour la table `ptf`
--
ALTER TABLE `ptf`
  ADD PRIMARY KEY (`id_promoteur`);

--
-- Index pour la table `regimealphabetisation`
--
ALTER TABLE `regimealphabetisation`
  ADD PRIMARY KEY (`id_regime_alpha`);

--
-- Index pour la table `ressource_financiere_materiel`
--
ALTER TABLE `ressource_financiere_materiel`
  ADD PRIMARY KEY (`id_ressource_financiere`),
  ADD KEY `FK8s2lhfj968ofld4k1iydb8ca6` (`id_centre`),
  ADD KEY `FK1riknkq9cetns18bb6ed2l53c` (`id_designation`);

--
-- Index pour la table `role_fonctionnalite_permission`
--
ALTER TABLE `role_fonctionnalite_permission`
  ADD PRIMARY KEY (`id_role_fonctionnalite_permission`),
  ADD KEY `FKqox7tj1nbgrp6ne9raeosgwnw` (`id_fonctionnalite`),
  ADD KEY `FKlqn2aa7tq9jife0kd4yc4ufub` (`id_permission`),
  ADD KEY `FK7c3v7b5k3jhopb6vg31c2lhnw` (`id_role`);

--
-- Index pour la table `sie`
--
ALTER TABLE `sie`
  ADD PRIMARY KEY (`id_centre`);

--
-- Index pour la table `sie_niveau`
--
ALTER TABLE `sie_niveau`
  ADD PRIMARY KEY (`id_sie_niveau`),
  ADD KEY `FK1wslppnula7xwvup535rd8y9f` (`id_annee_scolaire`),
  ADD KEY `FK3dgn7waamqgjjdwifxc1gytys` (`id_centre`),
  ADD KEY `FKkq9uflikuxsx55pmjylevu36` (`id_niveau_sie`);

--
-- Index pour la table `societe_civile`
--
ALTER TABLE `societe_civile`
  ADD PRIMARY KEY (`id_promoteur`);

--
-- Index pour la table `sous_prefecture`
--
ALTER TABLE `sous_prefecture`
  ADD PRIMARY KEY (`id_sous_prefecture`),
  ADD KEY `FKh2aw805alc1un1qk8ux2q58oe` (`id_departement`);

--
-- Index pour la table `statut_personnel`
--
ALTER TABLE `statut_personnel`
  ADD PRIMARY KEY (`id_statut_personnel`);

--
-- Index pour la table `structure_formation_certification`
--
ALTER TABLE `structure_formation_certification`
  ADD PRIMARY KEY (`id_structure_formation_certification`);

--
-- Index pour la table `support_didactique`
--
ALTER TABLE `support_didactique`
  ADD PRIMARY KEY (`id_support_didactique`);

--
-- Index pour la table `support_didactique_alpha`
--
ALTER TABLE `support_didactique_alpha`
  ADD PRIMARY KEY (`id_support_didactique_alpha`),
  ADD KEY `FKsslphias1m5vv15vc6xb1vafx` (`id_centre`),
  ADD KEY `FKs37232ync60hrsuk06hfavsud` (`id_support_didactique`);

--
-- Index pour la table `taux_evaluation`
--
ALTER TABLE `taux_evaluation`
  ADD PRIMARY KEY (`id_taux_evaluation`);

--
-- Index pour la table `theme_evaluation`
--
ALTER TABLE `theme_evaluation`
  ADD PRIMARY KEY (`id_theme_evaluation`);

--
-- Index pour la table `theme_evaluation_niveau1`
--
ALTER TABLE `theme_evaluation_niveau1`
  ADD PRIMARY KEY (`id_theme_evaluation_niveau1`);

--
-- Index pour la table `theme_evaluation_niveau2_post_alpha`
--
ALTER TABLE `theme_evaluation_niveau2_post_alpha`
  ADD PRIMARY KEY (`id_theme_evaluation_n2_post_alpha`);

--
-- Index pour la table `type_alpha`
--
ALTER TABLE `type_alpha`
  ADD PRIMARY KEY (`id_type_alpha`);

--
-- Index pour la table `type_document`
--
ALTER TABLE `type_document`
  ADD PRIMARY KEY (`id_type_document`);

--
-- Index pour la table `type_personne_morale`
--
ALTER TABLE `type_personne_morale`
  ADD PRIMARY KEY (`id_type_personne_morale`);

--
-- Index pour la table `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`id_role`,`id_user`),
  ADD KEY `FKic7gy45506t2ta1qbd676kpu1` (`id_user`);

--
-- Index pour la table `visite`
--
ALTER TABLE `visite`
  ADD PRIMARY KEY (`id_visite`),
  ADD KEY `FK6e231mpt1ab5uklgaw9mrf9p6` (`id_alpha`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `anne_scolaire`
--
ALTER TABLE `anne_scolaire`
  MODIFY `id_annee_scolaire` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `appui_partenaire`
--
ALTER TABLE `appui_partenaire`
  MODIFY `id_appui_partenaire` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `app_role`
--
ALTER TABLE `app_role`
  MODIFY `id_role` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `app_user`
--
ALTER TABLE `app_user`
  MODIFY `id_user` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `aspect_a_ameliorer`
--
ALTER TABLE `aspect_a_ameliorer`
  MODIFY `id_aspect_a_ameliorer` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `autorite_autorisation`
--
ALTER TABLE `autorite_autorisation`
  MODIFY `id_autorite_autorisation` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `campagne`
--
ALTER TABLE `campagne`
  MODIFY `id_compagne` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `categorie_appui`
--
ALTER TABLE `categorie_appui`
  MODIFY `id_categorie_appui` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `categorie_centre_alpha`
--
ALTER TABLE `categorie_centre_alpha`
  MODIFY `id_categorie_centre_alpha` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `cec_niveau`
--
ALTER TABLE `cec_niveau`
  MODIFY `id_cec_niveau` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `centre`
--
ALTER TABLE `centre`
  MODIFY `id_centre` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT pour la table `civilite`
--
ALTER TABLE `civilite`
  MODIFY `id_civilite` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `commune`
--
ALTER TABLE `commune`
  MODIFY `id_commune` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `competence`
--
ALTER TABLE `competence`
  MODIFY `id_competence` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `competence_centre`
--
ALTER TABLE `competence_centre`
  MODIFY `id_competence_centre` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `controle`
--
ALTER TABLE `controle`
  MODIFY `id_controle` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `cp_niveau`
--
ALTER TABLE `cp_niveau`
  MODIFY `id_cp_niveau` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `departement`
--
ALTER TABLE `departement`
  MODIFY `id_departement` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `designation`
--
ALTER TABLE `designation`
  MODIFY `id_designation` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `difficulte`
--
ALTER TABLE `difficulte`
  MODIFY `id_difficulte` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `difficulte_alpha`
--
ALTER TABLE `difficulte_alpha`
  MODIFY `id_difficulte_alpha` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `diplome`
--
ALTER TABLE `diplome`
  MODIFY `id_diplome` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `diplome_personnel`
--
ALTER TABLE `diplome_personnel`
  MODIFY `id_diplome_personnel` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `discipline`
--
ALTER TABLE `discipline`
  MODIFY `id_discipline` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `document`
--
ALTER TABLE `document`
  MODIFY `id_document` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `domaine_activite`
--
ALTER TABLE `domaine_activite`
  MODIFY `id_domaine_activite` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `domaine_activite_alpha`
--
ALTER TABLE `domaine_activite_alpha`
  MODIFY `id_domaine_activite_alpha` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `drena`
--
ALTER TABLE `drena`
  MODIFY `id_drena` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `drena_departement`
--
ALTER TABLE `drena_departement`
  MODIFY `id_drena_depart` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_abandon_alpha`
--
ALTER TABLE `effectif_abandon_alpha`
  MODIFY `id_effectif_abandon_alpha` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `effectif_abandon_cec`
--
ALTER TABLE `effectif_abandon_cec`
  MODIFY `id_effectif_debut10` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_abandon_cp`
--
ALTER TABLE `effectif_abandon_cp`
  MODIFY `id_effectif_debut15` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_abondan_sie`
--
ALTER TABLE `effectif_abondan_sie`
  MODIFY `id_effectif_debut21` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_admis_integration_cp`
--
ALTER TABLE `effectif_admis_integration_cp`
  MODIFY `id_effectif_debut16` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_alpha`
--
ALTER TABLE `effectif_alpha`
  MODIFY `id_effectif_alpha` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `effectif_cec`
--
ALTER TABLE `effectif_cec`
  MODIFY `id_effectif_cec` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `effectif_cepe_cec`
--
ALTER TABLE `effectif_cepe_cec`
  MODIFY `id_effectif_debut12` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_cepe_cp`
--
ALTER TABLE `effectif_cepe_cp`
  MODIFY `id_effectif_debut18` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_cp`
--
ALTER TABLE `effectif_cp`
  MODIFY `id_effectif_debut14` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `effectif_integration_formel_cp`
--
ALTER TABLE `effectif_integration_formel_cp`
  MODIFY `id_effectif_debut17` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_passage_alpha`
--
ALTER TABLE `effectif_passage_alpha`
  MODIFY `id_effectif_passage_alpha` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_promu_cec`
--
ALTER TABLE `effectif_promu_cec`
  MODIFY `id_effectif_debut11` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_promu_sie`
--
ALTER TABLE `effectif_promu_sie`
  MODIFY `id_effectif_debut22` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_reverse_formel_sie`
--
ALTER TABLE `effectif_reverse_formel_sie`
  MODIFY `id_effectif_debut23` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_sie`
--
ALTER TABLE `effectif_sie`
  MODIFY `id_effectif_debut20` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_situation_handicap_alpha`
--
ALTER TABLE `effectif_situation_handicap_alpha`
  MODIFY `id_effectif_situation_handicap_alpha` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_situation_handicap_cec`
--
ALTER TABLE `effectif_situation_handicap_cec`
  MODIFY `id_effectif_debut13` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `effectif_situation_handicap_cp`
--
ALTER TABLE `effectif_situation_handicap_cp`
  MODIFY `id_effectif_debut19` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `effectif_situation_handicap_sie`
--
ALTER TABLE `effectif_situation_handicap_sie`
  MODIFY `id_effectif_debut24` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `evaluation`
--
ALTER TABLE `evaluation`
  MODIFY `id_evaluation` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `fonction`
--
ALTER TABLE `fonction`
  MODIFY `id_fonction` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `fonctionnalite`
--
ALTER TABLE `fonctionnalite`
  MODIFY `id_fonctionnalite` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=118;

--
-- AUTO_INCREMENT pour la table `iep`
--
ALTER TABLE `iep`
  MODIFY `id_iep` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `impact`
--
ALTER TABLE `impact`
  MODIFY `id_impact` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `impact_alpha`
--
ALTER TABLE `impact_alpha`
  MODIFY `id_impact_alpha` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `infrastructure`
--
ALTER TABLE `infrastructure`
  MODIFY `id_infrastructure` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `infrastructure_centre`
--
ALTER TABLE `infrastructure_centre`
  MODIFY `id_infrastructure_centre` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `langue_apprentissage`
--
ALTER TABLE `langue_apprentissage`
  MODIFY `id_langue` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `localite_d_implantation`
--
ALTER TABLE `localite_d_implantation`
  MODIFY `id_localite` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `manuel`
--
ALTER TABLE `manuel`
  MODIFY `id_manuel` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `materiels_pedagogique`
--
ALTER TABLE `materiels_pedagogique`
  MODIFY `id_materiel_pedagogique` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `materiel_alpha`
--
ALTER TABLE `materiel_alpha`
  MODIFY `id_materiel_alpha` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `milieu_implantation`
--
ALTER TABLE `milieu_implantation`
  MODIFY `id_milieu_implentation` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `modealphabetisation`
--
ALTER TABLE `modealphabetisation`
  MODIFY `id_modealpha` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `naturecentre`
--
ALTER TABLE `naturecentre`
  MODIFY `id_naturecentre` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `nature_document`
--
ALTER TABLE `nature_document`
  MODIFY `id_nature_document` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `niveau_alpha`
--
ALTER TABLE `niveau_alpha`
  MODIFY `id_niveau_alpha` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT pour la table `niveau_controle`
--
ALTER TABLE `niveau_controle`
  MODIFY `id_niveau_controle` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `niveau_cp`
--
ALTER TABLE `niveau_cp`
  MODIFY `id_niveau_cp` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `niveau_evaluation`
--
ALTER TABLE `niveau_evaluation`
  MODIFY `id_niveau_evaluation` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `niveau_personnel`
--
ALTER TABLE `niveau_personnel`
  MODIFY `id_niveau_personnel` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `niveau_sie_cec`
--
ALTER TABLE `niveau_sie_cec`
  MODIFY `id_niveau_sie` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT pour la table `partenaire`
--
ALTER TABLE `partenaire`
  MODIFY `id_partenaire` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `performance`
--
ALTER TABLE `performance`
  MODIFY `id_performance` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `periode_activite`
--
ALTER TABLE `periode_activite`
  MODIFY `id_periode_activite` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `periode_evaluation`
--
ALTER TABLE `periode_evaluation`
  MODIFY `id_periode_evaluation` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `periodicite`
--
ALTER TABLE `periodicite`
  MODIFY `id_periodicite` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `permission`
--
ALTER TABLE `permission`
  MODIFY `id_permission` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `personnel`
--
ALTER TABLE `personnel`
  MODIFY `id_personnel` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `programme`
--
ALTER TABLE `programme`
  MODIFY `id_programme` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `programme_alpha`
--
ALTER TABLE `programme_alpha`
  MODIFY `id_programme_alpha` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `promoteur`
--
ALTER TABLE `promoteur`
  MODIFY `id_promoteur` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `regimealphabetisation`
--
ALTER TABLE `regimealphabetisation`
  MODIFY `id_regime_alpha` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `ressource_financiere_materiel`
--
ALTER TABLE `ressource_financiere_materiel`
  MODIFY `id_ressource_financiere` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `role_fonctionnalite_permission`
--
ALTER TABLE `role_fonctionnalite_permission`
  MODIFY `id_role_fonctionnalite_permission` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=431;

--
-- AUTO_INCREMENT pour la table `sie_niveau`
--
ALTER TABLE `sie_niveau`
  MODIFY `id_sie_niveau` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `sous_prefecture`
--
ALTER TABLE `sous_prefecture`
  MODIFY `id_sous_prefecture` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `statut_personnel`
--
ALTER TABLE `statut_personnel`
  MODIFY `id_statut_personnel` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `structure_formation_certification`
--
ALTER TABLE `structure_formation_certification`
  MODIFY `id_structure_formation_certification` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `support_didactique`
--
ALTER TABLE `support_didactique`
  MODIFY `id_support_didactique` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `support_didactique_alpha`
--
ALTER TABLE `support_didactique_alpha`
  MODIFY `id_support_didactique_alpha` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `taux_evaluation`
--
ALTER TABLE `taux_evaluation`
  MODIFY `id_taux_evaluation` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `theme_evaluation`
--
ALTER TABLE `theme_evaluation`
  MODIFY `id_theme_evaluation` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `theme_evaluation_niveau1`
--
ALTER TABLE `theme_evaluation_niveau1`
  MODIFY `id_theme_evaluation_niveau1` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `theme_evaluation_niveau2_post_alpha`
--
ALTER TABLE `theme_evaluation_niveau2_post_alpha`
  MODIFY `id_theme_evaluation_n2_post_alpha` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `type_alpha`
--
ALTER TABLE `type_alpha`
  MODIFY `id_type_alpha` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `type_document`
--
ALTER TABLE `type_document`
  MODIFY `id_type_document` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `type_personne_morale`
--
ALTER TABLE `type_personne_morale`
  MODIFY `id_type_personne_morale` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `visite`
--
ALTER TABLE `visite`
  MODIFY `id_visite` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `alpha`
--
ALTER TABLE `alpha`
  ADD CONSTRAINT `FK3k5nywxuk4gpprww0qijwqiit` FOREIGN KEY (`id_type_alpha`) REFERENCES `type_alpha` (`id_type_alpha`),
  ADD CONSTRAINT `FKi1d8ygbhpjxquntoj6l64pnxv` FOREIGN KEY (`id_regime_alpha`) REFERENCES `regimealphabetisation` (`id_regime_alpha`),
  ADD CONSTRAINT `FKip3sh04wb1qr6umqtbpfxlod` FOREIGN KEY (`id_compagne`) REFERENCES `campagne` (`id_compagne`),
  ADD CONSTRAINT `FKk21bnk7oaqw3spxujaru89hrm` FOREIGN KEY (`id_categorie_centre_alpha`) REFERENCES `categorie_centre_alpha` (`id_categorie_centre_alpha`),
  ADD CONSTRAINT `FKpa0yjlbfnm4okfabhwfa4gdf3` FOREIGN KEY (`id_centre`) REFERENCES `centre` (`id_centre`);

--
-- Contraintes pour la table `appui_partenaire`
--
ALTER TABLE `appui_partenaire`
  ADD CONSTRAINT `FKcki4b399611hqdwk43eskc9al` FOREIGN KEY (`id_partenaire`) REFERENCES `partenaire` (`id_partenaire`),
  ADD CONSTRAINT `FKgrinbg2oejieodm9cduqdjbu` FOREIGN KEY (`id_categorie_appui`) REFERENCES `categorie_appui` (`id_categorie_appui`),
  ADD CONSTRAINT `FKjuttgiio5edr0oou4hp61mqdp` FOREIGN KEY (`id_centre`) REFERENCES `centre` (`id_centre`);

--
-- Contraintes pour la table `cec`
--
ALTER TABLE `cec`
  ADD CONSTRAINT `FKm7bkwf00idinjiwgugelpm2dj` FOREIGN KEY (`id_centre`) REFERENCES `centre` (`id_centre`);

--
-- Contraintes pour la table `cec_niveau`
--
ALTER TABLE `cec_niveau`
  ADD CONSTRAINT `FK6gcdm1sdptkb6w099cng6o5n5` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`),
  ADD CONSTRAINT `FKpahg441s64a9eqio7ciousov` FOREIGN KEY (`id_centre`) REFERENCES `cec` (`id_centre`),
  ADD CONSTRAINT `FKs41ybrbdbxk41u1jinrowmkyb` FOREIGN KEY (`id_niveau_sie`) REFERENCES `niveau_sie_cec` (`id_niveau_sie`);

--
-- Contraintes pour la table `centre`
--
ALTER TABLE `centre`
  ADD CONSTRAINT `FK4a8bdyicy2idc29p9d2x3dawy` FOREIGN KEY (`id_promoteur`) REFERENCES `promoteur` (`id_promoteur`),
  ADD CONSTRAINT `FKbtly2k8t9vjyrfmihb0d3e3v2` FOREIGN KEY (`id_localite`) REFERENCES `localite_d_implantation` (`id_localite`),
  ADD CONSTRAINT `FKfy8n21gn2p94qvlvc16r56p86` FOREIGN KEY (`id_periodicite`) REFERENCES `periodicite` (`id_periodicite`),
  ADD CONSTRAINT `FKlgeh051xwfpg4uei64caqe68g` FOREIGN KEY (`id_autorite_autorisation`) REFERENCES `autorite_autorisation` (`id_autorite_autorisation`),
  ADD CONSTRAINT `FKnn4j6cxs0ptlv4h8pg38my0d2` FOREIGN KEY (`id_naturecentre`) REFERENCES `naturecentre` (`id_naturecentre`),
  ADD CONSTRAINT `FKo7uf0dosiawdkql62y61krv4k` FOREIGN KEY (`id_iep`) REFERENCES `iep` (`id_iep`);

--
-- Contraintes pour la table `communaute`
--
ALTER TABLE `communaute`
  ADD CONSTRAINT `FKgvhq56ipeo6rvyo7prmn430uy` FOREIGN KEY (`id_promoteur`) REFERENCES `personnemorale` (`id_promoteur`);

--
-- Contraintes pour la table `competence_centre`
--
ALTER TABLE `competence_centre`
  ADD CONSTRAINT `FK7mjj2eof77xgkn1a5mog8jjwn` FOREIGN KEY (`id_competence`) REFERENCES `competence` (`id_competence`),
  ADD CONSTRAINT `FKqmieuern0vt8cv26uo0hxaoyd` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`);

--
-- Contraintes pour la table `controle`
--
ALTER TABLE `controle`
  ADD CONSTRAINT `FK787wjhuigdw2nj74qk8dx82nw` FOREIGN KEY (`id_niveau_controle`) REFERENCES `niveau_controle` (`id_niveau_controle`),
  ADD CONSTRAINT `FKbrkqft2l0wch4bcc7aajjqbfg` FOREIGN KEY (`id_manuel`) REFERENCES `manuel` (`id_manuel`),
  ADD CONSTRAINT `FKexjgus7qb2j43i237jb4mdfir` FOREIGN KEY (`id_alpha`) REFERENCES `alpha` (`id_centre`),
  ADD CONSTRAINT `FKoqen0imbhs2l6f65xy7je7mad` FOREIGN KEY (`id_discipline`) REFERENCES `discipline` (`id_discipline`);

--
-- Contraintes pour la table `cp`
--
ALTER TABLE `cp`
  ADD CONSTRAINT `FKh5653b0xc7fpu350kljje6c3e` FOREIGN KEY (`id_centre`) REFERENCES `centre` (`id_centre`);

--
-- Contraintes pour la table `cp_niveau`
--
ALTER TABLE `cp_niveau`
  ADD CONSTRAINT `FKg4mmwo56m57l2nu1iyl20a9v0` FOREIGN KEY (`id_niveau_cp`) REFERENCES `niveau_cp` (`id_niveau_cp`),
  ADD CONSTRAINT `FKm2g50repm52gotfhc1i7sm9y3` FOREIGN KEY (`id_centre`) REFERENCES `cp` (`id_centre`),
  ADD CONSTRAINT `FKnrcjero9q66sxskfvpg5jnmsv` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`);

--
-- Contraintes pour la table `difficulte_alpha`
--
ALTER TABLE `difficulte_alpha`
  ADD CONSTRAINT `FKq54rdy7lr4qqwyhd2rqs9hfp9` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`),
  ADD CONSTRAINT `FKsscwl64ryrbtxarry4ha9swia` FOREIGN KEY (`id_difficulte`) REFERENCES `difficulte` (`id_difficulte`);

--
-- Contraintes pour la table `diplome_personnel`
--
ALTER TABLE `diplome_personnel`
  ADD CONSTRAINT `FK487oep9y1r5958nw0km3n0fw1` FOREIGN KEY (`id_diplome`) REFERENCES `diplome` (`id_diplome`),
  ADD CONSTRAINT `FKk1ml29jynyt2l5567la5gk93f` FOREIGN KEY (`id_personnel`) REFERENCES `personnel` (`id_personnel`);

--
-- Contraintes pour la table `document`
--
ALTER TABLE `document`
  ADD CONSTRAINT `FK8bfd998of8d3mledvq68sf0s7` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`),
  ADD CONSTRAINT `FK9bmhlm70xd6kmypoglxrun2qj` FOREIGN KEY (`id_type_document`) REFERENCES `type_document` (`id_type_document`),
  ADD CONSTRAINT `FKjmhn0bqec0igvd0ot8r3u39u0` FOREIGN KEY (`id_nature_document`) REFERENCES `nature_document` (`id_nature_document`);

--
-- Contraintes pour la table `domaine_activite_alpha`
--
ALTER TABLE `domaine_activite_alpha`
  ADD CONSTRAINT `FK89nlq7yy8whph0527ybynqd59` FOREIGN KEY (`id_domaine_activite`) REFERENCES `domaine_activite` (`id_domaine_activite`),
  ADD CONSTRAINT `FKjy7bv01he29iy2nxwlgrq324r` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`);

--
-- Contraintes pour la table `drena_departement`
--
ALTER TABLE `drena_departement`
  ADD CONSTRAINT `FKamuemov5w4fx8ip5lftfp88jh` FOREIGN KEY (`id_departement`) REFERENCES `departement` (`id_departement`),
  ADD CONSTRAINT `FKkqf5lsf0xad18t1b4favmepjw` FOREIGN KEY (`id_drena`) REFERENCES `drena` (`id_drena`);

--
-- Contraintes pour la table `effectif_abandon_alpha`
--
ALTER TABLE `effectif_abandon_alpha`
  ADD CONSTRAINT `FK4vmf372103udods5c23q4n9tp` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`),
  ADD CONSTRAINT `FKkybynm36jkudwq8geeiqqkyyl` FOREIGN KEY (`id_periode_activite`) REFERENCES `periode_activite` (`id_periode_activite`);

--
-- Contraintes pour la table `effectif_abandon_cec`
--
ALTER TABLE `effectif_abandon_cec`
  ADD CONSTRAINT `FK1ywbonxpdx7coendpua1dbrl6` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`),
  ADD CONSTRAINT `FKkypn5qe50jicyaj56ifkevjv3` FOREIGN KEY (`id_centre`) REFERENCES `cec` (`id_centre`),
  ADD CONSTRAINT `FKomo1032uoa5t5hk86209imlu7` FOREIGN KEY (`id_niveau_sie`) REFERENCES `niveau_sie_cec` (`id_niveau_sie`);

--
-- Contraintes pour la table `effectif_abandon_cp`
--
ALTER TABLE `effectif_abandon_cp`
  ADD CONSTRAINT `FKevltctixx1yd51jnbmmhxxril` FOREIGN KEY (`id_centre`) REFERENCES `cp` (`id_centre`),
  ADD CONSTRAINT `FKpudnoromtowgoevp81923phxj` FOREIGN KEY (`id_niveau_cp`) REFERENCES `niveau_cp` (`id_niveau_cp`),
  ADD CONSTRAINT `FKt9fsxig6nbkimx293nwl9ua23` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`);

--
-- Contraintes pour la table `effectif_abondan_sie`
--
ALTER TABLE `effectif_abondan_sie`
  ADD CONSTRAINT `FKhu5g8qw96jufd9fv4bejdybmm` FOREIGN KEY (`id_niveau_sie`) REFERENCES `niveau_sie_cec` (`id_niveau_sie`),
  ADD CONSTRAINT `FKq6kcwf66r4f2l381jvc2gdc0t` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`);

--
-- Contraintes pour la table `effectif_admis_integration_cp`
--
ALTER TABLE `effectif_admis_integration_cp`
  ADD CONSTRAINT `FKg2slic9676frvjq8sfl5bg7w0` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`),
  ADD CONSTRAINT `FKjg6als64mvdgpc4rx34l4tan2` FOREIGN KEY (`id_niveau_cp`) REFERENCES `niveau_cp` (`id_niveau_cp`),
  ADD CONSTRAINT `FKpi67h44534cj2hrlwtsrghg9u` FOREIGN KEY (`id_centre`) REFERENCES `cp` (`id_centre`);

--
-- Contraintes pour la table `effectif_alpha`
--
ALTER TABLE `effectif_alpha`
  ADD CONSTRAINT `FK74f5j85ycklgh9kpmwvlwpj9m` FOREIGN KEY (`id_periode_activite`) REFERENCES `periode_activite` (`id_periode_activite`),
  ADD CONSTRAINT `FKbt49ubckywul04rvejl580ooy` FOREIGN KEY (`id_niveau_alpha`) REFERENCES `niveau_alpha` (`id_niveau_alpha`),
  ADD CONSTRAINT `FKppwfgq8smgjf6hxo6xuj5bej8` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`);

--
-- Contraintes pour la table `effectif_cec`
--
ALTER TABLE `effectif_cec`
  ADD CONSTRAINT `FKaj41h71xtrbmrpao0qsre1i2p` FOREIGN KEY (`id_centre`) REFERENCES `cec` (`id_centre`),
  ADD CONSTRAINT `FKb4044wgbb8yo20rvjdtfmc9ie` FOREIGN KEY (`id_periode_activite`) REFERENCES `periode_activite` (`id_periode_activite`),
  ADD CONSTRAINT `FKjkr6l1b695f54qwa6r01khsvr` FOREIGN KEY (`id_niveau_sie`) REFERENCES `niveau_sie_cec` (`id_niveau_sie`);

--
-- Contraintes pour la table `effectif_cepe_cec`
--
ALTER TABLE `effectif_cepe_cec`
  ADD CONSTRAINT `FK513bicw2vkutxq5gwpcyhwlkg` FOREIGN KEY (`cec_id_centre`) REFERENCES `cec` (`id_centre`),
  ADD CONSTRAINT `FK8mgl232dkl26xnu2iuglp6x3k` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`),
  ADD CONSTRAINT `FKnola2gquep2eakyq235q75kt1` FOREIGN KEY (`id_centre`) REFERENCES `cec` (`id_centre`);

--
-- Contraintes pour la table `effectif_cepe_cp`
--
ALTER TABLE `effectif_cepe_cp`
  ADD CONSTRAINT `FKkcug02574vpc0uhgctfj4wah0` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`),
  ADD CONSTRAINT `FKqfijvnjwa4773n7i65eloc1ay` FOREIGN KEY (`id_centre`) REFERENCES `cp` (`id_centre`);

--
-- Contraintes pour la table `effectif_cp`
--
ALTER TABLE `effectif_cp`
  ADD CONSTRAINT `FK57319994d4mlyofk738rdf9tt` FOREIGN KEY (`id_centre`) REFERENCES `cp` (`id_centre`),
  ADD CONSTRAINT `FK8oo80aofr1651t0j6cvcaymeu` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`),
  ADD CONSTRAINT `FKex8t04f3od7idbwynbcukdfx1` FOREIGN KEY (`id_niveau_cp`) REFERENCES `niveau_cp` (`id_niveau_cp`);

--
-- Contraintes pour la table `effectif_integration_formel_cp`
--
ALTER TABLE `effectif_integration_formel_cp`
  ADD CONSTRAINT `FK2rgei6kug0uhvhv1u1x2nf27a` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`),
  ADD CONSTRAINT `FKa30i9qhjjtlpibhxbxbqy0u39` FOREIGN KEY (`id_centre`) REFERENCES `cp` (`id_centre`),
  ADD CONSTRAINT `FKpxdudsmf7e8qb25fyyc3wtqpg` FOREIGN KEY (`id_niveau_cp`) REFERENCES `niveau_cp` (`id_niveau_cp`);

--
-- Contraintes pour la table `effectif_passage_alpha`
--
ALTER TABLE `effectif_passage_alpha`
  ADD CONSTRAINT `FKbnkhkodv3qkxy7remb6ofruq6` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`),
  ADD CONSTRAINT `FKpn0ok3lvt6idjinwm34i7geo9` FOREIGN KEY (`id_periode_activite`) REFERENCES `periode_activite` (`id_periode_activite`);

--
-- Contraintes pour la table `effectif_promu_cec`
--
ALTER TABLE `effectif_promu_cec`
  ADD CONSTRAINT `FKe0rs9rceceuo2ll6u21ktpk55` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`),
  ADD CONSTRAINT `FKgwoqmrqsygujpjhtku9kyret8` FOREIGN KEY (`id_niveau_sie`) REFERENCES `niveau_sie_cec` (`id_niveau_sie`),
  ADD CONSTRAINT `FKp94rfo6j36qnvujq49mvl1886` FOREIGN KEY (`id_centre`) REFERENCES `cec` (`id_centre`);

--
-- Contraintes pour la table `effectif_promu_sie`
--
ALTER TABLE `effectif_promu_sie`
  ADD CONSTRAINT `FK8et1vvpgqgr4wcfmfyndw0ume` FOREIGN KEY (`id_niveau_sie`) REFERENCES `niveau_sie_cec` (`id_niveau_sie`),
  ADD CONSTRAINT `FKolq5j9oxd6m6pi9v8gixtsq5u` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`);

--
-- Contraintes pour la table `effectif_reverse_formel_sie`
--
ALTER TABLE `effectif_reverse_formel_sie`
  ADD CONSTRAINT `FK4te9ltjlsn1yufaxs1oieri2j` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`),
  ADD CONSTRAINT `FK7mv37mxcycvl16qm5bmjtxro3` FOREIGN KEY (`id_niveau_sie`) REFERENCES `niveau_sie_cec` (`id_niveau_sie`);

--
-- Contraintes pour la table `effectif_sie`
--
ALTER TABLE `effectif_sie`
  ADD CONSTRAINT `FKq5erpvjjqi68lvryar7emy9dr` FOREIGN KEY (`id_niveau_sie`) REFERENCES `niveau_sie_cec` (`id_niveau_sie`),
  ADD CONSTRAINT `FKxv5on1h5m62qyuw2iwc97ule` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`);

--
-- Contraintes pour la table `effectif_situation_handicap_alpha`
--
ALTER TABLE `effectif_situation_handicap_alpha`
  ADD CONSTRAINT `FK1v0ek2brba87w2n8xtqqylqas` FOREIGN KEY (`id_periode_activite`) REFERENCES `periode_activite` (`id_periode_activite`),
  ADD CONSTRAINT `FKrs5oqk2dei5v3bveig8dhrcfy` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`);

--
-- Contraintes pour la table `effectif_situation_handicap_cec`
--
ALTER TABLE `effectif_situation_handicap_cec`
  ADD CONSTRAINT `FK3jqugqdvkk1w424xuycr31oer` FOREIGN KEY (`id_niveau_sie`) REFERENCES `niveau_sie_cec` (`id_niveau_sie`),
  ADD CONSTRAINT `FKfjhqjjtkdto9h4a2qq74ewdjb` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`);

--
-- Contraintes pour la table `effectif_situation_handicap_cp`
--
ALTER TABLE `effectif_situation_handicap_cp`
  ADD CONSTRAINT `FKa2ct88gg0vdsnmsaqh7cd1apy` FOREIGN KEY (`id_centre`) REFERENCES `cp` (`id_centre`),
  ADD CONSTRAINT `FKav71jft4xd4xaj50ewpqovxcq` FOREIGN KEY (`id_niveau_cp`) REFERENCES `niveau_cp` (`id_niveau_cp`),
  ADD CONSTRAINT `FKrovyjl7ecsi1nlpgcgomf3wh6` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`);

--
-- Contraintes pour la table `effectif_situation_handicap_sie`
--
ALTER TABLE `effectif_situation_handicap_sie`
  ADD CONSTRAINT `FK4p0qdpiq55t640csflpi1asbs` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`),
  ADD CONSTRAINT `FKmq1msy8oq7dmv0bn0i6sdqnva` FOREIGN KEY (`id_niveau_sie`) REFERENCES `niveau_sie_cec` (`id_niveau_sie`);

--
-- Contraintes pour la table `evaluation`
--
ALTER TABLE `evaluation`
  ADD CONSTRAINT `FK31w8jsk6oergpxrqjhgeueli0` FOREIGN KEY (`id_niveau_evaluation`) REFERENCES `niveau_evaluation` (`id_niveau_evaluation`),
  ADD CONSTRAINT `FK7sgrj7cjx1axd4l3f0axa3lb3` FOREIGN KEY (`id_theme_evaluation`) REFERENCES `theme_evaluation` (`id_theme_evaluation`),
  ADD CONSTRAINT `FKgvnyiqmggrg6f7jeixtl8sp34` FOREIGN KEY (`id_alpha`) REFERENCES `alpha` (`id_centre`),
  ADD CONSTRAINT `FKi39puo7761exdvti27kgg74d8` FOREIGN KEY (`id_periode_evaluation`) REFERENCES `periode_evaluation` (`id_periode_evaluation`),
  ADD CONSTRAINT `FKk794f33mpcx0cg8vmhosdr61f` FOREIGN KEY (`id_taux_evaluation`) REFERENCES `taux_evaluation` (`id_taux_evaluation`);

--
-- Contraintes pour la table `iep`
--
ALTER TABLE `iep`
  ADD CONSTRAINT `FKj4xtn0okb9b4u9an42o6lh3b4` FOREIGN KEY (`id_drena`) REFERENCES `drena` (`id_drena`);

--
-- Contraintes pour la table `impact_alpha`
--
ALTER TABLE `impact_alpha`
  ADD CONSTRAINT `FKhrcnt1ij04ht3l8h6vl10jfqq` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`),
  ADD CONSTRAINT `FKhx7n8lbq1b3hweji91icugqgy` FOREIGN KEY (`id_impact`) REFERENCES `impact` (`id_impact`);

--
-- Contraintes pour la table `infrastructure_centre`
--
ALTER TABLE `infrastructure_centre`
  ADD CONSTRAINT `FKhblvm83o70b60qm4g4ttfx37x` FOREIGN KEY (`id_infrastructure`) REFERENCES `infrastructure` (`id_infrastructure`),
  ADD CONSTRAINT `FKojf3dey8tf7xobjt6u0g9ctem` FOREIGN KEY (`id_centre`) REFERENCES `centre` (`id_centre`);

--
-- Contraintes pour la table `langue_apprentissage`
--
ALTER TABLE `langue_apprentissage`
  ADD CONSTRAINT `FKo42lbljnwwwbbgb9v6dkhul55` FOREIGN KEY (`id_centre`) REFERENCES `centre` (`id_centre`);

--
-- Contraintes pour la table `localite_d_implantation`
--
ALTER TABLE `localite_d_implantation`
  ADD CONSTRAINT `FK61o0qysycv8sfbalbc9k2kkb` FOREIGN KEY (`id_commune`) REFERENCES `commune` (`id_commune`),
  ADD CONSTRAINT `FKkc0qo06dx86pp769w39mqed32` FOREIGN KEY (`id_milieu_implentation`) REFERENCES `milieu_implantation` (`id_milieu_implentation`),
  ADD CONSTRAINT `FKm4fc2kwwx1fd8p0x6nqrw7waf` FOREIGN KEY (`id_sous_prefecture`) REFERENCES `sous_prefecture` (`id_sous_prefecture`);

--
-- Contraintes pour la table `materiel_alpha`
--
ALTER TABLE `materiel_alpha`
  ADD CONSTRAINT `FK53ynq5wpoluxprnhxim8otqwq` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`),
  ADD CONSTRAINT `FKcjlxgusf4jc2or0umq7i7yhar` FOREIGN KEY (`id_materiel_pedagogique`) REFERENCES `materiels_pedagogique` (`id_materiel_pedagogique`);

--
-- Contraintes pour la table `ministere`
--
ALTER TABLE `ministere`
  ADD CONSTRAINT `FKtd8pdrgae71lturjt6gql4wtv` FOREIGN KEY (`id_promoteur`) REFERENCES `personnemorale` (`id_promoteur`);

--
-- Contraintes pour la table `modealphabetisation`
--
ALTER TABLE `modealphabetisation`
  ADD CONSTRAINT `FKlmqatqx8hytg3y346jvcjml0g` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`);

--
-- Contraintes pour la table `niveau_alpha`
--
ALTER TABLE `niveau_alpha`
  ADD CONSTRAINT `FKog45opu9svjrbu60slhq82uuv` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`);

--
-- Contraintes pour la table `ong`
--
ALTER TABLE `ong`
  ADD CONSTRAINT `FKkcke5si35cgv8mw0fujy5o0xm` FOREIGN KEY (`id_promoteur`) REFERENCES `personnemorale` (`id_promoteur`);

--
-- Contraintes pour la table `particulier`
--
ALTER TABLE `particulier`
  ADD CONSTRAINT `FK4iekbqikw5iyaw13499bk0619` FOREIGN KEY (`id_promoteur`) REFERENCES `personnemorale` (`id_promoteur`);

--
-- Contraintes pour la table `performance`
--
ALTER TABLE `performance`
  ADD CONSTRAINT `FKhq553l4y9jlh130ovpiw6inj3` FOREIGN KEY (`id_alpha`) REFERENCES `alpha` (`id_centre`);

--
-- Contraintes pour la table `personnel`
--
ALTER TABLE `personnel`
  ADD CONSTRAINT `FK1v639mhdoua9isl75opnmi2du` FOREIGN KEY (`id_centre`) REFERENCES `centre` (`id_centre`),
  ADD CONSTRAINT `FK2fkprp06tlnn93ojohkffvkfv` FOREIGN KEY (`id_fonction`) REFERENCES `fonction` (`id_fonction`),
  ADD CONSTRAINT `FKid9oexngguc9rrt0x2yfjtnh7` FOREIGN KEY (`id_structure_formation_certification`) REFERENCES `structure_formation_certification` (`id_structure_formation_certification`),
  ADD CONSTRAINT `FKlomxxqxe73uxls1sw4oypb1e4` FOREIGN KEY (`id_niveau_personnel`) REFERENCES `niveau_personnel` (`id_niveau_personnel`),
  ADD CONSTRAINT `FKmadesnax9hne2ctcj0lc0id0h` FOREIGN KEY (`id_civilite`) REFERENCES `civilite` (`id_civilite`),
  ADD CONSTRAINT `FKr8sdqk9fp2gpvmqlblpd95xkd` FOREIGN KEY (`id_statut_personnel`) REFERENCES `statut_personnel` (`id_statut_personnel`);

--
-- Contraintes pour la table `personnemorale`
--
ALTER TABLE `personnemorale`
  ADD CONSTRAINT `FK8ilcveja7pm3nyu829sixjy0v` FOREIGN KEY (`id_type_personne_morale`) REFERENCES `type_personne_morale` (`id_type_personne_morale`),
  ADD CONSTRAINT `FKq7n7g3i4ld789qejxgbvcwi68` FOREIGN KEY (`id_promoteur`) REFERENCES `promoteur` (`id_promoteur`);

--
-- Contraintes pour la table `personnephysique`
--
ALTER TABLE `personnephysique`
  ADD CONSTRAINT `FKh3dha8vxcsf1jco5tkqkhfhw9` FOREIGN KEY (`id_promoteur`) REFERENCES `promoteur` (`id_promoteur`);

--
-- Contraintes pour la table `programme_alpha`
--
ALTER TABLE `programme_alpha`
  ADD CONSTRAINT `FK5dcv56uih5hhu6g1gijdhum6b` FOREIGN KEY (`id_programme`) REFERENCES `programme` (`id_programme`),
  ADD CONSTRAINT `FK9fwwuwniqneewxfs428y3wp6h` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`);

--
-- Contraintes pour la table `ptf`
--
ALTER TABLE `ptf`
  ADD CONSTRAINT `FKip8wbfu5ubepegul9y4cj6w8u` FOREIGN KEY (`id_promoteur`) REFERENCES `personnemorale` (`id_promoteur`);

--
-- Contraintes pour la table `ressource_financiere_materiel`
--
ALTER TABLE `ressource_financiere_materiel`
  ADD CONSTRAINT `FK1riknkq9cetns18bb6ed2l53c` FOREIGN KEY (`id_designation`) REFERENCES `designation` (`id_designation`),
  ADD CONSTRAINT `FK8s2lhfj968ofld4k1iydb8ca6` FOREIGN KEY (`id_centre`) REFERENCES `centre` (`id_centre`);

--
-- Contraintes pour la table `role_fonctionnalite_permission`
--
ALTER TABLE `role_fonctionnalite_permission`
  ADD CONSTRAINT `FK7c3v7b5k3jhopb6vg31c2lhnw` FOREIGN KEY (`id_role`) REFERENCES `app_role` (`id_role`),
  ADD CONSTRAINT `FKlqn2aa7tq9jife0kd4yc4ufub` FOREIGN KEY (`id_permission`) REFERENCES `permission` (`id_permission`),
  ADD CONSTRAINT `FKqox7tj1nbgrp6ne9raeosgwnw` FOREIGN KEY (`id_fonctionnalite`) REFERENCES `fonctionnalite` (`id_fonctionnalite`);

--
-- Contraintes pour la table `sie`
--
ALTER TABLE `sie`
  ADD CONSTRAINT `FKtqunqv4koc22d76kywbia9l7d` FOREIGN KEY (`id_centre`) REFERENCES `centre` (`id_centre`);

--
-- Contraintes pour la table `sie_niveau`
--
ALTER TABLE `sie_niveau`
  ADD CONSTRAINT `FK1wslppnula7xwvup535rd8y9f` FOREIGN KEY (`id_annee_scolaire`) REFERENCES `anne_scolaire` (`id_annee_scolaire`),
  ADD CONSTRAINT `FK3dgn7waamqgjjdwifxc1gytys` FOREIGN KEY (`id_centre`) REFERENCES `sie` (`id_centre`),
  ADD CONSTRAINT `FKkq9uflikuxsx55pmjylevu36` FOREIGN KEY (`id_niveau_sie`) REFERENCES `niveau_sie_cec` (`id_niveau_sie`);

--
-- Contraintes pour la table `societe_civile`
--
ALTER TABLE `societe_civile`
  ADD CONSTRAINT `FKen6vew61544wpt6k53qqh6x6` FOREIGN KEY (`id_promoteur`) REFERENCES `personnemorale` (`id_promoteur`);

--
-- Contraintes pour la table `sous_prefecture`
--
ALTER TABLE `sous_prefecture`
  ADD CONSTRAINT `FKh2aw805alc1un1qk8ux2q58oe` FOREIGN KEY (`id_departement`) REFERENCES `departement` (`id_departement`);

--
-- Contraintes pour la table `support_didactique_alpha`
--
ALTER TABLE `support_didactique_alpha`
  ADD CONSTRAINT `FKs37232ync60hrsuk06hfavsud` FOREIGN KEY (`id_support_didactique`) REFERENCES `support_didactique` (`id_support_didactique`),
  ADD CONSTRAINT `FKsslphias1m5vv15vc6xb1vafx` FOREIGN KEY (`id_centre`) REFERENCES `alpha` (`id_centre`);

--
-- Contraintes pour la table `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `FKa6cvyoid93mvxgu9u7uxa13ef` FOREIGN KEY (`id_role`) REFERENCES `app_role` (`id_role`),
  ADD CONSTRAINT `FKic7gy45506t2ta1qbd676kpu1` FOREIGN KEY (`id_user`) REFERENCES `app_user` (`id_user`);

--
-- Contraintes pour la table `visite`
--
ALTER TABLE `visite`
  ADD CONSTRAINT `FK6e231mpt1ab5uklgaw9mrf9p6` FOREIGN KEY (`id_alpha`) REFERENCES `alpha` (`id_centre`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
