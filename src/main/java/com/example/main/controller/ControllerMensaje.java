package com.example.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
public class ControllerMensaje {

	@Autowired
	private RepoMensaje repoMensaje;
	
	@Autowired
	private RepoChat repoChat;
	
	@Autowired
	private RepoUsuario repoUsuario;
	
	@Autowired
	private RepoCategoria repoCategoria;

	@Autowired
	private RepoSubcategoria repoSubcategoria;
	
	@Autowired
	private RepoAnuncio repoAnuncio;
	
	@Autowired
	private UserAutenticado userAutenticado;
	
	@GetMapping("/user/mensajes")
	public String mensajes(Model model) {
		UserDetails user = userAutenticado.getAuth();
		Usuario usuarioInstante = repoUsuario.findByCorreo(user.getUsername());
		model.addAttribute("idDueno", usuarioInstante.getId());
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		List<Chat> listaChatsComprador = repoChat.listaChatsComprador(usuarioInstante);
		List<Chat> listaChatsVendedor = repoChat.listaChatsVendedor(usuarioInstante);
		List<Chat> listaChats = new ArrayList();
		listaChatsComprador.forEach(x ->  listaChats.add(x));
		listaChatsVendedor.forEach(x ->  listaChats.add(x));
		model.addAttribute("listaChats", listaChats);
		Chat chat = new Chat();
		chat.setId(0);
		model.addAttribute("chat", chat);
		return "mensajes";
	}
	
	
	@GetMapping("/user/mensajes/chat/{idChat}")
	public String mensajes( @PathVariable("idChat") int idChat, Model model) {
		UserDetails user = userAutenticado.getAuth();
		Usuario usuarioInstante = repoUsuario.findByCorreo(user.getUsername());
		model.addAttribute("idDueno", usuarioInstante.getId());
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll()); 
		List<Chat> listaChatsComprador = repoChat.listaChatsComprador(usuarioInstante);
		List<Chat> listaChatsVendedor = repoChat.listaChatsVendedor(usuarioInstante);
		List<Chat> listaChats = new ArrayList();
		listaChatsComprador.forEach(x ->  listaChats.add(x));
		listaChatsVendedor.forEach(x ->  listaChats.add(x));
		model.addAttribute("listaChats", listaChats);
		Chat chat = repoChat.findById(idChat).get();
		model.addAttribute("chat", chat);
		model.addAttribute("listaMensajes", repoMensaje.listarMensajesChats(chat));
		return "mensajes";
	}
	
	
	@PostMapping("/user/guardarMensaje")
	public String guardar( @RequestParam("idChat") int idChat ,@RequestParam("mensaje") String mensaje, Model model) {
		UserDetails user = userAutenticado.getAuth();
		Usuario usuarioInstante = repoUsuario.findByCorreo(user.getUsername());
		model.addAttribute("idDueno", usuarioInstante.getId());
		model.addAttribute("categorias", repoCategoria.findAll());
		model.addAttribute("subcategorias", repoSubcategoria.findAll());
		List<Chat> listaChatsComprador = repoChat.listaChatsComprador(usuarioInstante);
		List<Chat> listaChatsVendedor = repoChat.listaChatsVendedor(usuarioInstante);
		
		List<Chat> listaChats = new ArrayList();
		listaChatsComprador.forEach(x ->  listaChats.add(x));
		listaChatsVendedor.forEach(x ->  listaChats.add(x));
		model.addAttribute("listaChats", listaChats);
		if(mensaje.isEmpty()) {
			Chat chat = new Chat();
			chat.setId(0);
			model.addAttribute("chat", chat);
			model.addAttribute("error", "debe ingresar el mensaje para poder enviar");
			return "mensajes";
		}
		if(idChat==0) {
			Chat chat = new Chat();
			chat.setId(0);
			model.addAttribute("chat", chat);
			model.addAttribute("error", "debe escojer el chat cual enviar el mensaje");
			return "mensajes";
		}
		Chat chat = repoChat.findById(idChat).get();
		Mensaje m = new Mensaje();
		m.setMensaje(mensaje);
		m.setChat(chat);
		m.setFecha(new Date().toString());
		m.setEstado(false);
		m.setRemitente(usuarioInstante);
		repoMensaje.save(m);
		model.addAttribute("listaMensajes", repoMensaje.listarMensajesChats(chat));
		model.addAttribute("chat", chat);
		return "mensajes";
	}
	
	
	
	
	
	@GetMapping("/buscarMensaje/{id}")
	public String buscar(@PathVariable("id") int id, Model model) {
		Mensaje mensaje = repoMensaje.findById(id).orElseThrow(() -> new IllegalArgumentException("Mensaje inválido Id:" + id));
		model.addAttribute("mensaje", mensaje);
		return "buscar-mensaje";
	}
	
	@PostMapping("/modificarMensaje/{id}")
	public String modificar(@PathVariable("id") int id, @Valid Mensaje mensaje, BindingResult result, Model model) {
		if(result.hasErrors()) {
			mensaje.setId(id);
			return "modificar-mensaje";
		}
		repoMensaje.save(mensaje);
		model.addAttribute("mensajes", repoMensaje.findAll());
		return "modificar-mensaje";
	}
	
	@GetMapping("/eliminarMensaje/{id}")
	public String eliminar(@PathVariable("id") int id, Model model) {
		Mensaje mensaje = repoMensaje.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario inválido Id:" + id));
		repoMensaje.delete(mensaje);
		model.addAttribute("lista", repoMensaje.findAll());
		return "mensajes-comprador";
		
	}
}
