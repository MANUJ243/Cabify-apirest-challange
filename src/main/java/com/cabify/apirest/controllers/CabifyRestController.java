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
		
		//doy opcion a que el id pueda venir en mayusculas o minusculas
		if (ID != null) {
			idRecibido = ID;
		} else if(id != null) {
			idRecibido = id;
		} else {
		    //en este caso no se ha informado bien el id
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		try {
		    //intento localizar en db el grupo correspondiente al id
			grupo = grupoService.findById(idRecibido).get();
			
			if (grupo.getCoche() == null) {
			    //el grupo todavia no tiene un coche asociado
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			} else {
			    //devuelvo el id del coche asociado al grupo
				return new ResponseEntity(grupo.getCoche().getId(), HttpStatus.OK);
			}
		} catch (Exception e) {
		    //no existe el grupo para ese id
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/dropoff")
	public ResponseEntity finalizarTrayecto(Long ID, Long id) {
		Long idRecibido;
		Grupo grupo;
		
		//doy opcion a que el id pueda venir en mayusculas o minusculas
		if (ID != null) {
			idRecibido = ID;
		} else if(id != null) {
			idRecibido = id;
		} else {
		    //en este caso no se ha informado bien el id
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		try {
		    //intento borrar el grupo de la db
			grupo = grupoService.findById(idRecibido).get();
			grupoService.delete(grupo);
		} catch (Exception e) {
		    //no existe un grupo registrado con ese id
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		//dado que el flujo llega aqui significa que ha terminado un viaje
		//por lo que compruebo si puedo aceptar otro viaje
		intentarAsignarGrupo();
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@PostMapping("/journey")
	public void anadirModificarGrupo(@RequestBody Grupo grupo) {
	    //anado el grupo e intento aceptar un viaje
		grupoService.save(grupo);
		intentarAsignarGrupo();
	}
	
	public void intentarAsignarGrupo() {
	    //cojo de la db la lista de grupos registrados
		Iterable<Grupo> grupos = grupoService.findAll();
		ArrayList<Grupo> gruposSinCoche = new ArrayList<>();
		
		//relleno lista con aquellos que no tengan un coche asociado
		for (Grupo grupo: grupos) {
			if (grupo.getCoche() == null) {
				gruposSinCoche.add(grupo);
			}
		}
		
		//ordeno esa lista por orden de registro de cada grupo para mantener
		//el orden de peticion
		Collections.sort(gruposSinCoche, new Comparator<Grupo>() {
		    @Override
		    public int compare(Grupo g1, Grupo g2) {
		        return g1.getFechaPeticion().compareTo(g2.getFechaPeticion());
		    }
		});
		
		//por cada grupo sin coche y en el orden correcto
		for (Grupo grupo: gruposSinCoche) {
		    //intento asociar el mejor coche para cada grupo en concreto
			Coche coche = getMejorCoche(grupo);
			
			if (coche != null) {
			    //si encuentro un coche lo asocio al grupo
				grupo.setCoche(coche);
				grupoService.save(grupo);
				return;
			}
		}
	}
	
	//este metodo se encarga de encontrar el mejor coche con espacio para un grupo
	public Coche getMejorCoche(Grupo grupo) {
		Iterable<Coche> coches = cocheService.findAll();
		Coche mejorCoche = null;
		
		//por cada coche miro que tenga espacio para el grupo
		//y selecciono aquel coche en el que metiendo el grupo, quede el menor
		//menor espacio libre posible
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
