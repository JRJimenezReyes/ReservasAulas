package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.utilidades;

import com.mysql.cj.protocol.x.XProtocolError;
import com.mysql.cj.xdevapi.Schema;
import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SessionFactory;

public class SesionBD {
	
	private static final String HOST = "35.246.226.125";
	private static final String ESQUEMA = "reservasaulas";
	private static final String USUARIO = "reservasaulas";
	private static final String CONTRASENA = "reservasaulas-2020";
	private static final int PUERTO = 33060;
	
	private static Session sesion = null;
	
	private SesionBD() {
		// Evitamos que se creen instancias
	}
	
	public static Schema getEsquema() {
		if (sesion == null) {
			creaSesion();
		}
		return (sesion == null) ? null : sesion.getSchema(ESQUEMA);
	}
	private static void creaSesion() {
		String cadenaConexion = String.format("mysqlx://%s:%s@%s:%d", USUARIO, CONTRASENA, HOST, PUERTO);
		try {
			sesion = new SessionFactory().getSession(cadenaConexion);
			System.out.println("Conexión a MySQL mediante XdevAPI realizada correctamente.");
		} catch (XProtocolError e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	
	public static void cierraSesion() {
		if (sesion != null) {
			sesion.close();
			sesion = null;
			System.out.println("Conexión a MySQL mediante XdevAPI cerrada correctamente.");
		}
	}

}
