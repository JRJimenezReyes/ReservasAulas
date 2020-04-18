package org.iesalandalus.programacion.reservasaulas.mvc.vista.iugpestanas.controladoresvistas;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.iugpestanas.utilidades.Dialogos;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorAnadirAula {
	
	private static final String ER_OBLIGATORIO = ".+";
	private static final String ER_PUESTOS = "0[1-9]\\d|100";
	
	private IControlador controladorMVC;
	private ObservableList<Aula> aulas;
	
	@FXML private TextField tfNombre;
	@FXML private TextField tfPuestos;
	@FXML private Button btAnadir;
	@FXML private Button btCancelar;
	
	@FXML
	private void initialize() {
		tfNombre.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_OBLIGATORIO, tfNombre));
		tfPuestos.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_PUESTOS, tfPuestos));
	}
	
	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
	public void setAulas(ObservableList<Aula> aulas) {
		this.aulas = aulas;
	}
	
	@FXML
	private void anadirAula() {
		Aula aula = null;
		try {
			aula = getAula();
			controladorMVC.insertar(aula);
			aulas.add(aula);
			Stage propietario = ((Stage) btAnadir.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Añadir Aula", "Aula añadida satisfactoriamente", propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Añadir Aula", e.getMessage());
		}	
	}
	
	@FXML
	private void cancelar() {
		((Stage) btCancelar.getScene().getWindow()).close();
	}
	
    public void inicializa() {
    	tfNombre.setText("");
    	compruebaCampoTexto(ER_OBLIGATORIO, tfNombre);
    	tfPuestos.setText("");
    	compruebaCampoTexto(ER_PUESTOS, tfPuestos);
    }
	
	private void compruebaCampoTexto(String er, TextField campoTexto) {	
		String texto = campoTexto.getText();
		if (texto.matches(er)) {
			campoTexto.setStyle("-fx-border-color: green; -fx-border-radius: 5;");
		}
		else {
			campoTexto.setStyle("-fx-border-color: red; -fx-border-radius: 5;");
		}
	}
	
	private Aula getAula() throws OperationNotSupportedException {
		String nombre = tfNombre.getText();
		int puestos = 0;
		try {
			puestos = Integer.parseInt(tfPuestos.getText());
		} catch (NumberFormatException e) {
			throw new OperationNotSupportedException("El número de puestos debe ser entero.");
		}
		return new Aula(nombre, puestos);
	}

}
