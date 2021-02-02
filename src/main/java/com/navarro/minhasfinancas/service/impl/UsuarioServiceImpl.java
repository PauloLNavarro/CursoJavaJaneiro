package com.navarro.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.navarro.minhasfinancas.exception.RegraNegocioException;
import com.navarro.minhasfinancas.model.entity.Usuario;
import com.navarro.minhasfinancas.model.repository.UsuarioRepository;
import com.navarro.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	
	private UsuarioRepository repository;
		
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha invalida.")
		}
		
		return usuario.get();
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validarEmail(String email) {

		boolean existe = repository.existsByEmail(email);//verifica se existe usuario pelo email
		if(existe) {
			throw new RegraNegocioException("Já Existe um usuário cadastrado com esse email");
		}
	}

}
