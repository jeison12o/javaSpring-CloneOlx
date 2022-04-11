package com.example.main.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.main.domain.Chat;
import com.example.main.domain.Mensaje;

public interface RepoMensaje extends CrudRepository<Mensaje, Integer>{
	
	@Query("Select m from Mensaje m join m.chat c  where c =  ?1")
	public Iterable<Mensaje> listarMensajesChats(Chat chat);
	
}
