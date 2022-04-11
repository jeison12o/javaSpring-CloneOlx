package com.example.main.domain;


import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.Data;

import java.sql.Timestamp;


/**
 * The persistent class for the mensaje database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Mensaje.findAll", query="SELECT m FROM Mensaje m")
public class Mensaje implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String fecha;
	
	private String hora;
	
	private String mensaje;
	
	private boolean estado;
	
	@ManyToOne
	@JoinColumn(name="idRemitente")
	private Usuario remitente;

	@ManyToOne
	@JoinColumn(name="idChat")
	private Chat chat;
}