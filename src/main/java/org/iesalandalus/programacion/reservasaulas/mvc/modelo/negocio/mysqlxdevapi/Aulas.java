package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.utilidades.SesionBD;

import com.mysql.cj.xdevapi.Schema;

public class Aulas implements IAulas {
	
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
	public List<Aula> get() {
		return new ArrayList<>();
	}

	@Override
	public int getTamano() {
		return 0;
	}

	@Override
	public void insertar(Aula aula) throws OperationNotSupportedException {
		
	}

	@Override
	public Aula buscar(Aula aula) {
		return null;
	}

	@Override
	public void borrar(Aula aula) throws OperationNotSupportedException {
	}


}
