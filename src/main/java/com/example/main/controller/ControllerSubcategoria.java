package com.example.main.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.main.domain.Categoria;
import com.example.main.domain.Subcategoria;
import com.example.main.repository.RepoCategoria;
import com.example.main.repository.RepoSubcategoria;

@Controller
public class ControllerSubcategoria {

	@Autowired
	private RepoSubcategoria repo;

	@Autowired
	private RepoCategoria repoCategoria;

	@GetMapping("/admin/subcategoria")
	public String subcategoria(Model model) {
		model.addAttribute("subcategoria", new Subcategoria());
		model.addAttribute("subcategorias", repo.findAll());
		model.addAttribute("categorias", repoCategoria.findAll());
		return "subcategoria";
	}

	@PostMapping("/admin/guardarSubcategoria")
	public String guardar(@Valid Subcategoria s, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("categorias", repoCategoria.findAll());
			model.addAttribute("subcategorias", repo.findAll());
			return "subcategoria";
		}

		if (s.getNombreCategoria().equals("0")) {
			model.addAttribute("error", "Por favor selecciona una categor�a.");
			model.addAttribute("categorias", repoCategoria.findAll());
			model.addAttribute("subcategorias", repo.findAll());
			return "subcategoria";
		}

		if (s.getNombre().equals("")) {
			model.addAttribute("error", "Por favor ingresa un nombre para la subcategor�a.");
			model.addAttribute("categorias", repoCategoria.findAll());
			model.addAttribute("subcategorias", repo.findAll());
			return "subcategoria";
		}

		List<Subcategoria> lista = (List<Subcategoria>) repo.findAll();
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getNombre().equals(s.getNombre())) {
				model.addAttribute("error", "Ya hay registrada una subcategor�a por este nombre.");
				model.addAttribute("categorias", repoCategoria.findAll());
				model.addAttribute("subcategorias", repo.findAll());
				return "subcategoria";
			}
		}

		Categoria c = repoCategoria.findByNombre(s.getNombreCategoria());
		s.setCategoria(c);

		repo.save(s);
		model.addAttribute("subcategoria", new Subcategoria());
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repo.findAll());
		return "redirect:/subcategoria";
	}

	@GetMapping("/admin/buscarSubcategoria/{id}")
	public String buscar(@PathVariable("id") int id, Model model) {

		Subcategoria s = repo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Subcategor�a inv�lida Id:" + id));

		model.addAttribute("subcategoria", s);

		return "search-subcategoria";

	}

	@GetMapping("/admin/modificarSubcategoria/{id}")
	public String modificar(@PathVariable("id") int id, @Valid Subcategoria s, BindingResult result, Model model) {

		if (result.hasErrors()) {
			s.setId(id);
			return "modificar-subcategoria";
		}

		repo.save(s);
		model.addAttribute("subcategorias", repo.findAll());

		return "modificar-subcategoria";

	}

	@GetMapping("/admin/eliminarSubcategoria/{id}")
	public String eliminar(@PathVariable("id") int id, Model model) {
		Subcategoria s = repo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Subcategor�a inv�lida Id:" + id));
		repo.delete(s);
		model.addAttribute("subcategoria", new Subcategoria());
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repo.findAll());
		return "subcategoria";
	}

}
