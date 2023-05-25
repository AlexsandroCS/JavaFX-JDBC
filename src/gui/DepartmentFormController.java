package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	// Dependência para o Department.
	private Department entity;
	
	// Dependência para o DepartmentService.
	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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
	
	// Implementando métodos do dataChangeListeners.
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	// Implementando o método Set do Department entity.
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	// Implementando o método Set do Department service.
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
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
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null!");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(DBException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	// Método de notificação de criação de novo departamento.
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	// Capturando objetos digitados no formulário do departamento.
	private Department getFormData() {
		Department newForm = new Department();
		// Chamando a função que converte string para inteiro e passando o valor digitado dentro dela.
		newForm.setId(Utils.tryParseToInt(txtId.getText()));
		newForm.setName(txtName.getText());
		
		return newForm;
	}
	
	@FXML // Evento ao clicar no botão: Cancelar
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
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