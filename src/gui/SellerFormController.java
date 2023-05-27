package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	// Dependência para o Seller.
	private Seller entity;

	// Dependência para o SellerService.
	private SellerService service;

	private DepartmentService departmentService;

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
	private ComboBox<Department> comboBoxDepartment;

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

	@FXML
	private ObservableList<Department> obsList;

	// Implementando métodos do dataChangeListeners.
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	// Implementando o método Set do Seller entity.
	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	// Implementando o método Set do Seller service.
	public void setSellerServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	// Implementando a passagem de dados do Seller para o formulário.
	public void updateFormData() {
		if (entity == null) {
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
		if (entity.getDepartment() == null) {
			 comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else {
			comboBoxDepartment.setValue(entity.getDepartment());
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
		} catch (ValidationException e) {
			setErrorMesseges(e.getErrors());
		} catch (DBException e) {
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

		// Chamando a função que converte string para inteiro e passando o valor
		// digitado dentro dela.
		newForm.setId(Utils.tryParseToInt(txtId.getText()));

		// Fazendo a verificação de possíveis erros do campo Name.
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("Name", "Field can't be empty!");
		}

		newForm.setName(txtName.getText());
		
		// Fazendo a verificação de possíveis erros do campo Name.
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("Email", "Field can't be empty!");
		}

		newForm.setEmail(txtEmail.getText());

		// Fazendo a verificação da Data de nascimento.
		if (dpBirthDate.getValue() == null) {
			exception.addError("Birth Date", "Field can't be empty!");
		}
		else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			newForm.setBirthDate(Date.from(instant));
		}
		
		// Fazendo a verificação de possíveis erros do campo Name.
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			exception.addError("BaseSalary", "Field can't be empty!");
		}
		
		newForm.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		
		newForm.setDepartment(comboBoxDepartment.getValue());
		
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
		initializeComboBoxDepartment();
	}

	// Caregando lista com Departamentos do banco de dados.
	public void loadAssociatedObjects() {
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
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
		else {
			labelErrorName.setText("");
		}
		
		if (fields.contains("Email")) {
			labelErrorEmail.setText(errors.get("Email"));
		}
		else {
			labelErrorEmail.setText("");
		}
		
		if (fields.contains("BaseSalary")) {
			labelErrorSalary.setText(errors.get("BaseSalary"));
		}
		else {
			labelErrorSalary.setText("");
		}
		
		if (fields.contains("BirthDate")) {
			labelErrorBirthDate.setText(errors.get("BirthDate"));
		}
		else {
			labelErrorBirthDate.setText("");
		}
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}