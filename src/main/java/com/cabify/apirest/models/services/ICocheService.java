package com.cabify.apirest.models.services;

import java.util.List;

import com.cabify.apirest.models.entity.Coche;

public interface ICocheService {

	public List<Coche> findAll();
	
	public void save(Coche coche);
	
	public void saveAll(List<Coche> coches);
	
	public void deleteAll();
}
