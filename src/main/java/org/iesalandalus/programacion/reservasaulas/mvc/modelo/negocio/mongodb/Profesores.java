package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.utilidades.MongoDB;

import com.mongodb.client.MongoCollection;

public class Profesores implements IProfesores {
	
	private static final String COLECCION = "profesores";
	
	private MongoCollection<Document> colecccionProfesores;	
	
	@Override
	public void comenzar() {
		colecccionProfesores = MongoDB.getBD().getCollection(COLECCION);
	}

	@Override
	public void terminar() {
		MongoDB.cerrarConexion();
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
