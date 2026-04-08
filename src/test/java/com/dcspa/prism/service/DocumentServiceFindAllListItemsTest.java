package com.dcspa.prism.service;

import com.dcspa.prism.dto.DocumentListFilter;
import com.dcspa.prism.dto.DocumentListItem;
import com.dcspa.prism.entity.Alpha;
import com.dcspa.prism.entity.Document;
import com.dcspa.prism.entity.NatureDocument;
import com.dcspa.prism.entity.TypeDocument;
import com.dcspa.prism.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Vérifie la chaîne repository → mapper pour le module Visites (liste paginée).
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class DocumentServiceFindAllListItemsTest {

	@Mock
	private DocumentRepository documentRepository;

	@InjectMocks
	private DocumentService documentService;

	@Test
	void findAllListItemsReturnsFlattenedRows() {
		Document d = mock(Document.class);
		Alpha a = mock(Alpha.class);
		NatureDocument n = mock(NatureDocument.class);
		TypeDocument t = mock(TypeDocument.class);

		when(d.getId()).thenReturn(99);
		when(d.getCodeDocument()).thenReturn("DOC-99");
		when(d.getExiste()).thenReturn("OUI");
		when(d.getAjour()).thenReturn("OUI");
		when(d.getBientenu()).thenReturn("OUI");
		when(d.getRespmethode()).thenReturn("OUI");
		when(d.getBienrensigne()).thenReturn("OUI");
		when(d.getIdCentre()).thenReturn(a);
		when(a.getId()).thenReturn(7);
		when(a.getCodeCentre()).thenReturn("CC-7");
		when(a.getLibelleAlpha()).thenReturn("Centre test");
		when(d.getIdNatureDocument()).thenReturn(n);
		when(n.getId()).thenReturn(11);
		when(n.getLibelleNatureDocument()).thenReturn("Nature X");
		when(d.getIdTypeDocument()).thenReturn(t);
		when(t.getId()).thenReturn(22);
		when(t.getCodeTypeDocument()).thenReturn("T01");
		when(t.getLibelleTypeDocument()).thenReturn("Type Y");

		when(documentRepository.findAll(any(Specification.class), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(d), PageRequest.of(0, 20), 1L));

		Page<DocumentListItem> page = documentService.findAllListItems(PageRequest.of(0, 20), new DocumentListFilter());

		assertThat(page.getTotalElements()).isEqualTo(1);
		DocumentListItem item = page.getContent().get(0);
		assertThat(item.getId()).isEqualTo(99);
		assertThat(item.getCodeDocument()).isEqualTo("DOC-99");
		assertThat(item.getIdCentreAlpha()).isEqualTo(7);
		assertThat(item.getLibelleAlpha()).isEqualTo("Centre test");
		assertThat(item.getLibelleNatureDocument()).isEqualTo("Nature X");
		assertThat(item.getLibelleTypeDocument()).isEqualTo("Type Y");
	}
}
