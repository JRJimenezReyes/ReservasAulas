package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.utilidades;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Tramo;

import com.mysql.cj.protocol.x.XProtocolError;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.DbDocImpl;
import com.mysql.cj.xdevapi.JsonNumber;
import com.mysql.cj.xdevapi.JsonString;
import com.mysql.cj.xdevapi.Schema;
import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SessionFactory;

public class SesionBD {
	
	private static final String HOST = "35.246.226.125";
	private static final String ESQUEMA = "reservasaulas";
	private static final String USUARIO = "reservasaulas";
	private static final String CONTRASENA = "reservasaulas-2020";
	private static final int PUERTO = 33060;
	
	private static final String NOMBRE = "nombre";
	private static final String PUESTOS = "puestos";
	private static final String CORREO = "correo";
	private static final String TELEFONO = "telefono";
	private static final String AULA = "aula";
	private static final String PROFESOR = "profesor";
	private static final String DIA = "dia";
	private static final String TRAMO = "tramo";
	private static final String HORA = "hora";	
	
	private static final DateTimeFormatter FORMATO_DIA= DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
	
	private static Session sesion = null;
	
	private SesionBD() {
		// Evitamos que se creen instancias
	}
	
	public static Schema getEsquema() {
		if (sesion == null) {
			crearSesion();
		}
		return (sesion == null) ? null : sesion.getSchema(ESQUEMA);
	}
	private static void crearSesion() {
		String cadenaConexion = String.format("mysqlx://%s:%s@%s:%d", USUARIO, CONTRASENA, HOST, PUERTO);
		try {
			sesion = new SessionFactory().getSession(cadenaConexion);
			System.out.println("Conexión a MySQL mediante XdevAPI realizada correctamente.");
		} catch (XProtocolError e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	
	public static void cerrarSesion() {
		if (sesion != null) {
			sesion.close();
			sesion = null;
			System.out.println("Conexión a MySQL mediante XdevAPI cerrada correctamente.");
		}
	}
	
	public static DbDoc getDocumento(Aula aula) {
		DbDoc aulaDD = new DbDocImpl();
		aulaDD.add(NOMBRE, new JsonString().setValue(aula.getNombre()));
		aulaDD.add(PUESTOS, new JsonNumber().setValue(String.valueOf(aula.getPuestos())));
		return aulaDD;
	}
	
	public static Aula getAula(DbDoc aulaDD) {
		String nombre = ((JsonString)aulaDD.get(NOMBRE)).getString();
		int puestos = ((JsonNumber)aulaDD.get(PUESTOS)).getInteger();
		return new Aula(nombre, puestos);
	}
	
	public static DbDoc getDocumento(Profesor profesor) {
		DbDoc profesorDD = new DbDocImpl();
		profesorDD.add(NOMBRE, new JsonString().setValue(profesor.getNombre()));
		profesorDD.add(CORREO, new JsonString().setValue(profesor.getCorreo()));
		if (profesor.getTelefono() != null) {
			profesorDD.add(TELEFONO, new JsonString().setValue(profesor.getTelefono()));
		}
		return profesorDD;
	}
	
	public static Profesor getProfesor(DbDoc profesorDD) {
		String nombre = ((JsonString)profesorDD.get(NOMBRE)).getString();
		String correo = ((JsonString)profesorDD.get(CORREO)).getString();
		String telefono = null;
		if (profesorDD.containsKey(TELEFONO)) {
			telefono = ((JsonString)profesorDD.get(TELEFONO)).getString();
		}
		return new Profesor(nombre, correo, telefono);
	}
	
	public static DbDoc getDocumento(Reserva reserva) {
		DbDoc reservaDD = new DbDocImpl();
		reservaDD.add(PROFESOR, getDocumento(reserva.getProfesor()));
		reservaDD.add(AULA, getDocumento(reserva.getAula()));
		Permanencia permanencia = reserva.getPermanencia();
		reservaDD.add(DIA, new JsonString().setValue(permanencia.getDia().format(FORMATO_DIA)));
		if (permanencia instanceof PermanenciaPorTramo) {
			reservaDD.add(TRAMO, new JsonString().setValue(((PermanenciaPorTramo)permanencia).getTramo().name()));
		} else {
			reservaDD.add(HORA,  new JsonString().setValue(((PermanenciaPorHora)permanencia).getHora().format(FORMATO_HORA)));
		}
		return reservaDD;
	}
	
	public static Reserva getReserva(DbDoc reservaDD) {
		DbDoc profesorDD = (DbDoc)reservaDD.get(PROFESOR);
		
		String nombre = ((JsonString)profesorDD.get(NOMBRE)).getString();
		String correo = ((JsonString)profesorDD.get(CORREO)).getString();
		String telefono = null;
		if (profesorDD.containsKey(TELEFONO)) {
			telefono = ((JsonString)profesorDD.get(TELEFONO)).getString();
		}
		Profesor profesor = new Profesor(nombre, correo, telefono);
		DbDoc aulaDD = (DbDoc)reservaDD.get(AULA);
		nombre = ((JsonString)aulaDD.get(NOMBRE)).getString();
		int puestos = ((JsonNumber)aulaDD.get(PUESTOS)).getInteger();
		Aula aula = new Aula(nombre, puestos);
		Permanencia permanencia = null;
		LocalDate dia = LocalDate.parse(((JsonString)reservaDD.get(DIA)).getString(), FORMATO_DIA);
		if (reservaDD.containsKey(TRAMO)) {
			Tramo tramo = Tramo.valueOf(((JsonString)reservaDD.get(TRAMO)).getString());
			permanencia = new PermanenciaPorTramo(dia, tramo);
		} else if (reservaDD.containsKey(DIA)) {
			LocalTime hora = LocalTime.parse(((JsonString)reservaDD.get(HORA)).getString(), FORMATO_HORA);
			permanencia = new PermanenciaPorHora(dia, hora);
		}
		return new Reserva(profesor, aula, permanencia);
	}

}
