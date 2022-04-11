package com.example.main.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.main.domain.Administrador;

public interface RepoAdministrador extends CrudRepository<Administrador, Integer>{

	Administrador findByCorreo(String correo);
	
}
