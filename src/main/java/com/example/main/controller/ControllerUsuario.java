package com.example.main.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.main.domain.Authority;
import com.example.main.domain.Usuario;
import com.example.main.repository.AuthorityRepository;
import com.example.main.repository.RepoAdministrador;
import com.example.main.repository.RepoAnuncio;
import com.example.main.repository.RepoCategoria;
import com.example.main.repository.RepoSubcategoria;
import com.example.main.repository.RepoUsuario;
import com.example.main.util.Passgenerator;
import com.example.main.util.UserAutenticado;


@Controller
public class ControllerUsuario {

	@Autowired
	private RepoUsuario repo;

	@Autowired
	private RepoAnuncio repoAnuncio;

	@Autowired
	private RepoCategoria repoCategoria;

	@Autowired
	private RepoSubcategoria repoSubcategoria;

	@Autowired
	private RepoAdministrador repoAdministrador;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Autowired
    private Passgenerator passgenerator;
	
	@Autowired
	private UserAutenticado userAutenticado;

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaAnuncios", repoAnuncio.findAll());
		return "index";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		return "login";
	}
	
	@GetMapping("/download")
	public String download(Model model) {
		return "descarga";
	}

	@GetMapping("/registrarse")
	public String registrarse(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "registroUsuario";
	}
	

	@GetMapping("/menu")
	public String menuV(Model model) {
		String url = "";
		UserDetails user = userAutenticado.getAuth();
		for(GrantedAuthority grantedAuthority: user.getAuthorities()) {
			if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
				model.addAttribute("categorias", repoCategoria.findAll());
				model.addAttribute("subcategorias", repoSubcategoria.findAll());
				model.addAttribute("listaAnuncios", repoAnuncio.findAll());
				url = "menu";
			} else if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
				url = "administrador";
			}
		}
		return url;
	}

	

	@PostMapping("/guardarUsuario")
	public String guardar(@Valid Usuario usuario, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("error", "la repeticion de la contrasena debe ser igual a la contrasena");
			return "registroUsuario";
		}
		if (!usuario.getContrasena().equals(usuario.getRepContrasena())) {
			model.addAttribute("error", "la repeticion de la contrasena debe ser igual a la contrasena");
			return "registroUsuario";
		}
		
		try {
			if(valPass(usuario.getContrasena())) {
				Authority autorizacion= authorityRepository.findByAuthority("ROLE_USER");      
		        Set<Authority> authority= new HashSet<Authority>();
		        authority.add(autorizacion);
		        usuario.setAuthority(authority);
		        usuario.setContrasena(passgenerator.enciptarPassword(usuario.getContrasena()));
				repo.save(usuario);
			}else {
				model.addAttribute("errorContrasena", "La contrasena debe contener minimo una mayuscula, una minuscula, un numero y un tamano mayor o igual a 6.");
				return "registroUsuario";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			model.addAttribute("error", "error al guardar base de datos");
			return "registroUsuario";
		}
		return "redirect:/";
	}

	@GetMapping("/user/buscarUsuario")
	public String buscar(Model model) {
		UserDetails user = userAutenticado.getAuth();
		Usuario usuario = repo.findByCorreo(user.getUsername());
		model.addAttribute("usuario", usuario);
		return "actualizarUsuario";
	}

	@PostMapping("/user/modificarUsuario")
	public String modificar(@Valid Usuario usuario, BindingResult result, Model model) {
		if (result.hasErrors()) {
			System.out.println("error " +result.toString());
			return "actualizarUsuario";
		}
		try {
			UserDetails user = userAutenticado.getAuth();
			Usuario usu = repo.findByCorreo(user.getUsername());
			System.out.println("id actualizar" + usu.getId());
			usu.setNombre(usuario.getNombre());
			usu.setApellido(usuario.getApellido());
			usu.setDireccion(usuario.getDireccion());
			usu.setTelefono(usuario.getTelefono());
			usu.setFechaNacimiento(usuario.getFechaNacimiento());
			repo.save(usu);
			return "redirect:/menu";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "actualizarUsuario";
		}	
	}

	@GetMapping("/user/eliminarUsuario")
	public String eliminar(Model model) {
		UserDetails user = userAutenticado.getAuth();
		Usuario usuario = repo.findByCorreo(user.getUsername());
		repo.delete(usuario);
		return "redirect:/";
	}
	
	public boolean valPass(String pass) {
		boolean hasCap = false;
		boolean hasLow = false;
		boolean hasNum = false;
		char c;
		for(int i = 0; i < pass.length(); i++) {
			c = pass.charAt(i);
			if(Character.isDigit(c)){
				hasNum = true;
			}else if(Character.isUpperCase(c)) {
				hasCap = true;
			}else if(Character.isLowerCase(c)) {
				hasLow = true;
			}
			if(hasCap && hasLow && hasNum) {
				return true;
			}
		}
		return false;
	}

}
