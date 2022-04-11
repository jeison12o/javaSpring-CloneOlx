package com.example.main.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.main.domain.Administrador;
import com.example.main.repository.RepoAdministrador;

@Controller
public class ControllerAdministrador {

	@Autowired
	private RepoAdministrador repo;
	
	@GetMapping("/admin/gestionAdministrador")
	public String gestionAdministrador(Model model) {
		model.addAttribute("admin", new Administrador());
		return "gestionAdministrador";
	}
	
	
	@PostMapping("/admin/guardarAdmin")
	public String guardar(@Valid Administrador admin, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "guardar-Admin";
		}
		repo.save(admin);
		model.addAttribute("listaAdmin", repo.findAll());
		return "menu-admin";
	}
	
	@GetMapping("/admin/buscarAdmin/{id}")
	public String buscar(@PathVariable("id") int id, Model model) {
		Administrador admin = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario inválido Id:" + id));
		model.addAttribute("admin", admin);
		return "modificar-admin";
	}
	
	@PostMapping("/admin/modificarAdmin/{id}")
	public String modificar(@PathVariable("id") int id, @Valid Administrador admin, BindingResult result, Model model) {
		if(result.hasErrors()) {
			admin.setId(id);
			return "modificar-admin";
		}
		repo.save(admin);
		model.addAttribute("administradores", repo.findAll());
		return "menu";
	}
	

	@GetMapping("/admin/eliminarAdmin/{id}")
	public String eliminar(@PathVariable("id") int id, Model model) {
		Administrador admin = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario inválido Id:" + id));
		repo.delete(admin);
		model.addAttribute("lista", repo.findAll());
		return "menu-admin";
	}
}
