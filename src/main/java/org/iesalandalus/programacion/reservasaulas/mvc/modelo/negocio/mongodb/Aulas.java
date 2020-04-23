package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.utilidades.MongoDB;

import com.mongodb.client.MongoCollection;

public class Aulas implements IAulas {
	
	private static final String COLECCION = "aulas";
	
	private MongoCollection<Document> coleccionAulas;
	
	@Override
	public void comenzar() {
		coleccionAulas = MongoDB.getBD().getCollection(COLECCION);
	}

	@Override
	public void terminar() {
		MongoDB.cerrarConexion();
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
