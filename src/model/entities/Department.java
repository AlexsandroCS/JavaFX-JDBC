package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Department implements Serializable {
	// Serializable.
	private static final long serialVersionUID = 1L;

    // Atributes.
    private Integer id;
    private String name;

    // Constructor.
    public Department(){}
    public Department(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    // Getters and Setters.
    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Equals and HashCode.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString.
    @Override
    public String toString() {
        return getId() + " | Department: " + getName();
    }
}