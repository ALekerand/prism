package com.dcspa.prism.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * WebFlux utilise par défaut son propre pipeline Jackson (Jackson 3 / {@code tools.jackson}) qui ne
 * récupère pas toujours les {@link com.fasterxml.jackson.databind.Module} enregistrés pour MVC.
 * On aligne encodeur/décodeur réactifs sur le {@link ObjectMapper} auto-configuré par Spring Boot,
 * celui qui inclut {@link JacksonEntityConfig#entityFormatBModule()}.
 */
@Configuration
public class WebFluxJacksonUnifiedConfig implements WebFluxConfigurer {

	private final ObjectMapper objectMapper;

	public WebFluxJacksonUnifiedConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
		configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
	}
}
