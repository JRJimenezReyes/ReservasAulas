package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.utilidades.SesionBD;

import com.mysql.cj.protocol.x.XProtocolError;
import com.mysql.cj.xdevapi.Result;
import com.mysql.cj.xdevapi.Row;
import com.mysql.cj.xdevapi.RowResult;
import com.mysql.cj.xdevapi.Table;

public class Aulas implements IAulas {
	
	private static final String TABLA_AULAS = "Aulas";
	private static final String ID_AULA = "idAula";
	private static final String NOMBRE = "nombre";
	private static final String PUESTOS = "puestos";
	
	private Table tablaAulas = null;

	@Override
	public void comenzar() {
		tablaAulas = SesionBD.getEsquema().getTable(TABLA_AULAS);
	}

	@Override
	public void terminar() {
		SesionBD.cierraSesion();
	}

	@Override
	public List<Aula> get() {
		RowResult filas = tablaAulas.select("*").orderBy(NOMBRE).execute();
		filas.fetchAll();
		List<Aula> aulas = new ArrayList<>();
		for (Row fila : filas) {
			Aula aula = getAulaDesdeFila(fila);
			aulas.add(aula);
		}
		return aulas;
	}

	private Aula getAulaDesdeFila(Row fila) {
		String nombre = fila.getString(NOMBRE);
		int puestos = fila.getInt(PUESTOS);
		return new Aula(nombre, puestos);
	}

	@Override
	public int getTamano() {
		return (int)tablaAulas.count();
	}

	@Override
	public void insertar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede insertar un aula nula.");
		}
		try {
			tablaAulas.insert(ID_AULA, NOMBRE, PUESTOS).values(null, aula.getNombre(), aula.getPuestos()).execute();
		} catch (XProtocolError e) {
			throw new OperationNotSupportedException("ERROR: Ya existe un aula con ese nombre.");
		}
	}

	@Override
	public Aula buscar(Aula aula) {
		if (aula == null) {
			throw new IllegalArgumentException("ERROR: No se puede buscar un aula nula.");
		}
		RowResult filas = tablaAulas.select("*").where("nombre = :nombre").bind(NOMBRE, aula.getNombre()).execute();
		Row fila = filas.fetchOne();
		if (fila != null) {
			aula = getAulaDesdeFila(fila);
		}
		return aula;
	}

	@Override
	public void borrar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new IllegalArgumentException("ERROR: No se puede borrar un aula nula.");
		}
		try {
			Result borradas = tablaAulas.delete().where("nombre = :nombre").bind(NOMBRE, aula.getNombre()).execute();
			if (borradas.getAffectedItemsCount() == 0)
				throw new OperationNotSupportedException("ERROR: No existe ningún aula con ese nombre.");
		} catch (XProtocolError e) {
			throw new OperationNotSupportedException("ERROR: " + e.getMessage());
		}
	}

}
