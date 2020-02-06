package com.cabify.apirest.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "coches")
public class Coche implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_coche")
	private Long id;

	@Column(name = "seats")
	private Long seats;
	
	@OneToMany(mappedBy = "coche")
	private List<Grupo> grupos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSeats() {
		return seats;
	}

	public void setSeats(Long seats) {
		this.seats = seats;
	}
	
	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}
}
