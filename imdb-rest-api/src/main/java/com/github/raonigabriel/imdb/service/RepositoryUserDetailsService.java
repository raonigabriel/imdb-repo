package com.github.raonigabriel.imdb.service;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.raonigabriel.imdb.model.User;
import com.github.raonigabriel.imdb.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class RepositoryUserDetailsService implements ReactiveUserDetailsService {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	@PostConstruct
	public void populate() {
		
		repo.deleteAllInBatch();

		User user = new User();
		user.setName("Scott");
		user.setPassword(passwordEncoder.encode("TIGER"));
		repo.save(user);
	}

	public class UserPrincipal implements UserDetails {

		private static final long serialVersionUID = 1L;

		private User user;

		public UserPrincipal(User user) {
			this.user = user;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return Collections.singletonList(new SimpleGrantedAuthority("USER"));
		}

		@Override
		public String getPassword() {
			return user.getPassword();
		}

		@Override
		public String getUsername() {
			return user.getName();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return Mono.justOrEmpty(repo.findByName(username).map(UserPrincipal::new));
	}
}