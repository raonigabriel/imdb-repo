package com.github.raonigabriel.imdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.raonigabriel.imdb.model.Actor;

public interface ActorRepository extends JpaRepository<Actor, String>{

}