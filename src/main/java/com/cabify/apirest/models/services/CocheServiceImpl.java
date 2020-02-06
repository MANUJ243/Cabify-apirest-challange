package com.cabify.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cabify.apirest.models.dao.ICocheDao;
import com.cabify.apirest.models.entity.Coche;

@Service
public class CocheServiceImpl  implements ICocheService{

	@Autowired
	private ICocheDao cocheDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Coche> findAll() {
		return (List<Coche>) cocheDao.findAll();
	}

	@Override
	public void saveAll(List<Coche> coches) {
		cocheDao.saveAll(coches);
	}

	@Override
	public void deleteAll() {
		cocheDao.deleteAll();
	}

	@Override
	public void save(Coche coche) {
		cocheDao.save(coche);
	}
}
