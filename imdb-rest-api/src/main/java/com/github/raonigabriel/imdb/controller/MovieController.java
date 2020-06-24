package com.github.raonigabriel.imdb.controller;

import java.math.BigDecimal;
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

import com.github.raonigabriel.imdb.model.Movie;
import com.github.raonigabriel.imdb.repository.MovieRepository;

@RestController
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	private MovieRepository repo;

	@Transactional
	@PostConstruct
	private void populate() {
		
		repo.deleteAllInBatch();

		Movie movie  = new Movie();
		movie.setId("tt7286456");
		movie.setName("Joker");
		movie.setRating(new BigDecimal("8.5"));
		movie.setReleaseDate(LocalDate.of(2019, 10, 4));
		repo.save(movie);

		movie = new Movie();
		movie.setId("mo132434");
		movie.setName("Hero");
		movie.setReleaseDate(LocalDate.of(1964, 11, 9));
		movie.setRating(new BigDecimal("4.1"));
		movie.setDuration(115);
		repo.save(movie);

		movie = new Movie();
		movie.setId("mo024343");
		movie.setName("Charg");
		movie.setReleaseDate(LocalDate.of(1935, 04, 11));
		movie.setRating(new BigDecimal("3.9"));
		movie.setDuration(140);
		repo.save(movie);
	}

	@GetMapping
	public Collection<Movie> getAll() {
		return repo.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Movie> getOne(@PathVariable String id) {
		Optional<Movie> movie = repo.findById(id);
		return movie.isPresent() ? ResponseEntity.ok(movie.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteOne(@PathVariable String id) {
		repo.deleteById(id);
	}

	@PostMapping(path = "/{id}")
	public ResponseEntity<Movie> postMovie(@PathVariable String id, @RequestBody Movie movie) {
		if (repo.existsById(id)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();	
		}

		movie.setId(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(movie));
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Movie> putMovie(@PathVariable String id, @RequestBody Movie movie) {

		Optional<Movie> oldMovie = repo.findById(id);

		if (!oldMovie.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();	
		}

		if (!StringUtils.isEmpty(movie.getName())) {
			oldMovie.get().setName(movie.getName());
		}

		if (movie.getDuration() != null) {
			oldMovie.get().setDuration(movie.getDuration());
		}

		if (movie.getRating() != null) {
			oldMovie.get().setRating(movie.getRating());
		}

		if (movie.getReleaseDate() != null) {
			oldMovie.get().setReleaseDate(movie.getReleaseDate());
		}

		return ResponseEntity.status(HttpStatus.OK).body(repo.save(oldMovie.get()));
	}

}