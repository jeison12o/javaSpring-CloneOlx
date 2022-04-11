package com.example.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.main.domain.Chat;
import com.example.main.domain.Usuario;

public interface RepoChat extends CrudRepository<Chat, Integer>{

	@Query("select c from Chat c join c.anuncio a where a.usuario = ?1")
	List<Chat> listaChatsVendedor(Usuario usuario);
	
	@Query("select c from Chat c where c.usuario = ?1")
	List<Chat> listaChatsComprador(Usuario usuario);
}
