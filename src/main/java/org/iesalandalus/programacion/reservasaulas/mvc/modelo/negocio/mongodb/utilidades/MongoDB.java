package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.utilidades;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
	
	private static final String SERVIDOR = "35.198.132.204";
	private static final int PUERTO = 27017;
	private static final String BD = "reservasaulas";
	private static final String USUARIO = "profesor";
	private static final String CONTRASENA = "profesor-2020";
	
	public static final String PROFESOR = "profesor";
	public static final String NOMBRE = "nombre";
	public static final String CORREO = "correo";
	public static final String TELEFONO = "telefono";
	
	private static MongoClient conexion = null;
	
	private MongoDB() {
		// Evitamos que se cree el constructor por defecto
	}
	
	public static MongoDatabase getBD() {
		if (conexion == null) {
			establecerConexion();
		}
		return conexion.getDatabase(BD);
	}
	
	private static MongoClient establecerConexion() {
	    Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
	    mongoLogger.setLevel(Level.SEVERE);
		if (conexion == null) {
			MongoCredential credenciales = MongoCredential.createScramSha1Credential(USUARIO, BD, CONTRASENA.toCharArray());
			conexion = MongoClients.create(
			        MongoClientSettings.builder()
	                .applyToClusterSettings(builder -> 
	                        builder.hosts(Arrays.asList(new ServerAddress(SERVIDOR, PUERTO))))
	                .credential(credenciales)
	                .build());
			System.out.println("Conexión a MongoDB realizada correctamente.");
		}
		return conexion;
	}
	
	public static void cerrarConexion() {
		if (conexion != null) {
			conexion.close();
			conexion = null;
			System.out.println("Conexión a MongoDB cerrada.");
		}
	}
	
	public static Document obtenerDocumentoDesdeProfesor(Profesor profesor) {
		if (profesor == null) {
			return null;
		}
		String nombre = profesor.getNombre();
		String correo = profesor.getCorreo();
		String telefono = profesor.getTelefono();
		return new Document().append(NOMBRE, nombre).append(CORREO, correo).append(TELEFONO, telefono);
	}

	public static Profesor obtenerProfesorDesdeDocumento(Document documentoProfesor) {
		if (documentoProfesor == null) {
			return null;
		}
		return new Profesor(documentoProfesor.getString(NOMBRE), documentoProfesor.getString(CORREO), documentoProfesor.getString(TELEFONO));
	}

}
