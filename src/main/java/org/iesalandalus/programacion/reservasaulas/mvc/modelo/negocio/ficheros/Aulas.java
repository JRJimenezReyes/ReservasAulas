package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;

public class Aulas implements IAulas {
	
	private static final String NOMBRE_FICHERO_AULAS = "datos/aulas.dat";

	private List<Aula> coleccionAulas;
	
	public Aulas() {
		coleccionAulas = new ArrayList<>();
	}
	
	@Override
	public void comenzar() {
		leer();
	}
	
	private void leer() {
		File ficheroAulas = new File(NOMBRE_FICHERO_AULAS);
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ficheroAulas))) {
			Aula aula = null;
			do {
				aula = (Aula) entrada.readObject();
				insertar(aula);
			} while (aula != null);
		} catch (ClassNotFoundException e) {
			System.out.println("No puedo encontrar la clase que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo abrir el fihero de aulas.");
		} catch (EOFException e) {
			System.out.println("Fichero aulas leído satisfactoriamente.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida.");
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void terminar() {
		escribir();
	}
	
	private void escribir() {
		File ficheroAulas = new File(NOMBRE_FICHERO_AULAS);
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ficheroAulas))){
			for (Aula aula : coleccionAulas)
				salida.writeObject(aula);
			System.out.println("Fichero aulas escrito satisfactoriamente.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo crear el fichero de aulas.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida.");
		}
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
