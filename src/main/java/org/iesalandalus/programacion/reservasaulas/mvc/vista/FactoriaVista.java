package org.iesalandalus.programacion.reservasaulas.mvc.vista;

import org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.VistaIUGVentanas;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.texto.VistaTexto;

public enum FactoriaVista {

	TEXTO {
		public IVista crear() {
			return new VistaTexto();
		}
	},
	IUGVENTANAS {
		public IVista crear() {
			return new VistaIUGVentanas();
		}
	};
	
	public abstract IVista crear();
}
