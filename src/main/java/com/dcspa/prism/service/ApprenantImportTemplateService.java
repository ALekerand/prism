package com.dcspa.prism.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * Modèle d'import bulk apprenants (Excel + CSV) aux couleurs PRISM MENA.
 */
@Service
public class ApprenantImportTemplateService {

	public static final List<String> COLUMNS = List.of("nom", "prenom", "sexe", "dateNaissance");

	private static final byte[] ORANGE_700 = hex("#D66F1E");
	private static final byte[] ORANGE_100 = hex("#FFF5ED");
	private static final byte[] MINT_200 = hex("#D8F0E4");
	private static final byte[] INK = hex("#2A2520");
	private static final byte[] WHITE = hex("#FFFFFF");

	public byte[] buildExcelTemplate() {
		try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			buildInstructionsSheet(workbook);
			buildDataSheet(workbook);
			workbook.write(out);
			return out.toByteArray();
		} catch (IOException ex) {
			throw new IllegalStateException("Impossible de générer le modèle Excel: " + ex.getMessage(), ex);
		}
	}

	public byte[] buildCsvTemplate() {
		String csv = String.join(";", COLUMNS)
				+ "\n"
				+ "KOUASSI;Aya;F;2012-03-15\n"
				+ "TRAORE;Moussa;M;2011-11-02\n";
		return csv.getBytes(StandardCharsets.UTF_8);
	}

	private void buildInstructionsSheet(XSSFWorkbook workbook) {
		Sheet sheet = workbook.createSheet("Instructions");
		CellStyle title = titleStyle(workbook);
		CellStyle body = bodyStyle(workbook);
		CellStyle tip = tipStyle(workbook);

		int r = 0;
		Row titleRow = sheet.createRow(r++);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("PRISM MENA — Modèle d'import des apprenants");
		titleCell.setCellStyle(title);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

		r++;
		writeLine(sheet, r++, body, "1. Remplissez uniquement la feuille « Apprenants ».");
		writeLine(sheet, r++, body, "2. Ne renommez pas les colonnes de l'en-tête (ligne 1).");
		writeLine(sheet, r++, body, "3. Colonnes obligatoires : nom ; prenom ; sexe ; dateNaissance.");
		writeLine(sheet, r++, body, "4. sexe : M ou F (ou Homme / Femme).");
		writeLine(sheet, r++, body, "5. dateNaissance : format AAAA-MM-JJ (ex. 2012-03-15).");
		writeLine(sheet, r++, tip, "Astuce : vous pouvez aussi exporter/enregistrer en CSV (séparateur ;) pour l'import.");
		r++;
		writeLine(sheet, r, tip, "Plateforme PRISM — MENA / DCSPA — Côte d'Ivoire");

		sheet.setColumnWidth(0, 90 * 256);
	}

	private void buildDataSheet(XSSFWorkbook workbook) {
		Sheet sheet = workbook.createSheet("Apprenants");
		CellStyle header = headerStyle(workbook);
		CellStyle example = exampleStyle(workbook);

		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < COLUMNS.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(COLUMNS.get(i));
			cell.setCellStyle(header);
		}

		Object[][] examples = {
				{"KOUASSI", "Aya", "F", "2012-03-15"},
				{"TRAORE", "Moussa", "M", "2011-11-02"},
		};
		for (int i = 0; i < examples.length; i++) {
			Row row = sheet.createRow(i + 1);
			for (int c = 0; c < examples[i].length; c++) {
				Cell cell = row.createCell(c);
				cell.setCellValue(String.valueOf(examples[i][c]));
				cell.setCellStyle(example);
			}
		}

		for (int i = 0; i < COLUMNS.size(); i++) {
			sheet.setColumnWidth(i, 18 * 256);
		}
		sheet.createFreezePane(0, 1);
	}

	private static void writeLine(Sheet sheet, int rowIndex, CellStyle style, String text) {
		Row row = sheet.createRow(rowIndex);
		Cell cell = row.createCell(0);
		cell.setCellValue(text);
		cell.setCellStyle(style);
	}

	private CellStyle titleStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		applyFill(style, ORANGE_700);
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 14);
		setFontColor(font, WHITE);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}

	private CellStyle bodyStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		setFontColor(font, INK);
		style.setFont(font);
		style.setWrapText(true);
		return style;
	}

	private CellStyle tipStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		applyFill(style, MINT_200);
		Font font = workbook.createFont();
		font.setItalic(true);
		font.setFontHeightInPoints((short) 11);
		setFontColor(font, INK);
		style.setFont(font);
		style.setWrapText(true);
		return style;
	}

	private CellStyle headerStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		applyFill(style, ORANGE_700);
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 11);
		setFontColor(font, WHITE);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.WHITE.getIndex());
		style.setTopBorderColor(IndexedColors.WHITE.getIndex());
		style.setLeftBorderColor(IndexedColors.WHITE.getIndex());
		style.setRightBorderColor(IndexedColors.WHITE.getIndex());
		return style;
	}

	private CellStyle exampleStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		applyFill(style, ORANGE_100);
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		setFontColor(font, INK);
		style.setFont(font);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		return style;
	}

	private static void applyFill(XSSFCellStyle style, byte[] rgb) {
		style.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap()));
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	}

	private static void setFontColor(Font font, byte[] rgb) {
		if (font instanceof org.apache.poi.xssf.usermodel.XSSFFont xssfFont) {
			xssfFont.setColor(new XSSFColor(rgb, new DefaultIndexedColorMap()));
		}
	}

	private static byte[] hex(String value) {
		String v = value.startsWith("#") ? value.substring(1) : value;
		return new byte[] {
				(byte) Integer.parseInt(v.substring(0, 2), 16),
				(byte) Integer.parseInt(v.substring(2, 4), 16),
				(byte) Integer.parseInt(v.substring(4, 6), 16),
		};
	}
}
