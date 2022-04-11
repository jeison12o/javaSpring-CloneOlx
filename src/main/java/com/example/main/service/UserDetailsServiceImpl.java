package com.example.main.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.main.domain.Administrador;
import com.example.main.domain.Authority;
import com.example.main.domain.Usuario;
import com.example.main.repository.RepoAdministrador;
import com.example.main.repository.RepoUsuario;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    RepoUsuario usuarioRepo;
    
    @Autowired
    RepoAdministrador adminRepo;
	
    @Override
     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	UserDetails user=null;
    	List grantList = new ArrayList();
    	Administrador admin = adminRepo.findByCorreo(email);
    	Usuario usuario = usuarioRepo.findByCorreo(email);
    	
    	if (admin != null) {
    		for (Authority authority: admin.getAuthority()) {
    	        // ROLE_USER, ROLE_ADMIN,..
    	        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
    	        grantList.add(grantedAuthority);
    	    }
    		user = (UserDetails) new User(admin.getCorreo(), admin.getContrasena(), grantList);
    		System.out.println("admin");
    		
		} else if(usuario != null) {
			for (Authority authority: usuario.getAuthority()) {
		        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
		        grantList.add(grantedAuthority);
		    }
			System.out.println("user");
			user = (UserDetails) new User(usuario.getCorreo(), usuario.getContrasena(), grantList);
		}
    	
    	if (user != null) {
			return user;
		}else {
			System.out.println("error al logiar");
			throw new UsernameNotFoundException("No existe usuario");
		}
    }
}