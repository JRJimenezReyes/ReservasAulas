package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.utilidades.SesionBD;

import com.mysql.cj.xdevapi.Schema;

public class Profesores implements IProfesores {
	
	private Schema esquemaBD = null;

	@Override
	public void comenzar() {
		esquemaBD = SesionBD.getEsquema();	
	}

	@Override
	public void terminar() {
		SesionBD.cierraSesion();
	}

	@Override
	public List<Profesor> get() {
		return new ArrayList<>();
	}

	@Override
	public int getTamano() {
		return 0;
	}

	@Override
	public void insertar(Profesor profesor) throws OperationNotSupportedException {
	}

	@Override
	public Profesor buscar(Profesor profesor) {
		return null;
	}

	@Override
	public void borrar(Profesor profesor) throws OperationNotSupportedException {
	}

}
