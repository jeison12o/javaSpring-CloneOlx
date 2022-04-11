package com.example.main.domain;


import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Length(min = 3, max = 40, message="El apellido acepta datos desde los 3 hasta los 40 caracteres")
	//@Pattern(regexp = "^([A-Za-z]{0}?[A-Za-z\\']+[\\s])+([A-Za-z]{0}?[A-Za-z\\'])+[\\s]?([A-Za-z]{0}?[A-Za-z\\'])?$", message="Solo se aceptan caracteres alfabeticos.")
	@NotBlank(message = "{apellido.obligatorio}")
	private String apellido;

	@NotBlank(message = "{correo.obligatorio}")
	@Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$", message="{correo.formato}")
	@Column(unique = true)
	private String correo;

	@NotBlank(message = "{direccion.obligatorio}")
	@Length(min = 5, max = 35, message = "En la direccion solo se permite una leve descripcion desde 5 caracteres hasta 35")
	private String direccion;

	@NotNull(message = "{fecha.obligatorio}")
	@DateTimeFormat(pattern = "dd/mm/yyyy")
	private String fechaNacimiento;
	
	@Length(min = 2, max = 40, message="El nombre acepta datos desde los 2 hasta los 40 caracteres")
	//@Pattern(regexp = "^([A-Za-z]{0}?[A-Za-z\\']+[\\s])+([A-Za-z]{0}?[A-Za-z\\'])+[\\s]?([A-Za-z]{0}?[A-Za-z\\'])?$", message="Solo se aceptan caracteres alfabeticos.")
	@NotBlank(message = "{nombre.obligatorio}")
	private String nombre;

	@Length(min = 7, max = 10, message="Ingresa un numero de telefono valido, se aceptan fijos (7 cifras) o mobiles (10 cifras)")
	@NotNull(message = "{telefono.obligatorio}")
	private String telefono; // originalmente era Integer, lo cambi�, -Junior.
	
	//@Length(min = 6)
	@NotBlank(message = "{contrasena.obligatorio}")
	@Column(length = 255)
	private String contrasena;
	
	@Transient
	//@NotBlank(message = "{contrasena.repetir}") no pongan
	private String repContrasena;

	// este tipo de delete on cascade debe ir siempre en la entity padre en la relacion OneToMany
	
	@OneToMany(mappedBy="usuario", cascade = CascadeType.REMOVE)
	private List<Anuncio> anuncios;

	//bi-directional many-to-one association to Chat
	@OneToMany(mappedBy="usuario")
	private List<Chat> chats;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "authorities_usuarios", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
	private Set<Authority> authority;
}