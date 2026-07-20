package com.dcspa.prism.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

class ApprenantImportTemplateServiceTest {

	private final ApprenantImportTemplateService service = new ApprenantImportTemplateService();

	@Test
	void excelTemplateContainsApprenantsSheetAndHeaders() throws Exception {
		byte[] bytes = service.buildExcelTemplate();
		assertThat(bytes).isNotEmpty();
		try (Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(bytes))) {
			assertThat(wb.getSheet("Instructions")).isNotNull();
			Sheet data = wb.getSheet("Apprenants");
			assertThat(data).isNotNull();
			assertThat(data.getRow(0).getCell(0).getStringCellValue()).isEqualTo("nom");
			assertThat(data.getRow(0).getCell(1).getStringCellValue()).isEqualTo("prenom");
			assertThat(data.getRow(0).getCell(2).getStringCellValue()).isEqualTo("sexe");
			assertThat(data.getRow(0).getCell(3).getStringCellValue()).isEqualTo("dateNaissance");
		}
	}

	@Test
	void csvTemplateContainsHeaderAndExamples() {
		String csv = new String(service.buildCsvTemplate(), StandardCharsets.UTF_8);
		assertThat(csv).startsWith("nom;prenom;sexe;dateNaissance");
		assertThat(csv).contains("KOUASSI");
	}
}
