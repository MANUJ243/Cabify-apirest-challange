package com.cabify.apirest.models.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cabify.apirest.models.dao.IGrupoDao;
import com.cabify.apirest.models.entity.Grupo;

@Service
public class GrupoServiceImpl implements IGrupoService{

	@Autowired
	private IGrupoDao grupoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Grupo> findAll() {
		return (List<Grupo>) grupoDao.findAll();
	}

	@Override
	public void save(Grupo grupo) {
		grupoDao.save(grupo);
	}

	@Override
	public void delete(Grupo grupo) {
		grupoDao.delete(grupo);
	}

	@Override
	public Optional<Grupo> findById(Long id) {
		return grupoDao.findById(id);
	}

	@Override
	public void deleteAll() {
		grupoDao.deleteAll();
	}
}
