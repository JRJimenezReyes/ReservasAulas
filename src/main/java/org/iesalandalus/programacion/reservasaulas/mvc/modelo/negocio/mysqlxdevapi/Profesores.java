package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.utilidades.SesionBD;

import com.mysql.cj.protocol.x.XProtocolError;
import com.mysql.cj.xdevapi.Result;
import com.mysql.cj.xdevapi.Row;
import com.mysql.cj.xdevapi.RowResult;
import com.mysql.cj.xdevapi.Table;

public class Profesores implements IProfesores {
	
	private static final String TABLA_PROFESORES = "Profesores";
	private static final String ID_PROFESOR = "idProfesor";
	private static final String NOMBRE = "nombre";
	private static final String CORREO = "correo";
	private static final String TELEFONO = "telefono";
	
	private Table tablaProfesores = null;

	@Override
	public void comenzar() {
		tablaProfesores = SesionBD.getEsquema().getTable(TABLA_PROFESORES);
	}

	@Override
	public void terminar() {
		SesionBD.cierraSesion();
	}

	@Override
	public List<Profesor> get() {
		RowResult filas = tablaProfesores.select("*").orderBy(CORREO).execute();
		filas.fetchAll();
		List<Profesor> profesores = new ArrayList<>();
		for (Row fila : filas) {
			Profesor profesor = getProfesorDesdeFila(fila);
			profesores.add(profesor);
		}
		return profesores;
	}
	
	private Profesor getProfesorDesdeFila(Row fila) {
		String nombre = fila.getString(NOMBRE);
		String correo = fila.getString(CORREO);
		String telefono = fila.getString(TELEFONO);
		return new Profesor(nombre, correo, telefono);
	}

	@Override
	public int getTamano() {
		return (int)tablaProfesores.count();
	}

	@Override
	public void insertar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede insertar un profesor nulo.");
		}
		try {
			tablaProfesores.insert(ID_PROFESOR, NOMBRE, CORREO, TELEFONO)
			.values(null, profesor.getNombre(), profesor.getCorreo(), profesor.getTelefono()).execute();
		} catch (XProtocolError e) {
			throw new OperationNotSupportedException("ERROR: Ya existe un profesor con ese correo.");
		}
	}

	@Override
	public Profesor buscar(Profesor profesor) {
		if (profesor == null) {
			throw new IllegalArgumentException("ERROR: No se puede buscar un profesor nulo.");
		}
		RowResult filas = tablaProfesores.select("*")
				.where("correo = :correo").bind(CORREO, profesor.getCorreo()).execute();
		Row fila = filas.fetchOne();
		if (fila != null) {
			profesor = getProfesorDesdeFila(fila);
		}
		return profesor;
	}

	@Override
	public void borrar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new IllegalArgumentException("ERROR: No se puede borrar un profesor nulo.");
		}
		try {
			Result borrados = tablaProfesores.delete()
					.where("correo = :correo").bind(CORREO, profesor.getCorreo()).execute();
			if (borrados.getAffectedItemsCount() == 0)
				throw new OperationNotSupportedException("ERROR: No existe ningún profesor con ese correo.");
		} catch (XProtocolError e) {
			throw new OperationNotSupportedException("ERROR: " + e.getMessage());
		}
	}

}
