package com.cabify.apirest.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cabify.apirest.models.entity.Coche;
import com.cabify.apirest.models.entity.Grupo;
import com.cabify.apirest.models.services.ICocheService;
import com.cabify.apirest.models.services.IGrupoService;

@RestController
public class CabifyRestController {
	
	@Autowired
	private IGrupoService grupoService;
	
	@Autowired
	private ICocheService cocheService;
	
	@PutMapping("/cars")
	public void anadirCoches(@RequestBody List<Coche> coches) {
		grupoService.deleteAll();
		cocheService.deleteAll();
		cocheService.saveAll(coches);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/status")
	public ResponseEntity sendViaResponseEntity() {
	    return new ResponseEntity(HttpStatus.OK);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/locate")
	public ResponseEntity localizar(Long ID, Long id) {
		Long idRecibido;
		Grupo grupo;
		
		if (ID != null) {
			idRecibido = ID;
		} else if(id != null) {
			idRecibido = id;
		} else {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		try {
			grupo = grupoService.findById(idRecibido).get();
			
			if (grupo.getCoche() == null) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity(grupo.getCoche().getId(), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/dropoff")
	public ResponseEntity finalizarTrayecto(Long ID, Long id) {
		Long idRecibido;
		Grupo grupo;
		
		if (ID != null) {
			idRecibido = ID;
		} else if(id != null) {
			idRecibido = id;
		} else {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		try {
			grupo = grupoService.findById(idRecibido).get();
			grupoService.delete(grupo);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		intentarAsignarGrupo();
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@PostMapping("/journey")
	public void anadirModificarGrupo(@RequestBody Grupo grupo) {
		grupoService.save(grupo);
		intentarAsignarGrupo();
	}
	
	public void intentarAsignarGrupo() {
		Iterable<Grupo> grupos = grupoService.findAll();
		ArrayList<Grupo> gruposSinCoche = new ArrayList<>();
		
		for (Grupo grupo: grupos) {
			if (grupo.getCoche() == null) {
				gruposSinCoche.add(grupo);
			}
		}
		
		Collections.sort(gruposSinCoche, new Comparator<Grupo>() {
		    @Override
		    public int compare(Grupo g1, Grupo g2) {
		        return g1.getFechaPeticion().compareTo(g2.getFechaPeticion());
		    }
		});
		
		for (Grupo grupo: gruposSinCoche) {
			Coche coche = getMejorCoche(grupo);
			
			if (coche != null) {
				grupo.setCoche(coche);
				grupoService.save(grupo);
				return;
			}
		}
	}
	
	public Coche getMejorCoche(Grupo grupo) {
		Iterable<Coche> coches = cocheService.findAll();
		Coche mejorCoche = null;
		
		for(Coche coche: coches) {
			int espacioOcupado = 0;
			int espacioLibre = 0;
			
			for (Grupo grupoCoche: coche.getGrupos()) {
				espacioOcupado += grupoCoche.getPeople();
			}
			
			espacioLibre = (int) (coche.getSeats() - espacioOcupado);
			
			if (espacioLibre >= grupo.getPeople()) {
				if (mejorCoche == null) {
					mejorCoche = coche;
				} else {
					int espacioOcupadoMejorCoche = 0;
					
					for (Grupo grupoCoche: mejorCoche.getGrupos()) {
						espacioOcupadoMejorCoche += grupoCoche.getPeople();
					}
					
					if (espacioLibre < (mejorCoche.getSeats() - espacioOcupadoMejorCoche)) {
						mejorCoche = coche;
					}
				}
			}
		}
		
		return mejorCoche;
	}
	
}
