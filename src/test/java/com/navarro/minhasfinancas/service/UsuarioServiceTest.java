package com.navarro.minhasfinancas.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.navarro.minhasfinancas.exception.RegraNegocioException;
import com.navarro.minhasfinancas.model.repository.UsuarioRepository;
import com.navarro.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")

public class UsuarioServiceTest {
	
	public static String EMAIL = "fulano@email.com"; 
	public static String NOME = "Fulano das Flores"; 
	
	UsuarioService service;
	
	@MockBean
	UsuarioRepository repository;
	
	@BeforeEach
	public void setUp(){
		service = new UsuarioServiceImpl(repository);
	}
	
	@Test
	public void deveValidarEmail() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {service.validarEmail(EMAIL);});
	}
 
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> {service.validarEmail(EMAIL);});
	}
}
