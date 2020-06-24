package com.github.raonigabriel.imdb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.raonigabriel.imdb.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByName(String name);

}