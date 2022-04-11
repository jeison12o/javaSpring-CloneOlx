package com.example.main;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringBootApplication
public class JeeproyectoApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(JeeproyectoApplication.class, args);
	}
	@Bean
	public LocaleResolver localeResolver() {
	  CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
	  //cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
	  cookieLocaleResolver.setDefaultLocale(new Locale ("en","EN"));
	  cookieLocaleResolver.setCookieName("lang");
	  return cookieLocaleResolver;
	}
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	@Bean
	public MessageSource messageSource() {
	  ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	  messageSource.setBasename("classpath:messages/messages");
	  messageSource.setDefaultEncoding("UTF-8");
	  return messageSource;
	}
	
	@Bean
	public LocalValidatorFactoryBean getValidator() {
	    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	    bean.setValidationMessageSource(messageSource());
	    return bean;
	}

	
}
