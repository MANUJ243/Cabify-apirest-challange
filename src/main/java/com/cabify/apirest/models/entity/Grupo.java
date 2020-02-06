package com.cabify.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "grupos")
public class Grupo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_grupo")
	private Long id;

	@Column(name = "people")
	private Long people;

	@ManyToOne
	@JoinColumn(name = "id_coche")
	private Coche coche;

	@Column(name = "fecha_peticion")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaPeticion;
	
	@PrePersist
	public void prePersist() {
		fechaPeticion = new Date();
	}

	public Date getFechaPeticion() {
		return fechaPeticion;
	}

	public void setFechaPeticion(Date fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPeople() {
		return people;
	}

	public void setPeople(Long people) {
		this.people = people;
	}

	public Coche getCoche() {
		return coche;
	}

	public void setCoche(Coche coche) {
		this.coche = coche;
	}
}
