package com.navarro.minhasfinancas.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.navarro.minhasfinancas.exception.ErroAutenticacao;
import com.navarro.minhasfinancas.exception.RegraNegocioException;
import com.navarro.minhasfinancas.model.entity.Usuario;
import com.navarro.minhasfinancas.model.repository.UsuarioRepository;
import com.navarro.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")

public class UsuarioServiceTest {

	public static String EMAIL = "fulano@email.com";
	public static String NOME = "Fulano das Flores";
	public static String SENHA = "Senha";

	@SpyBean
	UsuarioServiceImpl service;

	@MockBean
	UsuarioRepository repository;

	@Test
	public void deveSalvarumUsuario() {
		// Cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1l).nome(NOME).email(EMAIL).senha(SENHA).build();

		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		// Ação
		service.salvarUsuario(new Usuario());

		// Verificação
		org.assertj.core.api.Assertions.assertThat(usuario).isNotNull();
		org.assertj.core.api.Assertions.assertThat(usuario.getId()).isEqualTo(1l);
		org.assertj.core.api.Assertions.assertThat(usuario.getNome()).isEqualTo(NOME);
		org.assertj.core.api.Assertions.assertThat(usuario.getEmail()).isEqualTo(EMAIL);
		org.assertj.core.api.Assertions.assertThat(usuario.getSenha()).isEqualTo(SENHA);

	}

	@Test
	public void naoDeveCadastrarUmUsuarioComEmailJaCadastrado() {
		// Cenário
		Usuario usuario = Usuario.builder().email(EMAIL).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(EMAIL);

		// Ação
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> {
			service.salvarUsuario(usuario);
		});

		// verificação

		Mockito.verify(repository, Mockito.never()).save(usuario);

	}

	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		// Cenário
		Usuario usuario = Usuario.builder().email(EMAIL).senha(SENHA).id(1l).build();
		Mockito.when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));

		// Ação
		Usuario result = service.autenticar(EMAIL, SENHA);

		// Verificação
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();

	}

	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		// Cenário

		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		// Ação
		Throwable exeption = org.assertj.core.api.Assertions.catchThrowable(() -> service.autenticar(EMAIL, SENHA));

		// Verificação
		org.assertj.core.api.Assertions.assertThat(exeption).isInstanceOf(ErroAutenticacao.class)
				.hasMessage("Usuário não encontrado para o email informado.");

	}

	@Test
	public void deveLancarErroQuandoSenhaNaoBate() {
		// Cenário
		Usuario usuario = Usuario.builder().email(EMAIL).senha(SENHA).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

		// Ação
		Throwable exeption = org.assertj.core.api.Assertions.catchThrowable(() -> service.autenticar(EMAIL, "123"));

		// Verificação
		org.assertj.core.api.Assertions.assertThat(exeption).isInstanceOf(ErroAutenticacao.class)
				.hasMessage("Senha inválida.");
	}

	@Test
	public void deveValidarEmail() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
			service.validarEmail(EMAIL);
		});
	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> {
			service.validarEmail(EMAIL);
		});
	}
}
