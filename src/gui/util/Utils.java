package gui.util;

import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	public static Stage currentStage(javafx.event.ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
	
	// Método para pegar os valores digitado na criação de um departamento e transforma-los. 
	public static Integer tryParseToInt(String str) {
		// Caso minha string seja um valor normal retorne try
		try {
			return Integer.parseInt(str);
		}
		// Caso minha string seja um valor diferente de inteiro retorne null.
		catch(NumberFormatException e) {
			return null;
		}
	}
}
