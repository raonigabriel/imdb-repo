package com.github.raonigabriel.imdb.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import com.github.raonigabriel.imdb.model.Actor;

public class ActorTest {

	@Test
	public void givenNonEmptyActorShouldSerializeAndDeserialize() {
		assertDoesNotThrow(() -> {
			Actor actor = new Actor();
			actor.setId("jd00001");
			actor.setName("John Doe");
			actor.setBirthDate(LocalDate.of(1970, 12, 25));
			actor.setHeight(170);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ObjectOutput out = new ObjectOutputStream(baos)) {
				out.writeObject(actor);
			}
			assertEquals(99, baos.toByteArray().length);
			try (ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
				actor = (Actor) in.readObject();
			}
			assertNotNull(actor);
			assertEquals("jd00001", actor.getId());
			assertEquals("John Doe", actor.getName());
			assertEquals(LocalDate.of(1970, 12, 25), actor.getBirthDate());
			assertEquals(170, actor.getHeight());
		});
	}

}
