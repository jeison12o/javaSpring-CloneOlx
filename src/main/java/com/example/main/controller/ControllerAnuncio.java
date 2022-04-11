package com.example.main.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.utils.ObjectUtils;
import com.example.main.CloudinaryConfig;
import com.example.main.domain.Anuncio;
import com.example.main.domain.Subcategoria;
import com.example.main.domain.Usuario;
import com.example.main.repository.RepoAnuncio;
import com.example.main.repository.RepoCategoria;
import com.example.main.repository.RepoSubcategoria;
import com.example.main.repository.RepoUsuario;
import com.example.main.util.UserAutenticado;

@Controller
public class ControllerAnuncio {

	@Autowired
	private RepoAnuncio repo;

	@Autowired
	private RepoUsuario repoUsuario;

	@Autowired
	private RepoCategoria repoCategoria;

	@Autowired
	private RepoSubcategoria repoSubcategoria;
	
	@Autowired
    private CloudinaryConfig cloudc;
	
	@Autowired
	private UserAutenticado userAutenticado;

	@GetMapping("/subcategoria/{idSubcategoria}")
	public String subcategoria(@PathVariable("idSubcategoria") int idSubcategoria, Model model) {
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		Subcategoria sub = repoSubcategoria.findById(idSubcategoria).orElseThrow(() -> new IllegalArgumentException("Subcategoría inválida Id:" + idSubcategoria));
		model.addAttribute("listaAnuncios", repo.findAllBySubcategoria(sub));
		return "index";
	}
	
	@GetMapping("/user/subcategoria/{idSubcategoria}")
	public String subcategoriaUser(@PathVariable("idSubcategoria") int idSubcategoria, Model model) {
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		Subcategoria sub = repoSubcategoria.findById(idSubcategoria).orElseThrow(() -> new IllegalArgumentException("Subcategoría inválida Id:" + idSubcategoria));
		model.addAttribute("listaAnuncios", repo.findAllBySubcategoria(sub));
		return "menu";
	}

	@GetMapping("/palabraClave")
	public String palabraClave( @RequestParam("palabra") String palabra,Model model) {
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaAnuncios", repo.findAllByTituloOrDescripcionContainingIgnoreCase(palabra,palabra));
		return "index"; 
	}
	
	@GetMapping("/user/palabraClave")
	public String palabraClaveUSer( @RequestParam("palabra") String palabra,Model model) {
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaAnuncios", repo.findAllByTituloOrDescripcionContainingIgnoreCase(palabra,palabra));
		return "menu";
	}

	@GetMapping("/user/anuncios")
	public String menu(Model model) {
		UserDetails user = userAutenticado.getAuth();
		Usuario usuario = repoUsuario.findByCorreo(user.getUsername());
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaAnuncios", repo.findAllByUsuario(usuario));
		return "myAnuncios";
	}

	@GetMapping("/user/registrarAnuncio")
	public String registrarAnuncio(Model model) {
		model.addAttribute("anuncio", new Anuncio());
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		return "crearAnuncio";
	}

	@PostMapping("/user/guardarAnuncio")
	public String guardar(@Valid Anuncio anuncio, BindingResult result, Model model, @RequestParam("file") MultipartFile file) {
		anuncio.setId(0);
		if (result.hasErrors()) {
			model.addAttribute("categorias", repoCategoria.findAll());
			model.addAttribute("subcategorias", repoSubcategoria.findAll());
			return "crearAnuncio";
		}
		UserDetails user = userAutenticado.getAuth();
		Usuario usuario = repoUsuario.findByCorreo(user.getUsername());
		anuncio.setUsuario(usuario);
		try {
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            System.out.println(uploadResult.get("url").toString());
            anuncio.setUrlFoto(uploadResult.get("url").toString());	
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
		repo.save(anuncio);
		return "redirect:/user/anuncios";
	}

	@GetMapping("/user/buscarAnuncio/{id}")
	public String buscar(@PathVariable("id") int id, Model model) {
		Anuncio anuncio = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("anuncio inválido Id:" + id));
		model.addAttribute("anuncio", anuncio);
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		return "actualizarAnuncio";
	}

	@PostMapping("/user/modificarAnuncio")
	public String modificar(@Valid Anuncio anuncio, BindingResult result,Model model, @RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl) {
		if (result.hasErrors()) {
			model.addAttribute("anuncio", anuncio);
			model.addAttribute("categorias", repoCategoria.findAll());
			model.addAttribute("subcategorias", repoSubcategoria.findAll());
			return "actualizarAnuncio";
		}
		UserDetails user = userAutenticado.getAuth();
		Usuario usuario = repoUsuario.findByCorreo(user.getUsername());
		anuncio.setUsuario(usuario);
		if (cambioUrl) {
			try {
	            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
	            System.out.println(uploadResult.get("url").toString());
	            anuncio.setUrlFoto(uploadResult.get("url").toString());	
	        } catch (Exception e) {
	        	System.out.println(e.getMessage());
	        }
		}
		repo.save(anuncio);
		return "redirect:/user/anuncios";
	}

	@GetMapping("/user/eliminarAnuncio/{id}")
	public String eliminar(@PathVariable("id") int id, Model model) {
		Anuncio anuncio = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Anuncio inválido Id:" + id));
		repo.delete(anuncio);
		return "redirect:/user/anuncios";
	}

	@GetMapping("/detalle/{id}")
	public String detalle(@PathVariable("id") int id, Model model) {
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		Anuncio anuncio = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("anuncio inválido Id:" + id));
		model.addAttribute("anuncio", anuncio);
		Usuario vendedor = repoUsuario.findById(anuncio.getUsuario().getId()).orElseThrow(() -> new IllegalArgumentException("Usuario 		inválido Id:" + anuncio.getUsuario().getId()));
		model.addAttribute("vendedor", vendedor);
		model.addAttribute("idDueno", 0);
		return "detalleAnuncio";
	}
	
	@GetMapping("/user/detalle/{id}")
	public String detalleUser(@PathVariable("id") int id, Model model) {
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		Anuncio anuncio = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("anuncio inválido Id:" + id));
		model.addAttribute("anuncio", anuncio);
		Usuario vendedor = repoUsuario.findById(anuncio.getUsuario().getId()).orElseThrow(() -> new IllegalArgumentException("Usuario 		inválido Id:" + anuncio.getUsuario().getId()));
		model.addAttribute("vendedor", vendedor);
		UserDetails user = userAutenticado.getAuth();
		Usuario usuario = repoUsuario.findByCorreo(user.getUsername());
		model.addAttribute("idDueno", usuario.getId());
		return "detalleAnuncio";
	}

	@PostMapping("/user/subirImagen")
	public @ResponseBody String subirImagen(@RequestParam("file") MultipartFile file) {
			try {
	            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
	            System.out.println(uploadResult.get("url").toString());
	            return uploadResult.get("url").toString();
	        } catch (Exception e) {
	        	System.out.println(e.getMessage());
	            e.printStackTrace();
	            return "";
	        }
	}

}
