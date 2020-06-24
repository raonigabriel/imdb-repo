package com.github.raonigabriel.imdb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.github.raonigabriel.imdb.service.RepositoryUserDetailsService;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ReactiveWebSecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ReactiveAuthenticationManager authenticationManager(@Autowired RepositoryUserDetailsService userService) {
		return new UserDetailsRepositoryReactiveAuthenticationManager(userService);
	}

	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		return http.authorizeExchange()
				.pathMatchers(HttpMethod.GET, "/", "/favicon.ico", "/index.html", "/app.js", "/app.css", "/webjars/**").permitAll()
				.pathMatchers(HttpMethod.GET, "/actors").permitAll()
				.pathMatchers(HttpMethod.GET, "/movies").permitAll()
				.anyExchange().authenticated()
				.and().httpBasic().and().build();
	}

}