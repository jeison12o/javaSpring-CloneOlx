package com.example.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import com.example.main.domain.Anuncio;
import com.example.main.domain.Chat;
import com.example.main.domain.Mensaje;
import com.example.main.domain.Usuario;
import com.example.main.repository.RepoAnuncio;
import com.example.main.repository.RepoCategoria;
import com.example.main.repository.RepoChat;
import com.example.main.repository.RepoMensaje;
import com.example.main.repository.RepoSubcategoria;
import com.example.main.repository.RepoUsuario;
import com.example.main.util.UserAutenticado;

@Controller
public class ControllerChat {

	@Autowired
	private RepoChat repo;
	
	@Autowired
	private RepoUsuario repoUsuario;
	
	@Autowired
	private RepoCategoria repoCategoria;

	@Autowired
	private RepoSubcategoria repoSubcategoria;
	
	@Autowired
	private RepoAnuncio repoAnuncio;
	
	@Autowired
	private RepoMensaje repoMensaje;
	
	@Autowired
	private UserAutenticado userAutenticado;
	
	@PostMapping("/user/chat")
	public String chat(@RequestParam("idVendedor") int idVendedor, @RequestParam("idAnuncio") int idAnuncio ,Model model) {
		UserDetails user = userAutenticado.getAuth();
		Usuario usuario = repoUsuario.findByCorreo(user.getUsername());
		model.addAttribute("idDueno", usuario.getId());
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		model.addAttribute("anuncio", repoAnuncio.findById(idAnuncio).get());
		model.addAttribute("vendedor", repoUsuario.findById(idVendedor).get());
		model.addAttribute("comprador", usuario);
		return "chat";
	}
	
	@PostMapping("/user/guardarChat")
	public String guardar(@RequestParam("mensaje") String mensaje, @RequestParam("idAnuncio") int idAnuncio, Model model, @RequestParam("idVendedor") int idVendedor) {
		UserDetails user = userAutenticado.getAuth();
		Usuario usuarioInstante = repoUsuario.findByCorreo(user.getUsername());
		model.addAttribute("idDueno", usuarioInstante.getId());
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		if(mensaje.isEmpty()) {
			model.addAttribute("anuncio", repoAnuncio.findById(idAnuncio).get());
			model.addAttribute("vendedor", repoUsuario.findById(idVendedor).get());
			model.addAttribute("comprador", usuarioInstante);
			model.addAttribute("error", "debe ingresar el mensaje para poder enviar");
			return "chat";
		} 
		Chat chat = new Chat();
		chat.setAnuncio(repoAnuncio.findById(idAnuncio).get());
		chat.setUsuario(usuarioInstante);
		chat = repo.save(chat);
		
		Mensaje m = new Mensaje();
		m.setMensaje(mensaje);
		m.setChat(chat);
		m.setFecha(new Date().toString());
		m.setEstado(false);
		m.setRemitente(usuarioInstante);
		repoMensaje.save(m);
		
		List<Chat> listaChatsComprador = repo.listaChatsComprador(usuarioInstante);
		List<Chat> listaChatsVendedor = repo.listaChatsVendedor(usuarioInstante);
		
		List<Chat> listaChats = new ArrayList();
		listaChatsComprador.forEach(x ->  listaChats.add(x));
		listaChatsVendedor.forEach(x ->  listaChats.add(x));
		model.addAttribute("listaChats", listaChats);
		model.addAttribute("listaMensajes", repoMensaje.listarMensajesChats(chat));
		model.addAttribute("chat", chat);
		return "mensajes";
	}
	
	
	
	
	
	@GetMapping("/buscarChat/{id}")
	public String buscar(@PathVariable("id") int id, Model model) {
		Chat chat = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario inválido Id:" + id));
		model.addAttribute("chat", chat);
		return "modificar-chat";
	}
	
	@PostMapping("/modificarChat/{id}")
	public String modificar(@PathVariable("id") int id, @Valid Chat chat, BindingResult result, Model model) {
		if(result.hasErrors()) {
			chat.setId(id);
			return "modificar-chat";
		}
		repo.save(chat);
		model.addAttribute("lista", repo.findAll());
		return "chat";
	}
	
	
	@GetMapping("/eliminarChat/{id}")
	public String eliminar(@PathVariable("id") int id, Model model) {
		Chat chat = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario inválido Id:" + id));
		repo.delete(chat);
		model.addAttribute("lista", repo.findAll());
		return "chat";
	}
}
