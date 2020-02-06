package com.cabify.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.cabify.apirest.models.entity.Coche;

public interface ICocheDao extends CrudRepository<Coche, Long>{

}
