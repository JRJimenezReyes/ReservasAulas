package org.iesalandalus.programacion.reservasaulas.mvc.modelo;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros.FactoriaFuenteDatosFicheros;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.FactoriaFuenteDatosMongoDB;

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
	};
	
	public abstract IFuenteDatos crear();

}
