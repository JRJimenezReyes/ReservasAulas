package org.iesalandalus.programacion.reservasaulas.mvc.vista;

import org.iesalandalus.programacion.reservasaulas.mvc.vista.iugpestanas.VistaIUGPestanas;
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
	},
	IUGPESTANAS {
		public IVista crear() {
			return new VistaIUGPestanas();
		}
	};
	
	public abstract IVista crear();
}
