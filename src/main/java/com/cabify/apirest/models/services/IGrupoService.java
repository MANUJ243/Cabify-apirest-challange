package com.cabify.apirest.models.services;

import java.util.List;
import java.util.Optional;

import com.cabify.apirest.models.entity.Grupo;

public interface IGrupoService {
	
	public List<Grupo> findAll();
	
	public void save(Grupo grupo);
	
	public void delete(Grupo grupo);
	
	public Optional<Grupo> findById(Long id);
	
	public void deleteAll();
}
