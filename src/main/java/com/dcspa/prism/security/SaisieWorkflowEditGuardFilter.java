package com.dcspa.prism.security;

import com.dcspa.prism.service.SaisieWorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SaisieWorkflowEditGuardFilter implements WebFilter {
	private final SaisieWorkflowService workflowService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		RecordRef ref = editableRecordRef(request);
		if (ref == null) {
			return chain.filter(exchange);
		}
		return ReactiveSecurityContextHolder.getContext()
				.flatMap(ctx -> {
					Authentication auth = ctx.getAuthentication();
					AuthUser user = auth != null && auth.getPrincipal() instanceof AuthUser au ? au : null;
					if (workflowService.isEditable(ref.resourcePath(), ref.recordId(), user)) {
						return chain.filter(exchange);
					}
					return writeBlocked(exchange);
				})
				.switchIfEmpty(Mono.defer(() -> {
					if (workflowService.isEditable(ref.resourcePath(), ref.recordId(), null)) {
						return chain.filter(exchange);
					}
					return writeBlocked(exchange);
				}));
	}

	private static Mono<Void> writeBlocked(ServerWebExchange exchange) {
		exchange.getResponse().setRawStatusCode(400);
		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
		byte[] bytes = "{\"message\":\"Modification impossible : la donnée est soumise ou validée.\"}".getBytes();
		return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
	}

	private RecordRef editableRecordRef(ServerHttpRequest request) {
		String method = request.getMethod().name();
		if (!"PUT".equalsIgnoreCase(method) && !"DELETE".equalsIgnoreCase(method)) {
			return null;
		}
		String path = request.getPath().pathWithinApplication().value();
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
