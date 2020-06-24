package com.github.raonigabriel.imdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.raonigabriel.imdb.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, String> {

}