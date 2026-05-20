package com.dcspa.prism;

import com.dcspa.prism.config.AppCorsProperties;
import com.dcspa.prism.config.LangueCatalogueProperties;
import com.dcspa.prism.repositorybase.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties({ AppCorsProperties.class, LangueCatalogueProperties.class })
@EnableJpaRepositories(
		basePackages = "com.dcspa.prism",
		repositoryBaseClass = BaseRepositoryImpl.class
)
public class PrismApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PrismApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(PrismApplication.class, args);
	}

}
