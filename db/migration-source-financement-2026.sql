-- Référentiel sources de financement (BM, ECOBANK, BCAO, …)
CREATE TABLE IF NOT EXISTS `source_financement` (
  `id_source_financement` int NOT NULL AUTO_INCREMENT,
  `code_source_financement` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `libelle_source_financement` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_source_financement`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `source_financement` (`code_source_financement`, `libelle_source_financement`)
SELECT 'BM', 'Banque mondiale'
WHERE NOT EXISTS (SELECT 1 FROM `source_financement` WHERE `code_source_financement` = 'BM');

INSERT INTO `source_financement` (`code_source_financement`, `libelle_source_financement`)
SELECT 'ECOBANK', 'Ecobank'
WHERE NOT EXISTS (SELECT 1 FROM `source_financement` WHERE `code_source_financement` = 'ECOBANK');

INSERT INTO `source_financement` (`code_source_financement`, `libelle_source_financement`)
SELECT 'BCAO', 'BCAO'
WHERE NOT EXISTS (SELECT 1 FROM `source_financement` WHERE `code_source_financement` = 'BCAO');
