package com.example.main.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;


/**
 * The persistent class for the administrador database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Administrador.findAll", query="SELECT a FROM Administrador a")
public class Administrador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank(message = "{apellido.obligatorio}")
	@Size(min = 3, max = 255, message = "{apellido.tamano}")
	
	@Pattern(regexp = "^([A-Za-z������������]{0}?[A-Za-z������������\\']+[\\s])+([A-Za-z������������]{0}?[A-Za-z������������\\'])+[\\s]?([A-Za-z������������]{0}?[A-Za-z������������\\'])?$", message = "{apellido.valido}")
	private String apellido;

	@NotBlank(message = "{correo.obligatorio}")
	@Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$", message="{correo.formato}")
	@Column(unique = true)
	private String correo;

	@NotBlank(message = "{direccion.obligatorio}")
	private String direccion;

	@NotNull(message = "{fecha.obligatorio}")
	@DateTimeFormat(pattern = "dd/mm/yyyy")
	private String fechaNacimiento;

	@NotBlank(message = "{nombre.obligatorio}")
	private String nombre;

	@NotNull(message = "{telefono.obligatorio}")
	private int telefono;
	
	@NotBlank(message = "{contrasena.obligatorio}")
	@Column(length = 255)
	private String contrasena;
	
	@Transient
	@NotBlank(message = "{contrasena.repetir}")
	private String repContrasena;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "authorities_administradores", joinColumns = @JoinColumn(name = "admin_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
	private Set<Authority> authority;
	
}