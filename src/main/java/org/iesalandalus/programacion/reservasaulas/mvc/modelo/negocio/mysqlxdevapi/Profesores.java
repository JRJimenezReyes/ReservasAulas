package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.utilidades.SesionBD;

import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.xdevapi.Collection;
import com.mysql.cj.xdevapi.DocResult;

public class Profesores implements IProfesores {
	
	private static final String COLECCION = "ProfesoresX";
	private static final String CORREO = "correo";
	private static final String BUSQUEDA = "correo = :correo";
	
	private Collection coleccionProfesores = null;

	@Override
	public void comenzar() {
		try {
			coleccionProfesores = SesionBD.getEsquema().getCollection(COLECCION, true);
		} catch (WrongArgumentException e) {
			SesionBD.getEsquema().createCollection(COLECCION);
		}
	}

	@Override
	public void terminar() {
		SesionBD.cerrarSesion();
	}

	@Override
	public List<Profesor> get() {
		List<Profesor> profesores = new ArrayList<>();
		DocResult documentos = coleccionProfesores.find().orderBy(CORREO).execute();
		while (documentos.hasNext()) {
			profesores.add(SesionBD.getProfesor(documentos.next()));
		}
		return profesores;
	}

	@Override
	public int getTamano() {
		return (int)coleccionProfesores.count();
	}

	@Override
	public void insertar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede insertar un profesor nulo.");
		}
		if (buscar(profesor) == null) {
			coleccionProfesores.add(SesionBD.getDocumento(profesor)).execute();
		} else {
			throw new OperationNotSupportedException("ERROR: Ya existe un profesor con ese correo.");
		}
	}

	@Override
	public Profesor buscar(Profesor profesor) {
		DocResult documentos = coleccionProfesores.find(BUSQUEDA).bind(CORREO, profesor.getCorreo()).execute();
		return documentos.hasNext() ? SesionBD.getProfesor(documentos.next()) : null;
	}

	@Override
	public void borrar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new IllegalArgumentException("ERROR: No se puede borrar un aula nulo.");
		}
		if (buscar(profesor) != null) {
			coleccionProfesores.remove(BUSQUEDA).bind(CORREO, profesor.getCorreo()).execute();
		} else {
			throw new OperationNotSupportedException("ERROR: No existe ningún profesor con ese correo.");
		}
	}

}
