package com.example.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.main.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	String[] resources = new String[] { "/include/**", "/css/**", "/icons/**", "/images/**", "/js/**", "/layer/**" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(resources).permitAll().antMatchers("/","/signup","/download", "/registrarse", "/guardarUsuario", "/subcategoria/*", "/palabraClave", "/detalle/*").permitAll()
				.antMatchers("/admin*").access("hasRole('ADMIN') or hasRole('USER')")
				.antMatchers("/user*").access("hasRole('USER')")
				.anyRequest().authenticated().and().formLogin()
				.loginPage("/login").permitAll()
				.defaultSuccessUrl("/menu").failureUrl("/signup?error=true")
				.usernameParameter("email").passwordParameter("contrasena").and().logout().permitAll()
				.logoutSuccessUrl("/signup?logout=true");
	}

	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		bCryptPasswordEncoder = new BCryptPasswordEncoder(4);
		return bCryptPasswordEncoder;
	}

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
}