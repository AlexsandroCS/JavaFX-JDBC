package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	// Dependência para o Seller.
	private Seller entity;
	
	// Dependência para o SellerService.
	private SellerService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Label labelErrorSalary;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	// Implementando métodos do dataChangeListeners.
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	// Implementando o método Set do Seller entity.
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	// Implementando o método Set do Seller service.
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	// Implementando a passagem de dados do Seller para o formulário.
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
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
		catch(ValidationException e) {
			setErrorMesseges(e.getErrors());
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
	private Seller getFormData() {
		Seller newForm = new Seller();
		
		// Instanciando erro de validação.
		ValidationException exception = new ValidationException("Validation error");
		
		// Chamando a função que converte string para inteiro e passando o valor digitado dentro dela.
		newForm.setId(Utils.tryParseToInt(txtId.getText()));
		
		// Fazendo a verificação de possíveis erros do campo Name.
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("Name", "Field can't be empty!");
		}
		
		newForm.setName(txtName.getText());
		
		// Lançando exceções caso haja.
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
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
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}
	
	// Gerando os erros ao usuário.
	private void setErrorMesseges(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("Name")) {
			labelErrorName.setText(errors.get("Name"));
		}
	}
}