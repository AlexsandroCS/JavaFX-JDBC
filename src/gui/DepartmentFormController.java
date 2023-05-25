package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	
	// Dependência para o Department.
	private Department entity;

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
	
	// Implementando o método Set do Department entity.
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	// Implementando a passagem de dados do Department para o formulário.
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

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