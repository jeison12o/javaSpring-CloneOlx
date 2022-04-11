package com.example.main.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.main.domain.Categoria;

public interface RepoCategoria extends CrudRepository<Categoria, Integer>{
	
	@Query(value = "select * from categoria c join subcategoria", nativeQuery = true)
	Categoria all();
	
	
	Categoria findByNombre(String nombre);
	
	// --- eliminar por nombre
	
	@Query("delete from Categoria where nombre = ?1")
	public boolean eliminarPorNombre(String nombre);
	
}
