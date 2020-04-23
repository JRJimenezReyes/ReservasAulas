package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.utilidades.MongoDB;

import com.mongodb.client.MongoCollection;

public class Reservas implements IReservas {
	
	private static final String COLECCION = "reservas";
	
	private MongoCollection<Document> coleccionReservas;
	
	@Override
	public void comenzar() {
		coleccionReservas = MongoDB.getBD().getCollection(COLECCION);
	}

	@Override
	public void terminar() {
		MongoDB.cerrarConexion();
	}
	
	@Override
	public List<Reserva> get() {
		return new ArrayList<>();
	}
	
	@Override
	public List<Reserva> get(Profesor profesor) {
		return new ArrayList<>();
	}
	
	@Override
	public List<Reserva> get(Aula aula) {
		return new ArrayList<>();
	}
	
	@Override
	public int getTamano() {
		return 0;
	}
	
	@Override
	public void insertar(Reserva reserva) throws OperationNotSupportedException {
	}
	
	@Override
	public Reserva buscar(Reserva reserva) {
		return null;
	}
	
	@Override
	public void borrar(Reserva reserva) throws OperationNotSupportedException {
	}

}
