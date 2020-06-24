package com.github.raonigabriel.imdb.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.raonigabriel.imdb.model.Actor;
import com.github.raonigabriel.imdb.repository.ActorRepository;

@RestController
@RequestMapping("/actors")
public class ActorController {

	@Autowired
	private ActorRepository repo;

	@Transactional
	@PostConstruct
	private void populate() {
		
		repo.deleteAllInBatch();
		
		Actor actor = new Actor();
		actor.setId("nm0001191");
		actor.setName("Adam Sandler");
		actor.setBirthDate(LocalDate.of(1966, 9, 9));
		repo.save(actor);

		actor = new Actor();
		actor.setId("nm0001618");
		actor.setName("Joaquin Phoenix");
		actor.setBirthDate(LocalDate.of(1974, 10, 28));
		repo.save(actor);

		actor = new Actor();
		actor.setId("nm0000375");
		actor.setName("Robert Downey Jr");
		actor.setBirthDate(LocalDate.of(1965, 4, 4));
		repo.save(actor);
	}

	@GetMapping
	public Collection<Actor> getAll() {
		return repo.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Actor> getOne(@PathVariable String id) {
		Optional<Actor> actor = repo.findById(id);
		return actor.isPresent() ? ResponseEntity.ok(actor.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteOne(@PathVariable String id) {
		repo.deleteById(id);
	}

	@PostMapping(path = "/{id}")
	public ResponseEntity<Actor> postActor(@PathVariable String id, @RequestBody Actor actor) {
		if (repo.existsById(id)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();	
		}

		actor.setId(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(actor));
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Actor> putActor(@PathVariable String id, @RequestBody Actor actor) {
		
		Optional<Actor> oldActor = repo.findById(id);
		
		if (!oldActor.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();	
		}

		if (!StringUtils.isEmpty(actor.getName())) {
			oldActor.get().setName(actor.getName());
		}

		if (actor.getBirthDate() != null) {
			oldActor.get().setBirthDate(actor.getBirthDate());
		}

		return ResponseEntity.status(HttpStatus.OK).body(repo.save(oldActor.get()));
	}

}
