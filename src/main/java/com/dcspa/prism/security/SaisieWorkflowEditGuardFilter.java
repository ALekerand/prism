package com.dcspa.prism.security;

import com.dcspa.prism.service.SaisieWorkflowService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class SaisieWorkflowEditGuardFilter extends OncePerRequestFilter {

	private final SaisieWorkflowService workflowService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		RecordRef ref = editableRecordRef(request);
		if (ref == null) {
			filterChain.doFilter(request, response);
			return;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AuthUser user = auth != null && auth.getPrincipal() instanceof AuthUser au ? au : null;
		if (!workflowService.isEditable(ref.resourcePath(), ref.recordId(), user)) {
			writeBlocked(response);
			return;
		}
		filterChain.doFilter(request, response);
	}

	private static void writeBlocked(HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter()
				.write("{\"message\":\"Modification impossible : la donnée est soumise ou validée.\"}");
	}

	private RecordRef editableRecordRef(HttpServletRequest request) {
		String method = request.getMethod();
		if (!"PUT".equalsIgnoreCase(method) && !"DELETE".equalsIgnoreCase(method)) {
			return null;
		}
		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
			path = path.substring(contextPath.length());
		}
		if (!path.startsWith("/api/")
				|| path.startsWith("/api/saisie-workflows/")
				|| path.startsWith("/api/app-users/")) {
			return null;
		}
		String[] parts = path.split("/");
		if (parts.length < 4) {
			return null;
		}
		String idSegment = parts[parts.length - 1];
		Integer id = parsePositiveInteger(idSegment);
		if (id == null) {
			return null;
		}
		String resourcePath = path.substring(0, path.length() - idSegment.length() - 1);
		return new RecordRef(resourcePath, id);
	}

	private Integer parsePositiveInteger(String value) {
		try {
			int parsed = Integer.parseInt(value);
			return parsed > 0 ? parsed : null;
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	private record RecordRef(String resourcePath, Integer recordId) {
	}
}
