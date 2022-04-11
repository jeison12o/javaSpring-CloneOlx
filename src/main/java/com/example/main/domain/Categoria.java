package com.example.main.domain;



import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;


/**
 * The persistent class for the categoria database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Categoria.findAll", query="SELECT c FROM Categoria c")
public class Categoria implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Pattern(regexp = "^[A-Za-z -]+$", message = "Solo son admitidos los caracteres alfabeticos en el nombre de la categoria.")
	@Length(min = 3, max = 15, message="La categoria debe tener minimo un tamano de 3 caracteres y maximo 15.")
	@NotBlank(message = "{nombre.obligatorio}")
	@Column (unique = true)
	private String nombre;
	
	@OneToMany//linea que hace error de los anuncios(mappedBy="categoria", cascade = CascadeType.REMOVE)
	private List<Subcategoria> subcategorias;
}