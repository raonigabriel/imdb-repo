package com.github.raonigabriel.imdb.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "MOVIE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie implements Serializable, Externalizable {

	private static final long serialVersionUID = 1L;
	public static final long VERSION = 1L;	

	@Id
	@NotEmpty
	@Size(max = 10)
	@Column(name = "ID", nullable = false, length = 10)
	private String id;

	@NotEmpty
	@Size(max = 100)
	@Column(name =  "NAME", nullable = false, length = 100)
	private String name;

	@NotNull
	@JsonProperty("release_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name =  "RELEASE_DATE", nullable = false)
	private LocalDate releaseDate;

	@NotNull
	@DecimalMin(value = "0")
	@DecimalMax(value = "5")
	@Digits(integer = 1, fraction = 1)
	@Column(name = "RATING", nullable = false, precision = 2, scale = 1)
	private BigDecimal rating;

	@NotNull
	@PositiveOrZero
	@Column(name = "DURATION")
	private Integer duration;

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

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(id);
		out.writeUTF(name);
		out.writeUTF(releaseDate.toString());
		out.writeUTF(rating.toString());
		out.writeInt(duration);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		id = in.readUTF();
		name = in.readUTF();
		releaseDate = LocalDate.parse(in.readUTF());
		rating = new BigDecimal(in.readUTF());
		duration = in.readInt();
	}

}	
