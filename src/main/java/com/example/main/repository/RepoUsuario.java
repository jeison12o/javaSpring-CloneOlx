package com.example.main.repository;
import org.springframework.data.repository.CrudRepository;

import com.example.main.domain.Usuario;

public interface RepoUsuario extends CrudRepository<Usuario, Integer>{
	Usuario findByCorreo(String correo);
}
