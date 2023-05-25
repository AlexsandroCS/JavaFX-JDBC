package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable {

	@FXML // ID
	private TextField txtId;
	@FXML // Name
	private TextField txtName;
	@FXML // Label de Error
	private Label labelErrorName;
	@FXML // Botão Salvar
	private Button btSave;
	@FXML // Botão Cancelar
	private Button btCancel;

	@FXML // Evento ao clicar no botão: Salvar
	public void onBtSaveAction() {
		System.out.println("on Button Save Action!");
	}

	@FXML // Evento ao clicar no botão: Cancelar
	public void onBtCancelAction() {
		System.out.println("on Button Cancel Action!");
	}

	// Restrições do formulário.
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}
}