package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{
	private static final long seriaVersionUID = 1L;
	
	// Map para capturar o campo e o tipo de erro caso haja algum erro.
	private Map<String, String> errors = new HashMap<>();
	
	// Exceção para validação do formulário.
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErrors(){
		return errors;
	}
	
	// Adicionando os possíveis erros a coleção Map.
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
