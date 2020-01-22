package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;

public class Aulas {

	private List<Aula> coleccionAulas;
	
	public Aulas() {
		coleccionAulas = new ArrayList<>();
	}
	
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
	
	public int getTamano() {
		return coleccionAulas.size();
	}
	
	
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
