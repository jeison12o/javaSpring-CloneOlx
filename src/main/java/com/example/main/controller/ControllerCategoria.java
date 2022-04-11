package com.example.main.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.main.domain.Categoria;
import com.example.main.repository.RepoCategoria;

@Controller
public class ControllerCategoria {

	@Autowired
	private RepoCategoria repo;

	@GetMapping("/admin/categorias")
	public String menu() {
		return "categorias";
	}

	@GetMapping("/admin/registrarCategoria")
	public String registrarCategoria() {
		return "guardar-categoria";
	}

	@GetMapping("/admin/categoria")
	public String categoria(Model model) {
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("categorias", repo.findAll());
		return "categoria";
	}

	@PostMapping("/admin/guardarCategoria")
	public String guardar(@Valid Categoria c, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("categorias", repo.findAll());
			return "categoria";
		}
		try {
			repo.save(c);
		} catch (Exception e) {
			model.addAttribute("categorias", repo.findAll());
			model.addAttribute("error", "Esta categor�a ya existe.");
			return "categoria";
		}
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("categorias", repo.findAll());
		return "redirect:/categoria";
	}

	@GetMapping("/admin/buscarCategoria")
	public String buscar(@PathVariable("id") int id, Model model) {
		Categoria cate = repo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Usuario inv�lido Id:" + id));
		model.addAttribute("categoria", cate);
		return "modificar-categoria";
	}

	@PostMapping("/admin/modificarCategoria/{id}")
	public String modificar(@PathVariable("id") int id, @Valid Categoria cate, BindingResult result, Model model) {
		if (result.hasErrors()) {
			cate.setId(id);
			return "modificar-categoria";
		}
		repo.save(cate);
		model.addAttribute("lista", repo.findAll());
		return "categoria";
	}

	@GetMapping("/admin/eliminarCategoria/{id}")
	public String eliminar(@PathVariable("id") int id, Model model) {
		Categoria c = repo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id de categoria invalido:" + id));
		repo.delete(c);
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("categorias", repo.findAll());
		return "categoria";
	}
}
