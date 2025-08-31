package com.accenture.assessment.dto;

import java.util.List;
import org.springframework.data.domain.Page;

/**
 * DTO para exibição condensada das informações de paginação
 * 
 * @param <T> O tipo do conteúdo da página.
 */
public class PageResponseDTO<T> {

	private final List<T> content;
	private final int pageNumber;
	private final int pageSize;
	private final long totalElements;
	private final int totalPages;

	// Constructor
	public PageResponseDTO(Page<T> page) {
     this.content = page.getContent();
     this.pageNumber = page.getNumber();
     this.pageSize = page.getSize();
     this.totalElements = page.getTotalElements();
     this.totalPages = page.getTotalPages();
 }

	// Getters
	public List<T> getContent() {
		return content;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}
}
