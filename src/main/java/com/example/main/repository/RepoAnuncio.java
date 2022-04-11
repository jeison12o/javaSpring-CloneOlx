package com.example.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.main.domain.Anuncio;
import com.example.main.domain.Subcategoria;
import com.example.main.domain.Usuario;

public interface RepoAnuncio extends CrudRepository<Anuncio, Integer>{

	// ----------------- Buscar anuncio por palabra relacionada -------------------
	
	//@Query("Select a from Anuncio a where a.titulo like '%?1%'")
	
	public Iterable<Anuncio> findAllByTituloOrDescripcionContainingIgnoreCase(String titulo,String descripcion);
	
	// -----------------  Ordenar anuncios por precio ascendente ----------------------
	
	@Query("Select a from Anuncio a order by a.precio asc")
	public Iterable<Anuncio> listarAnunciosPrecioAsc();
	
	// ----------------- Ordenar anuncios por precio descendente ----------------------
	
	@Query("Select a from Anuncio a order by a.precio desc")
	public Iterable<Anuncio> listarAnunciosPrecioDesc();
	
	
	List<Anuncio> findAllByUsuario(Usuario usuario);
	List<Anuncio> findAllBySubcategoria(Subcategoria subcategoria);
	
}
