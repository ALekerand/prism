package com.dcspa.prism;

import com.dcspa.prism.repositorybase.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
		basePackages = "com.dcspa.prism.repository",
		repositoryBaseClass = BaseRepositoryImpl.class
)
public class PrismApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrismApplication.class, args);
	}

}
