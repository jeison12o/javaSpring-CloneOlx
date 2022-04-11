package com.example.main.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * The persistent class for the subcategoria database table.
 * 
 */
@Entity
@Data
@NamedQuery(name = "Subcategoria.findAll", query = "SELECT s FROM Subcategoria s")
public class Subcategoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Pattern(regexp = "^[A-Za-z -]+$", message = "Solo son admitidos los caracteres alfabeticos en el nombre de la subcategoria.")
	@Length(min = 3, max = 15, message="La categoria debe tener minimo un tamano de 3 caracteres y maximo 15.")
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "idCategoria")
	private Categoria categoria;

	@Transient
	private String nombreCategoria;
	
	/*@OneToMany(mappedBy="subcategoria")  <-- esta fue una relacion que yo hice a mano para poder hacer el método de abajo, desde siempre.
	private List<Anuncio> anuncios;
	
	@PreRemove
	public void nullableSetToAdds() {
		anuncios.forEach(add -> add.setSubcategoria(null));
	}*/
	
}