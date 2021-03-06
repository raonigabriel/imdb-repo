package com.github.raonigabriel.imdb.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "ACTOR")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Actor implements Serializable, Externalizable {

	private static final long serialVersionUID = 1L;
	public static final long VERSION = 2L;

	@Id
	@NotEmpty
	@Size(max = 10)
	@Column(name = "ID", nullable = false, length = 10)
	private String id;

	@NotEmpty
	@Size(max = 100)
	@Column(name = "NAME", nullable = false, length = 100)
	private	String name;

	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("birth_date")
	@PastOrPresent
	@Column(name = "BIRTH_DATE", nullable = false)
	private	LocalDate birthDate;

	@Positive
	@Column(name = "HEIGHT", nullable = true)
	private Integer height; // In cm

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(id);
		out.writeUTF(name);
		out.writeUTF(birthDate.toString());
		if (VERSION > 1) {
			out.writeInt(height != null ? height : Integer.MIN_VALUE);
		}

	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		id = in.readUTF();
		name = in.readUTF();
		birthDate = LocalDate.parse(in.readUTF());
		
		int auxInteger;
		if (VERSION > 1) {
			auxInteger = in.readInt();
			height = auxInteger != Integer.MIN_VALUE ? auxInteger : null;
		}
	}

}
