package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;

public class Aulas implements IAulas {

	private List<Aula> coleccionAulas;
	
	public Aulas() {
		coleccionAulas = new ArrayList<>();
	}
	
	@Override
	public List<Aula> get() {
		List<Aula> aulasOrdenadas = copiaProfundaAulas();
		aulasOrdenadas.sort(Comparator.comparing(Aula::getNombre));
		return aulasOrdenadas;
	}
	
	private List<Aula> copiaProfundaAulas() {
		List<Aula> copiaAulas = new ArrayList<>();
		for (Aula aula : coleccionAulas) {
			copiaAulas.add(new Aula(aula));
		}
		return copiaAulas;
	}
	
	@Override
	public int getTamano() {
		return coleccionAulas.size();
	}
	
	
	@Override
	public void insertar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede insertar un aula nula.");
		}
		int indice = coleccionAulas.indexOf(aula);
		if (indice == -1) {
			coleccionAulas.add(new Aula(aula));
		} else {
			throw new OperationNotSupportedException("ERROR: Ya existe un aula con ese nombre.");
		}		
		
	}
	
	@Override
	public Aula buscar(Aula aula) {
		if (aula == null) {
			throw new IllegalArgumentException("ERROR: No se puede buscar un aula nula.");
		}
		int indice = coleccionAulas.indexOf(aula);
		if (indice == -1) {
			return null;
		} else {
			return new Aula(coleccionAulas.get(indice));
		}
	}
	
	@Override
	public void borrar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new IllegalArgumentException("ERROR: No se puede borrar un aula nula.");
		}
		int indice = coleccionAulas.indexOf(aula);
		if (indice == -1) {
			throw new OperationNotSupportedException("ERROR: No existe ningún aula con ese nombre.");
		} else {
			coleccionAulas.remove(indice);
		}
	}

}
