package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.utilidades;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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

}
