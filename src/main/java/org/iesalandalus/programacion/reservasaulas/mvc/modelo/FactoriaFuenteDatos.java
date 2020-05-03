package org.iesalandalus.programacion.reservasaulas.mvc.modelo;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros.FactoriaFuenteDatosFicheros;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.FactoriaFuenteDatosMongoDB;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql.FactoriaFuenteDatosMySQL;

public enum FactoriaFuenteDatos {
	
	FICHEROS {
		public IFuenteDatos crear() {
			return new FactoriaFuenteDatosFicheros();
		}
	},
	MONGODB {
		public IFuenteDatos crear() {
			return new FactoriaFuenteDatosMongoDB();
		}
	},
	MYSQL {
		public IFuenteDatos crear() {
			return new FactoriaFuenteDatosMySQL();
		}
	};
	
	public abstract IFuenteDatos crear();

}
