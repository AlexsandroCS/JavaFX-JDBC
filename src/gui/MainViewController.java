package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable{
	
	// Menu Botões: Registrar.
	@FXML
	private MenuItem menuItemVendedor;
	@FXML
	private MenuItem menuItemDepartamento;
	
	// Meu Botões: Ajuda.
	@FXML
	private MenuItem menuItemMais;
	
	
	// ------------------------ TRATANDO AÇÕES DOS BOTÕES ------------------------
	
	// Ação Botão: Vendedor.
	@FXML
	public void onMenuItemVendedorAction() {
		loadView("/gui/MainView.fxml", x -> {});
	}
	
	// Ação Botão: Departamento.
	@FXML
	public void onMenuItemDepartamentoAction() {
		// Após a vírgula na passagem de parâmetro é uma expressão lambda.
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	// Ação Botão: Mais.
	@FXML
	public void onMenuItemMaisAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	// ------------------------ MÉTODO PARA CARREGAR MÚLTIPLAS CENAS ------------------------
	public synchronized <T> void loadView(String absolutName, java.util.function.Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			// Pegando a referência da VBox.
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			// Pegando o primeiro "<children>" da janela principal.
			Node mainMenu = mainVBox.getChildren().get(0);
			// Limpando todos os "<children>" da mainVBox.
			mainVBox.getChildren().clear();
			
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch(IOException error) {
			Alerts.showAlert("IOException","Error Loading View!",error.getMessage(), AlertType.ERROR);
		}
	}	
}
