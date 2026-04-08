package com.dcspa.prism.service.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

// Limite la taille de page pour éviter les requêtes trop lourdes.
public final class PageableUtils {

	public static final int MAX_PAGE_SIZE = 5000;

	private PageableUtils() {
	}

	public static Pageable cap(Pageable pageable) {
		if (pageable == null) {
			return PageRequest.of(0, 20, Sort.by("id").ascending());
		}
		if (pageable.getPageSize() <= MAX_PAGE_SIZE) {
			return pageable;
		}
		return PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE, pageable.getSort());
	}
}
