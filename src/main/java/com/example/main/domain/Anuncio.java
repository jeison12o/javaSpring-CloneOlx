package com.example.main.domain;


import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

import java.util.List;


/**
 * The persistent class for the anuncio database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Anuncio.findAll", query="SELECT a FROM Anuncio a")
public class Anuncio implements Serializable {
	
	private static final long serialVersionUID = 2679541203984681110L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank(message = "{telefono.obligatorio}")
	private String contacto;

	@Length(min=15, max = 200, message="La descripcion debe tener m�nimo un tama�o de 15 caracteres y menos de 200.")
	@NotBlank(message = "{descripcion.obligatorio}")
	private String descripcion;

	@Digits(integer = 8, fraction = 0, message = "Los precios permitidos son: min 1 000 - max 99 999 999, sin puntos ni comas.")
	@NotNull(message = "{precio.obligatorio}")
	private long precio; // inicialmente era double, lo cambi�, -Junior.

	@NotBlank(message = "{titulo.obligatorio}")
	private String titulo;

	@NotBlank(message = "{foto.obligatorio}")
	@Column(length = 255)
	private String urlFoto;
	
	@ManyToOne
	@JoinColumn(name="idDueno")
	private Usuario usuario;

	@OneToMany(mappedBy="anuncio", cascade = CascadeType.REMOVE)
	private List<Chat> chats;

	
	@ManyToOne
	@JoinColumn(name="idSubcategoria")
	private Subcategoria subcategoria;
}