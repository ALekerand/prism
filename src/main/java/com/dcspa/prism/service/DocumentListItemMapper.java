package com.dcspa.prism.service;

import com.dcspa.prism.dto.DocumentListItem;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Document;
import com.dcspa.prism.entity.NatureDocument;
import com.dcspa.prism.entity.TypeDocument;

public final class DocumentListItemMapper {

	private DocumentListItemMapper() {
	}

	public static DocumentListItem fromDocument(Document d) {
		Alpha a = d.getIdCentre();
		NatureDocument n = d.getIdNatureDocument();
		TypeDocument t = d.getIdTypeDocument();
		return new DocumentListItem(
				d.getId(),
				d.getCodeDocument(),
				d.getExiste(),
				d.getAjour(),
				d.getBientenu(),
				d.getRespmethode(),
				d.getBienrensigne(),
				a != null ? a.getId() : null,
				a != null ? a.getCodeCentre() : null,
				a != null ? a.getLibelleAlpha() : null,
				n != null ? n.getId() : null,
				n != null ? n.getLibelleNatureDocument() : null,
				t != null ? t.getId() : null,
				t != null ? t.getCodeTypeDocument() : null,
				t != null ? t.getLibelleTypeDocument() : null
		);
	}
}
