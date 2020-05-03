package org.iesalandalus.programacion.reservasaulas.mvc.modelo;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros.FactoriaFuenteDatosFicheros;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.FactoriaFuenteDatosMongoDB;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql.FactoriaFuenteDatosMySQL;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.FactoriaFuenteDatosMySQLXdevAPI;

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
	},
	MYSQL_XDEVAPI {
		public IFuenteDatos crear() {
			return new FactoriaFuenteDatosMySQLXdevAPI();
		}
	};
	
	public abstract IFuenteDatos crear();

}
