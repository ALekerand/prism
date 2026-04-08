package com.dcspa.prism.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.data.web.ReactiveSortHandlerMethodArgumentResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

/**
 * En stack WebFlux (sans spring-webmvc), {@link org.springframework.data.domain.Pageable}
 * et {@link org.springframework.data.domain.Sort} ne sont pas résolus sans ces résolveurs réactifs.
 */
@Configuration
public class WebFluxSpringDataWebConfig implements WebFluxConfigurer {

	@Override
	public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
		ReactiveSortHandlerMethodArgumentResolver sortResolver = new ReactiveSortHandlerMethodArgumentResolver();
		configurer.addCustomResolver(sortResolver);
		configurer.addCustomResolver(new ReactivePageableHandlerMethodArgumentResolver(sortResolver));
	}
}
