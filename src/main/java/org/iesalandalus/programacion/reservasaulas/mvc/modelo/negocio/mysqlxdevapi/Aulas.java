package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.utilidades.SesionBD;

import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.xdevapi.Collection;
import com.mysql.cj.xdevapi.DocResult;

public class Aulas implements IAulas {
	
	private static final String COLECCION = "AulasX";
	private static final String NOMBRE = "nombre";
	private static final String BUSQUEDA = "nombre = :nombre";
	
	private Collection coleccionAulas = null;

	@Override
	public void comenzar() {
		try {
			coleccionAulas = SesionBD.getEsquema().getCollection(COLECCION, true);
		} catch (WrongArgumentException e) {
			SesionBD.getEsquema().createCollection(COLECCION);
		}	
	}

	@Override
	public void terminar() {
		SesionBD.cerrarSesion();
	}

	@Override
	public List<Aula> get() {
		List<Aula> aulas = new ArrayList<>();
		DocResult documentos = coleccionAulas.find().orderBy(NOMBRE).execute();
		while (documentos.hasNext()) {
			aulas.add(SesionBD.getAula(documentos.next()));
		}
		return aulas;
	}

	@Override
	public int getTamano() {
		return (int)coleccionAulas.count();
	}

	@Override
	public void insertar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede insertar un aula nula.");
		}
		if (buscar(aula) == null) {
			coleccionAulas.add(SesionBD.getDocumento(aula)).execute();
		} else {
			throw new OperationNotSupportedException("ERROR: Ya existe un aula con ese nombre.");
		}
	}

	@Override
	public Aula buscar(Aula aula) {
		DocResult documentos = coleccionAulas.find(BUSQUEDA).bind(NOMBRE, aula.getNombre()).execute();
		return documentos.hasNext() ? SesionBD.getAula(documentos.next()) : null;
	}

	@Override
	public void borrar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new IllegalArgumentException("ERROR: No se puede borrar un aula nula.");
		}
		if (buscar(aula) != null) {
			coleccionAulas.remove(BUSQUEDA).bind(NOMBRE, aula.getNombre()).execute();
		} else {
			throw new OperationNotSupportedException("ERROR: No existe ningún aula con ese nombre.");
		}
	}

}
