package com.github.raonigabriel.imdb.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.github.raonigabriel.imdb.model")
@EnableJpaRepositories(basePackages = "com.github.raonigabriel.imdb.repository")
@EnableJpaAuditing
public class JpaConfig {

}