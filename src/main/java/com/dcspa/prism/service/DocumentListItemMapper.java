package com.dcspa.prism.service;

import com.dcspa.prism.dto.DocumentListItem;
import com.dcspa.prism.entity.Centre;
import com.dcspa.prism.entity.Document;
import com.dcspa.prism.entity.NatureDocument;
import com.dcspa.prism.entity.TypeDocument;

public final class DocumentListItemMapper {

	private DocumentListItemMapper() {
	}

	public static DocumentListItem fromDocument(Document d) {
		Centre c = d.getIdCentre();
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
				c != null ? c.getId() : null,
				c != null ? c.getCodeCentre() : null,
				c != null ? c.getLocalisationCentre() : null,
				n != null ? n.getId() : null,
				n != null ? n.getLibelleNatureDocument() : null,
				t != null ? t.getId() : null,
				t != null ? t.getCodeTypeDocument() : null,
				t != null ? t.getLibelleTypeDocument() : null
		);
	}
}
