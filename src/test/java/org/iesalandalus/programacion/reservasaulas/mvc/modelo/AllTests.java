package org.iesalandalus.programacion.reservasaulas.mvc.modelo;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.AulaTest;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHoraTest;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.ProfesorTest;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.ReservaTest;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.TramoTest;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ModeloFicherosTest;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros.AulasTest;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros.ProfesoresTest;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros.ReservasTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ProfesorTest.class, AulaTest.class, TramoTest.class, PermanenciaPorHoraTest.class, ReservaTest.class,
	ProfesoresTest.class, AulasTest.class, ReservasTest.class,
	ModeloFicherosTest.class })
public class AllTests {

}
