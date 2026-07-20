package com.dcspa.prism.controller;

import com.dcspa.prism.entity.AnneScolaire;
import com.dcspa.prism.repository.AnneScolaireRepository;
import com.dcspa.prism.service.ApprenantImportTemplateService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Import bulk liste d'apprenants + téléchargement du modèle officiel (Excel / CSV).
 * Colonnes : nom ; prenom ; sexe ; dateNaissance.
 */
@RestController
@RequestMapping("/api/apprenants")
@RequiredArgsConstructor
public class ApprenantImportController {

	private static final MediaType XLSX = MediaType.parseMediaType(
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

	private final AnneScolaireRepository anneScolaireRepository;
	private final ApprenantImportTemplateService templateService;

	@GetMapping("/import/modele")
	public ResponseEntity<byte[]> downloadExcelTemplate() {
		byte[] bytes = templateService.buildExcelTemplate();
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"PRISM_modele_import_apprenants.xlsx\"")
				.contentType(XLSX)
				.body(bytes);
	}

	@GetMapping("/import/modele.csv")
	public ResponseEntity<byte[]> downloadCsvTemplate() {
		byte[] bytes = templateService.buildCsvTemplate();
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"PRISM_modele_import_apprenants.csv\"")
				.contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
				.body(bytes);
	}

	@Transactional(readOnly = true)
	@PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> importFile(
			@RequestParam("file") MultipartFile file,
			@RequestParam(value = "idAnneeScolaire", required = false) Integer idAnneeScolaire) {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("Fichier obligatoire (CSV ou Excel .xlsx)");
		}
		if (idAnneeScolaire != null) {
			AnneScolaire annee = anneScolaireRepository.findById(idAnneeScolaire)
					.orElseThrow(() -> new IllegalArgumentException("Année scolaire introuvable: " + idAnneeScolaire));
			if (annee.getId() == null) {
				throw new IllegalArgumentException("Année scolaire invalide");
			}
		}

		String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase(Locale.ROOT);
		int dataRows;
		try {
			if (name.endsWith(".xlsx") || name.endsWith(".xls")) {
				dataRows = countExcelDataRows(file);
			} else {
				dataRows = countCsvDataRows(file);
			}
		} catch (IllegalArgumentException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new IllegalArgumentException("Impossible de lire le fichier: " + ex.getMessage());
		}

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("accepted", true);
		body.put("fileName", file.getOriginalFilename());
		body.put("lignesDonnees", dataRows);
		body.put("message", "Import accepté (préparation). " + dataRows + " ligne(s) détectée(s).");
		return ResponseEntity.ok(body);
	}

	private int countCsvDataRows(MultipartFile file) throws Exception {
		int lines = 0;
		int dataRows = 0;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines++;
				if (lines == 1 && looksLikeHeader(line)) {
					continue;
				}
				if (!line.isBlank()) {
					dataRows++;
				}
			}
		}
		return dataRows;
	}

	private int countExcelDataRows(MultipartFile file) throws Exception {
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			Sheet sheet = workbook.getSheet("Apprenants");
			if (sheet == null) {
				sheet = workbook.getNumberOfSheets() > 0 ? workbook.getSheetAt(0) : null;
			}
			if (sheet == null) {
				throw new IllegalArgumentException("Feuille Excel introuvable");
			}
			DataFormatter formatter = new DataFormatter();
			int dataRows = 0;
			boolean headerChecked = false;
			for (Row row : sheet) {
				if (row == null) {
					continue;
				}
				String joined = joinRow(row, formatter);
				if (joined.isBlank()) {
					continue;
				}
				if (!headerChecked) {
					headerChecked = true;
					if (looksLikeHeader(joined)) {
						continue;
					}
				}
				dataRows++;
			}
			return dataRows;
		}
	}

	private static String joinRow(Row row, DataFormatter formatter) {
		StringBuilder sb = new StringBuilder();
		short last = row.getLastCellNum();
		for (int i = 0; i < last; i++) {
			if (i > 0) {
				sb.append(';');
			}
			sb.append(formatter.formatCellValue(row.getCell(i)).trim());
		}
		return sb.toString();
	}

	private static boolean looksLikeHeader(String line) {
		String lower = line.toLowerCase(Locale.ROOT);
		return lower.contains("nom") && (lower.contains("prenom") || lower.contains("prénom"));
	}
}
