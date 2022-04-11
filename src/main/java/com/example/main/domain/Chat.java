package com.example.main.domain;


import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

import java.util.List;


/**
 * The persistent class for the chat database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Chat.findAll", query="SELECT c FROM Chat c")
public class Chat implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name="idComprador")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name="idAnuncio")
	private Anuncio anuncio;

	
	@OneToMany(mappedBy="chat")
	private List<Mensaje> mensajes;
}