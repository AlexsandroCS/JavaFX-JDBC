package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
	public List<Department> findAll(){
		// Dados Mockados.
		List<Department> list = new ArrayList<>();
		list.add(new Department(1,"Livraria"));
		list.add(new Department(2,"Computação"));
		list.add(new Department(3,"Financeiro"));
		return list;
	}
}
